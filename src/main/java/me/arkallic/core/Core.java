package me.arkallic.core;

import me.arkallic.core.command.*;
import me.arkallic.core.command.home.*;
import me.arkallic.core.data.RankData;
import me.arkallic.core.handler.LangHandler;
import me.arkallic.core.listener.*;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.manager.RankDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public final class Core extends JavaPlugin {

    private final RankData rankData = new RankData(this);
    private final RankDataManager rankDataManager = new RankDataManager(rankData);
    public final PlayerDataManager playerDataManager = new PlayerDataManager(rankDataManager, this);
    private final LangHandler langHandler = new LangHandler(this);

    @Override
    public void onEnable() {
        rankData.initialize();
        rankDataManager.initialize();
        langHandler.initialize();

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
        Bukkit.getPluginCommand("home").setExecutor(new HomeCommand(playerDataManager, langHandler));
        Bukkit.getPluginCommand("sethome").setExecutor(new SetHomeCommand(playerDataManager, langHandler));
        Bukkit.getPluginCommand("deletehome").setExecutor(new DeleteHomeCommand(playerDataManager, langHandler));
        Bukkit.getPluginCommand("maxhomes").setExecutor(new MaxHomesCommand(playerDataManager));
    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerDataListener(playerDataManager, langHandler, rankDataManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }
}
