package de.christianbergau.warcraft3.replaymanager;

public class PlayerId {
    private byte value;

    public PlayerId(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }

    @Override
    public String toString() {
        return "PlayerId{" +
                "value=" + value +
                '}';
    }
}
