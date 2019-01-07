package de.christianbergau.warcraft3.replaymanager;

public class Player {
    public PlayerId playerId;
    public boolean isInitiator;
    public String name;
    public byte race;

    @Override
    public String toString() {
        String raceAsString = "";

        switch (this.race) {
            case Race.HUMAN:
                raceAsString = "Human";
                break;
            case Race.ORC:
                raceAsString = "Orc";
                break;
            case Race.UNDEAD:
                raceAsString = "Undead";
                break;
            case Race.NIGHTELF:
                raceAsString = "Nightelf";
                break;
            case Race.RANDOM:
                raceAsString = "Random";
                break;
        }

        return "Player{" +
                "playerId=" + playerId +
                ", isInitiator=" + isInitiator +
                ", name='" + name + '\'' +
                ", race='" + raceAsString + '\'' +
                '}';
    }

}
