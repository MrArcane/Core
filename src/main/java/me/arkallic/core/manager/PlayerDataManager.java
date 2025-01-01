package me.arkallic.core.manager;

import me.arkallic.core.Core;
import me.arkallic.core.model.Home;
import me.arkallic.core.model.PlayerData;
import me.arkallic.core.model.PlayerData.defaultData;
import me.arkallic.core.model.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static me.arkallic.core.handler.MessageHandler.send;

public class PlayerDataManager {
    private final HashMap<UUID, PlayerData> playerData = new HashMap<>();
    private final RankManager rankManager;
    private final Core core;

    public PlayerDataManager(RankManager rankManager, Core core) {
        this.rankManager = rankManager;
        this.core = core;
    }

    public void register(UUID uuid) {
        PlayerData player = new PlayerData(uuid, core);
        playerData.computeIfAbsent(uuid, _ -> new PlayerData(uuid, core));
        checkDefaultValues(uuid);


        for (String s : this.getHomesSection(uuid).getKeys(false)) {
            Home home = new Home(s, player.getLocation(defaultData.HOMES + "." + s));
            this.getHomes(uuid).put(home.getName(), home);
        }
    }

    public void unregister(UUID uuid) {
        getPlayerData(uuid).save();
        getHomes(uuid).clear();
        this.playerData.remove(uuid);
    }

    private void checkDefaultValues(UUID uuid) {
        FileConfiguration cfg = this.getPlayerData(uuid).getHandler().getConfig();
        final PlayerData playerData = this.getPlayerData(uuid);

        if (!cfg.contains(defaultData.RANK)) {
            playerData.set(defaultData.RANK, "Player");
        }

        if (!cfg.contains(defaultData.MAX_HOMES)) {
            playerData.set(defaultData.MAX_HOMES, 1);
        }

        if (!cfg.contains(defaultData.HOMES)) {
            playerData.setConfigurationSection(defaultData.HOMES);
        }

        if (!cfg.contains(defaultData.DISPLAYNAME)) {
            playerData.set(defaultData.DISPLAYNAME, Bukkit.getPlayer(uuid).getName());
        }
    }

    /**
     * Booleans
     */

    public boolean playerDataExists(UUID uuid) {
        return this.getPlayerData(uuid).getHandler().getFile().exists();
    }


    /**
     * GETTERS
     */

    public FileConfiguration getConfig(UUID uuid) {
    return getPlayerData(uuid).getHandler().getConfig();
    }


    public PlayerData getPlayerData(UUID playerUUID) {
        return playerData.get(playerUUID);
    }


    public Rank getRank(UUID uuid) {
        return rankManager.getRank(getPlayerData(uuid).getString(defaultData.RANK));
    }

    public String getDisplayName(UUID uuid) {
        return getPlayerData(uuid).getString(defaultData.DISPLAYNAME);
    }


    /**
     * SETTERS
     */

    public void setRank(UUID uuid, String name, boolean showRankChange) {
        getPlayerData(uuid).set(defaultData.RANK, name);
        if (showRankChange) {
            Player player = Bukkit.getPlayer(uuid);
            send(player,  String.format("&7Your rank is now: &a%s", name));
        }
    }

    public void setDisplayName(UUID uuid, String name) {
        getPlayerData(uuid).set(defaultData.DISPLAYNAME, name);
    }


    /**
     * Homes
     */
    public boolean hasHomes(UUID uuid) {
        return !this.getHomes(uuid).isEmpty();
    }

    public boolean homeExists(UUID uuid, String name) {
        return this.getHomes(uuid).containsKey(name);
    }

    public Home getHome(UUID uuid, String name) {
        for (Home h : getHomes(uuid).values()) {
            if (h.getName().equalsIgnoreCase(name)) {
                return h;
            }
        }
        return null;
    }


    public int getMaxHomes(UUID uuid) {
        return this.getConfig(uuid).getInt(defaultData.MAX_HOMES);
    }

    public ConfigurationSection getHomesSection(UUID uuid) {
        return getPlayerData(uuid).getConfigurationSection(defaultData.HOMES);
    }

    public HashMap<String, Home> getHomes(UUID uuid) {
        return this.playerData.get(uuid).getHomes();
    }

    public void setMaxHomes(UUID uuid, int amount) {
        getPlayerData(uuid).set(defaultData.MAX_HOMES, amount);
    }

    public void setHome(UUID uuid, String name, Location location) {
        Home home = new Home(name.toLowerCase(), location);
        getPlayerData(uuid).set(defaultData.HOMES + "." + name.toLowerCase(), location);
        getHomes(uuid).put(home.getName(), home);
    }

    public void deleteHome(UUID uuid, String name) {
        Home home = getHome(uuid, name);
        getHomes(uuid).remove(home.getName());
        getPlayerData(uuid).set(defaultData.HOMES + "." + name.toLowerCase(), null);
    }
}
