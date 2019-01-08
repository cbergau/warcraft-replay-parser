package de.christianbergau.warcraft3.replaymanager.race;

public interface Race {
    public static final byte HUMAN = 0x01;
    public static final byte ORC = 0x02;
    public static final byte NIGHTELF = 0x04;
    public static final byte UNDEAD = 0x08;
    public static final byte DEAMON = 0x10;
    public static final byte RANDOM = 0x20;
    public static final byte SELECTABLE_FIXED = 0x40;

    public String shortName();
}
