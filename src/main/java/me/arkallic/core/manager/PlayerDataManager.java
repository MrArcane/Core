package me.arkallic.core.manager;

import me.arkallic.core.Core;
import me.arkallic.core.data.LangData;
import me.arkallic.core.data.PlayerData;
import me.arkallic.core.model.Home;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static me.arkallic.core.util.MessageUtil.send;

public class PlayerDataManager {
    private final HashMap<UUID, PlayerData> playerData = new HashMap<>();
    private final Core core;
    private final LangData langData;
    private final EconomyManager economyManager;

    public PlayerDataManager(LangData langData, EconomyManager economyManager, Core core) {
        this.core = core;
        this.langData = langData;
        this.economyManager = economyManager;
    }

    public void register(UUID uuid) {
        int defaultMaxHomes = core.getConfig().getInt(PlayerData.defaultData.max_homes);
        int defaultCurrency = core.getConfig().getInt(PlayerData.defaultData.coins);

        playerData.computeIfAbsent(uuid, _ -> new PlayerData(uuid, core));
        playerData.get(uuid).setDefaultData(uuid, defaultMaxHomes, defaultCurrency);
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


    public void teleportHome(UUID uuid, Home home, int cost) {
        Player player = Bukkit.getPlayer(uuid);
        PlayerData pd = this.playerData.get(uuid);
        if (pd.getCoins() < cost) {
            send(player, String.format("&c&lNot enough coins to teleport home! &f%s &ccoins required.", cost));
            return;
        }
        player.teleport(home.getLocation());
        pd.setCoins(pd.getCoins() - cost);
        send(player,  langData.teleportHome.replace("%HOME%", home.getName()));
        economyManager.getEconomy().payServer(cost);
    }

    public PlayerData getPlayerData(UUID playerUUID) {
        return playerData.get(playerUUID);
    }
}