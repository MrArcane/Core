package me.arkallic.core.manager;

import me.arkallic.core.Core;
import me.arkallic.core.data.PlayerData;
import me.arkallic.core.model.Home;
import me.arkallic.core.model.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static me.arkallic.core.util.MessageUtil.send;

public class PlayerDataManager {
    private final HashMap<UUID, PlayerData> playerData = new HashMap<>();
    private final RankDataManager rankDataManager;
    private final Core core;

    public PlayerDataManager(RankDataManager rankDataManager, Core core) {
        this.rankDataManager = rankDataManager;
        this.core = core;
    }

    public void register(UUID uuid) {
        int defaultMaxHomes = core.getConfig().getInt(PlayerData.defaultData.MAX_HOMES);
        int defaultCurrency = core.getConfig().getInt(PlayerData.defaultData.currency);

        playerData.computeIfAbsent(uuid, _ -> new PlayerData(uuid, core));
        playerData.get(uuid).setDefaultData(uuid, rankDataManager.getDefault().getName(), defaultMaxHomes, defaultCurrency);
    }

    public void unregister(UUID uuid) {
        try {
            getPlayerData(uuid).save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getPlayerData(uuid).getHomes().clear();
        this.playerData.remove(uuid);
    }


    /**
     * Misc
     */

    public boolean playerDataExists(UUID uuid) {
        return this.getPlayerData(uuid).exists();
    }


    public PlayerData getPlayerData(UUID playerUUID) {
        return playerData.get(playerUUID);
    }
}