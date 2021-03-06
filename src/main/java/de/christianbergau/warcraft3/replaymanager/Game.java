package de.christianbergau.warcraft3.replaymanager;

import de.christianbergau.warcraft3.replaymanager.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final int TYPE_CUSTOM = 0x01;
    public static final int TYPE_LADDER = 0x08;

    public byte type;
    public String name;
    public int speed;
    public int visibility;
    public int observerMode;
    public boolean teamsPlacedAtNeighboredPlaces;
    public boolean fixedTeams;
    public boolean fullSharedUnitControl;
    public boolean randomHero;
    public boolean randomRaces;
    private Map map;
    public String creator;
    public byte slots;
    public boolean isPrivate;
    public List<Player> players = new ArrayList<>();

    public boolean isLadder() {
        return type == TYPE_LADDER;
    }

    @Override
    public String toString() {
        return "Game{" +
                "type=" + type +
                ", name=" + name +
                ", speed=" + speed +
                ", visibility=" + visibility +
                ", observerMode=" + observerMode +
                ", teamsPlacedAtNeighboredPlaces=" + teamsPlacedAtNeighboredPlaces +
                ", fixedTeams=" + fixedTeams +
                ", fullSharedUnitControl=" + fullSharedUnitControl +
                ", randomHero=" + randomHero +
                ", randomRaces=" + randomRaces +
                ", map=" + map.shortName() +
                ", creator=" + creator +
                ", slots=" + slots +
                ", isPrivate=" + isPrivate +
                '}';
    }

    public void map(Map map) {
        this.map = map;
    }

    public Map map() {
        return map;
    }
}
