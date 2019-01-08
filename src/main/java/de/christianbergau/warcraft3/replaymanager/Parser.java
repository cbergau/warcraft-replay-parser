package de.christianbergau.warcraft3.replaymanager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;

public class Parser {
    public Game parse(String absolutePath) throws Throwable {
        Path path = Paths.get(absolutePath);
        byte[] buffer = Files.readAllBytes(path);

        Game game = new Game();

        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.position(28);

        ReplayHeader replayHeader = new ReplayHeader();
        replayHeader.recordedGameString = ByteUtil.getStringFromByteArray(buffer, 0, 28);
        replayHeader.size = byteBuffer.get(0x001c);
        replayHeader.compressedFileSize = byteBuffer.getInt(0x0020);
        replayHeader.version = byteBuffer.get(0x0024);
        replayHeader.decompressedSize = byteBuffer.getInt(0x0028);
        replayHeader.numberOfBlocks = byteBuffer.get(0x002c);

        byteBuffer.position(replayHeader.size);

        for (int block = 0; block < replayHeader.numberOfBlocks; block++) {
            DataBlockHeader dataBlockHeader = new DataBlockHeader();
            dataBlockHeader.sizeOfCompressedDataBlock = byteBuffer.getShort();
            dataBlockHeader.sizeOfDecompressedDataBlock = byteBuffer.getShort();
            dataBlockHeader.checksum = byteBuffer.getInt();

            byte[] temp = new byte[dataBlockHeader.sizeOfCompressedDataBlock];
            byteBuffer.get(temp, 0, dataBlockHeader.sizeOfCompressedDataBlock);

            Inflater decompresser = new Inflater();
            decompresser.setInput(temp);
            byte[] decompressedRaw = new byte[dataBlockHeader.sizeOfDecompressedDataBlock];
            decompresser.inflate(decompressedRaw);
            ByteBuffer decompressed = ByteBuffer.wrap(decompressedRaw);

            if (block == 0) {
                Player player = parsePlayer(game, decompressed);
                game.players.add(player);
                parseGame(game, decompressed);
            }
        }

        return game;
    }

    private void parseGame(Game game, ByteBuffer decompressed) {
        // Find the first string that is not a null byte and read the game name
        while (true) {
            if ((char) decompressed.get() != 0) {
                decompressed.position(decompressed.position() - 1);
                break;
            }
        }

        //  3 | variable | GameName (null terminated string) (see 4.2)
        StringBuilder gameName = new StringBuilder();

        while (true) {
            char c = (char) decompressed.get();
            // 4 |   1 byte | Nullbyte
            if ((int) c == 0) {
                break;
            }

            gameName.append(c);
        }

        game.name = gameName.toString();

        decompressed.get();

        List<Character> bytes = new ArrayList<>();
        int mask = 0;
        int pos = 0;
        int dpos = 0;

        while (true) {
            byte b = decompressed.get();

            if ((char) b == 0) {
                break;
            }

            if (pos % 8 == 0) {
                mask = ByteUtil.ord((char) b);
            } else {
                if ((mask & (0x1 << (pos % 8))) == 0) {
                    bytes.add(dpos++, (char) ByteUtil.ord((char) (b - 1)));
                } else {
                    bytes.add(dpos++, (char) ByteUtil.ord((char) b));
                }
            }

            pos++;
        }

        // 4.4 [GameSettings]

        // Game Speed
        game.speed = ByteUtil.ord(bytes.get(0));

        // Game Visibility
        int visibility = ByteUtil.ord(bytes.get(1));

        if ((visibility & 1) > 0) {
            game.visibility = GameVisibility.HIDE_TERRAIN;
        } else if ((visibility & 2) > 0) {
            game.visibility = GameVisibility.MAP_EXPLORED;
        } else if ((visibility & 4) > 0) {
            game.visibility = GameVisibility.ALWAYS_VISIBLE;
        } else if ((visibility & 8) > 0) {
            game.visibility = GameVisibility.DEFAULT;
        }

        game.observerMode = (ByteUtil.ord(bytes.get(1)) & 16) + ((ByteUtil.ord(bytes.get(1)) & 32)) * 2;
        game.teamsPlacedAtNeighboredPlaces = (ByteUtil.ord(bytes.get(1)) & 64) > 0;
        game.fixedTeams = ByteUtil.ord(bytes.get(2)) > 0;
        game.fullSharedUnitControl = (ByteUtil.ord(bytes.get(3)) & 1) > 0;
        game.randomHero = (ByteUtil.ord(bytes.get(3)) & 2) > 0;
        game.randomRaces = (ByteUtil.ord(bytes.get(3)) & 4) > 0;

        if ((ByteUtil.ord(bytes.get(3)) & 64) > 0) {
            game.observerMode = ObserverMode.REFEREES;
        }

        // 4.5 [Map&CreatorName]
        StringBuilder concat = new StringBuilder();

        Iterator<Character> iterator = bytes.iterator();

        while (iterator.hasNext()) {
            concat.append(iterator.next());
        }

        String[] mapAndCreator = concat.substring(13).split("\0");

        game.map = mapAndCreator[0];
        game.creator = mapAndCreator[1];

        // 4.6 [PlayerCount]
        game.slots = decompressed.get();
        decompressed.position(decompressed.position() + 3);

        // 4.7 [GameType]
        game.type = (byte) ByteUtil.ord((char) decompressed.get());
        game.isPrivate = ByteUtil.ord((char) decompressed.get()) > 0;

        decompressed.position(decompressed.position() + 6);

        // 4.9 [PlayerList]
        while (decompressed.get() == 0x16) {
            decompressed.position(decompressed.position() - 5);
            Player player = parsePlayer(game, decompressed);
            game.players.add(player);
        }
    }

    private Player parsePlayer(Game game, ByteBuffer decompressed) {
        //@todo just skip 4 bytes
        new RecordId((byte) (decompressed.get() + decompressed.get() + decompressed.get() + decompressed.get()));

        Player player = new Player();
        player.playerId = new PlayerId(decompressed.get());
        player.isInitiator = decompressed.get() == 0;

        StringBuilder name = new StringBuilder();

        for (int i = 0; i <= 20; i++) {
            char c = (char) decompressed.get();
            if ((int) c == 0) {
                break;
            }

            name.append(c);
        }

        // FFA
        if (name.toString().isEmpty()) {
            player.name = "Player " + player.playerId.value();
        } else {
            player.name = name.toString();
        }

        //@todo This seems to be a duplicate
        game.type = decompressed.get();

        if (game.isLadder()) {
            //@todo just skip the bytes
            int runtime = decompressed.getInt();
            player.race(decompressed.get());
        }

        return player;
    }
}
