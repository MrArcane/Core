package me.arkallic.core;

import me.arkallic.core.command.*;
import me.arkallic.core.command.coins.BalanceCommand;
import me.arkallic.core.command.coins.CoinsCommand;
import me.arkallic.core.command.home.*;
import me.arkallic.core.data.RankData;
import me.arkallic.core.data.LangData;
import me.arkallic.core.listener.*;
import me.arkallic.core.manager.EconomyManager;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.manager.RankDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;


public final class Core extends JavaPlugin {

    private final RankData rankData = new RankData(this);
    private final RankDataManager rankDataManager = new RankDataManager(rankData);
    private final LangData langData = new LangData(this);
    private final EconomyManager economyManager = new EconomyManager(this);
    private final PlayerDataManager playerDataManager = new PlayerDataManager(rankDataManager,  langData, economyManager, this);

    @Override
    public void onEnable() {
        rankData.initialize();
        rankDataManager.initialize();
        langData.initialize();
        economyManager.initialize();

        loadCommands();
        loadListeners();

        //Reload
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerDataManager.register(player.getUniqueId());
        }

        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        try {
            economyManager.getEconomy().save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Save all User data if the server shuts down
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != null) {
                playerDataManager.unregister(player.getUniqueId());
            }
        }
    }

    private void loadCommands() {
        Bukkit.getPluginCommand("core").setExecutor(new CoreCommand(playerDataManager));
        Bukkit.getPluginCommand("rank").setExecutor(new RankCommand(rankDataManager, playerDataManager));
        Bukkit.getPluginCommand("setrank").setExecutor(new SetRankCommand(rankDataManager, playerDataManager));
        Bukkit.getPluginCommand("home").setExecutor(new HomeCommand(playerDataManager, langData, this));
        Bukkit.getPluginCommand("homes").setExecutor(new HomesCommand(playerDataManager, langData));
        Bukkit.getPluginCommand("sethome").setExecutor(new SetHomeCommand(playerDataManager, langData));
        Bukkit.getPluginCommand("deletehome").setExecutor(new DeleteHomeCommand(playerDataManager, langData));
        Bukkit.getPluginCommand("maxhomes").setExecutor(new MaxHomesCommand(playerDataManager));
        Bukkit.getPluginCommand("balance").setExecutor(new BalanceCommand(playerDataManager));
        Bukkit.getPluginCommand("coins").setExecutor(new CoinsCommand(playerDataManager));
        Bukkit.getPluginCommand("economy").setExecutor(new EconomyCommand(economyManager));
    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerDataListener(playerDataManager, langData, rankDataManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    public int getHomeTeleportCost() {
        return this.getConfig().getInt("Settings.Home-teleport-cost");
    }
}
