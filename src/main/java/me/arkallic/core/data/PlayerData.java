package me.arkallic.core.data;

import me.arkallic.core.Core;
import me.arkallic.core.model.Home;
import me.arkallic.core.wrappers.YMLFileWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


import java.io.IOException;

import java.util.HashMap;
import java.util.UUID;


public class PlayerData {

    private final YMLFileWrapper config;
    private final HashMap<String, Home> homes = new HashMap<>();

    public PlayerData(UUID uuid, Core core) {
        this.config = new YMLFileWrapper("players", uuid + ".yml", core);

        loadHomes();
    }

    public void save() {
        try {
            config.saveFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadDefaultData(UUID uuid, String rank) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            return;
        }

        if (!this.exists() || getConfig().getKeys(false).isEmpty()) {

            if (!getConfig().contains(defaultData.DISPLAYNAME)) {
                getConfig().set(defaultData.DISPLAYNAME, player.getName());
            }
            if (!getConfig().contains(defaultData.RANK)) {
                getConfig().set(defaultData.RANK, rank);
            }
            if (!getConfig().contains(defaultData.MAX_HOMES)) {
                getConfig().set(defaultData.MAX_HOMES, 1);
            }
        }
    }

    private void loadHomes() {

        if (getConfig().getConfigurationSection(defaultData.HOMES) == null) {
            return;
        }

        ConfigurationSection homeSection = this.getConfig().getConfigurationSection(defaultData.HOMES);
        for (String s : homeSection.getKeys(false)) {
            Home home = new Home(s, homeSection.getLocation(s));
            this.homes.putIfAbsent(home.getName(), home);
        }
    }

    private FileConfiguration getConfig() {
        return this.config.getConfig();
    }

    public interface defaultData {
        String RANK = "Settings.Rank";
        String MAX_HOMES = "Settings.Max-homes";
        String HOMES = "Homes.";
        String DISPLAYNAME = "Settings.Display-name";
    }

    public boolean exists() {
        return this.config.getFile().exists();
    }

    public HashMap<String, Home> getHomes() {
        return homes;
    }



    public Home getHome(String name) {
        return homes.get(name);
    }
    public void setHome(String name, Location location) {
        Home home = new Home(name, location);
        homes.putIfAbsent(name, home);
        getConfig().set(defaultData.HOMES + name, home.getLocation());
    }
    public void deleteHome(Home home) {
        homes.remove(home.getName());
        getConfig().set(defaultData.HOMES + home.getName(), null);
    }



    public int getMaxHomes() {

        if (!getConfig().contains(defaultData.MAX_HOMES)) {
            return 0;
        }

        return this.config.getConfig().getInt(defaultData.MAX_HOMES);
    }
    public void setMaxHomes(int amount) {
        this.config.getConfig().set(defaultData.MAX_HOMES, amount);
    }



    public String getDisplayName() {
        return this.getConfig().getString(defaultData.DISPLAYNAME);
    }
    public void setDisplayName(String name) {
        getConfig().set(defaultData.DISPLAYNAME, name);
    }



    public String getRank() {
        return getConfig().getString(defaultData.RANK);
    }
    public void setRank(String name) {
        getConfig().set(defaultData.RANK + name, name);
    }



    public int getCurrentHomes() {

        if (getConfig().getConfigurationSection(defaultData.HOMES) == null) {
            return 0;
        }

        return getConfig().getConfigurationSection(defaultData.HOMES).getKeys(false).size();
    }
}


