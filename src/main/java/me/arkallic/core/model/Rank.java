package me.arkallic.core.model;

public class Rank {

    private final String name;
    private final String description;
    private final String prefix;
    private final int hierarchy;
    private final boolean defaultRank;

    public Rank(String name, String description, String prefix, int hierarchy, boolean defaultRank) {
        this.name = name;
        this.description = description;
        this.prefix = prefix;
        this.hierarchy = hierarchy;
        this.defaultRank = defaultRank;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrefix() {
        return prefix;
    }
    public int getHierarchy() {
        return hierarchy;
    }

    public boolean isDefault() {
        return defaultRank;
    }
}
