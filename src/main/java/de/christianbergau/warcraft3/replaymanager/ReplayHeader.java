package de.christianbergau.warcraft3.replaymanager;

class ReplayHeader {
    public String recordedGameString;
    public int size;
    public int version;
    public int compressedFileSize;
    public int decompressedSize;
    public int numberOfBlocks;

    @Override
    public String toString() {
        return "ReplayHeader{" +
                "recordedGameString='" + recordedGameString + '\'' +
                ", size=" + size +
                ", version=" + version +
                ", compressedFileSize=" + compressedFileSize +
                ", decompressedSize=" + decompressedSize +
                ", numberOfBlocks=" + numberOfBlocks +
                '}';
    }
}
