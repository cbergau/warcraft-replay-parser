package de.christianbergau.warcraft3.replaymanager.player;

import de.christianbergau.warcraft3.replaymanager.race.*;

public class Player {
    public PlayerId playerId;
    public boolean isInitiator;
    public String name;
    private Race race;

    public void race(byte race) {
        switch (race) {
            case Race.HUMAN:
                this.race = new Human();
                break;
            case Race.ORC:
                this.race = new Orc();
                break;
            case Race.UNDEAD:
                this.race = new Undead();
                break;
            case Race.NIGHTELF:
                this.race = new Nightelf();
                break;
            case Race.RANDOM:
                this.race = new Random();
                break;
        }
    }

    public Race race() {
        return race;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", isInitiator=" + isInitiator +
                ", name='" + name + '\'' +
                ", race=" + race().shortName() +
                '}';
    }
}
