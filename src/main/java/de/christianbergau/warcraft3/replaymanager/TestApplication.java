package de.christianbergau.warcraft3.replaymanager;

import de.christianbergau.warcraft3.replaymanager.player.Player;

import java.io.File;

public class TestApplication {
    public static void main(String... args) {
        Parser parser = new Parser();
        parseFiles(parser, new File(args[0]).listFiles());
    }

    static void parseFiles(Parser parser, File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                parseFiles(parser, file.listFiles()); // Calls same method again.
            } else if (file.getName().endsWith(".w3g")) {
                try {
                    Game game = parser.parse(file.getAbsolutePath());
                    Player player1 = game.players.get(0);
                    Player player2 = game.players.get(1);
                    System.out.println(player1.name + " (" + player1.race().shortName() + ") vs (" + player2.race().shortName() + ") " + player2.name + " on " + game.map);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }
}
