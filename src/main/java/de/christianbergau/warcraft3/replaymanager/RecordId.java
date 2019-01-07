package de.christianbergau.warcraft3.replaymanager;

public class RecordId {
    private byte value;

    public RecordId(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }

    @Override
    public String toString() {
        return "RecordId{" +
                "value=" + value +
                '}';
    }
}
