package me.arkallic.core.model;

import org.bukkit.Location;

public class Home {

    private final String name;
    private final Location location;

    public Home(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
