package me.arkallic.core.model;

import me.arkallic.core.Core;
import me.arkallic.core.handler.FileHandler;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;

import java.util.HashMap;
import java.util.UUID;


public class PlayerData {

    private final FileHandler handler;
    private final UUID uuid;
    private final HashMap<String, Home> homes = new HashMap<>();
    private final Core core;
    public PlayerData(UUID uuid, Core core) {
        this.uuid = uuid;
        this.core = core;
        this.handler = new FileHandler("players", uuid + ".yml", core);
    }

    public void save() {
        try {
            handler.saveFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface defaultData {
        String RANK = "Settings.Rank";
        String MAX_HOMES = "Settings.Max-homes";
        String HOMES = "Homes";
        String DISPLAYNAME = "Settings.Display-name";
        String DAILIES = "Dailies.";
    }

    /**
     * Getters
     */
    public UUID getUUID() {
        return uuid;
    }

    public HashMap<String, Home> getHomes() {
        return homes;
    }


    public FileHandler getHandler() {
        return handler;
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return handler.getConfig().getConfigurationSection(path);
    }
    public int getInt(String path) {
        return this.handler.getConfig().getInt(path);
    }

    public String getString(String path) {
        return handler.getConfig().getString(path);
    }
    public Location getLocation(String path) {
        return handler.getConfig().getLocation(path);
    }

    public boolean getBoolean(String path) {
        return handler.getConfig().getBoolean(path);
    }
    /**
     * Setter
     */

    public void setConfigurationSection(String name) {
        handler.getConfig().createSection(name);
    }

    public void set(@NotNull String path, Object value) {
        handler.getConfig().set(path, value);
    }

}


