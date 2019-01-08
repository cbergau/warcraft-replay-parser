package de.christianbergau.warcraft3.replaymanager;

public class Map {
    private String creator;
    private String name;

    public Map(String creator, String name) {
        this.creator = creator;
        this.name = name;
    }

    public String creator() {
        return creator;
    }

    public String fullName() {
        return name;
    }

    public String shortName() {
        String[] split = name.split("/");
        String lastPart = split[split.length - 1];
        return lastPart.replaceAll("\\(\\d\\)", "");
    }
}
