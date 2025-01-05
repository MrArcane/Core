package me.arkallic.core.manager;

import me.arkallic.core.Core;
import me.arkallic.core.model.Home;
import me.arkallic.core.data.PlayerData;
import me.arkallic.core.model.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static me.arkallic.core.handler.MessageHandler.send;

public class PlayerDataManager {
    private final HashMap<UUID, PlayerData> playerData = new HashMap<>();
    private final RankDataManager rankDataManager;
    private final Core core;

    public PlayerDataManager(RankDataManager rankDataManager, Core core) {
        this.rankDataManager = rankDataManager;
        this.core = core;
    }

    public void register(UUID uuid) {
        playerData.computeIfAbsent(uuid, _ -> new PlayerData(uuid, core));
        playerData.get(uuid).loadDefaultData(uuid, rankDataManager.getDefault().getName());
    }

    public void unregister(UUID uuid) {
        getPlayerData(uuid).save();
        getHomes(uuid).clear();
        this.playerData.remove(uuid);
    }

    public void rankUp(UUID uuid) {
        PlayerData pd = getPlayerData(uuid);
        Rank currentRank = rankDataManager.getRank(pd.getRank());
        Rank nextRank = null;
        int currentHierarchy = currentRank.getHierarchy();
        int nextHierarchy;

        for (Rank rank : rankDataManager.getRanks()) {
            nextHierarchy = rank.getHierarchy();
            if (currentHierarchy > nextHierarchy) {
                nextRank = rank;
            }
        }
        pd.setRank(nextRank.getName());

    }

    /**
     * Booleans
     */

    public boolean playerDataExists(UUID uuid) {
        return this.getPlayerData(uuid).exists();
    }


    /**
     * GETTERS
     */



    public PlayerData getPlayerData(UUID playerUUID) {
        return playerData.get(playerUUID);
    }


    public Rank getRank(UUID uuid) {
        return rankDataManager.getRank(playerData.get(uuid).getRank());
    }

    public String getDisplayName(UUID uuid) {
        return getPlayerData(uuid).getDisplayName();
    }


    /**
     * SETTERS
     */

    public void setRank(UUID uuid, String name, boolean showRankChange) {
        getPlayerData(uuid).setRank(name);
        if (showRankChange) {
            Player player = Bukkit.getPlayer(uuid);
            send(player,  String.format("&7Your rank is now: &a%s", name));
        }
    }

    public void setDisplayName(UUID uuid, String name) {
        getPlayerData(uuid).setDisplayName(name);
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
        return getPlayerData(uuid).getHome(name);
    }


    public int getMaxHomes(UUID uuid) {
        return getPlayerData(uuid).getMaxHomes();
    }

    public int getCurrentHomes(UUID uuid) {
        return this.playerData.get(uuid).getCurrentHomes();
    }


    public HashMap<String, Home> getHomes(UUID uuid) {
        return this.playerData.get(uuid).getHomes();
    }

    public void setMaxHomes(UUID uuid, int amount) {
        getPlayerData(uuid).setMaxHomes(amount);
    }

    public void setHome(UUID uuid, String name, Location location) {
        getPlayerData(uuid).setHome(name, location);
    }

    public void deleteHome(UUID uuid, String name) {
        Home home = getHome(uuid, name);
        getHomes(uuid).remove(home.getName());
        getPlayerData(uuid).deleteHome(home);
    }
}
