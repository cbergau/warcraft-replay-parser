package de.christianbergau.warcraft3.replaymanager;

public class DataBlockHeader {
    public short sizeOfCompressedDataBlock;
    public short sizeOfDecompressedDataBlock;
    public long checksum;
    public byte recordId;

    @Override
    public String toString() {
        return "DataBlockHeader{" +
                "sizeOfCompressedDataBlock=" + sizeOfCompressedDataBlock +
                ", sizeOfDecompressedDataBlock=" + sizeOfDecompressedDataBlock +
                ", checksum=" + checksum +
                ", recordId=" + recordId +
                '}';
    }
}
