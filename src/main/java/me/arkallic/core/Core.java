package me.arkallic.core;

import me.arkallic.core.command.*;
import me.arkallic.core.command.home.*;
import me.arkallic.core.file.ApplicationsFile;
import me.arkallic.core.handler.LangHandler;
import me.arkallic.core.listener.*;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.manager.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public final class Core extends JavaPlugin {

    private final RankManager rankManager = new RankManager(this);
    private final ApplicationsFile applicationsFile = new ApplicationsFile(this);
    public final PlayerDataManager playerDataManager = new PlayerDataManager(rankManager, this);
    private final LangHandler langHandler = new LangHandler(this);
    private final Listeners event = new Listeners(this);
    private final Commands cmd = new Commands(this);

    @Override
    public void onEnable() {
        rankManager.initialize();
        applicationsFile.initialize();
        langHandler.initialize();

        //Commands
        cmd.register("core", new CoreCommand(playerDataManager));
        cmd.register("rank", new RankCommand(rankManager, playerDataManager));
        cmd.register("setrank", new SetRankCommand(rankManager, playerDataManager));
        cmd.register("home", new HomeCommand(playerDataManager, langHandler));
        cmd.register("sethome", new SetHomeCommand(playerDataManager, langHandler));
        cmd.register("deletehome", new DeleteHomeCommand(playerDataManager, langHandler));
        cmd.register("maxhomes", new MaxHomesCommand(playerDataManager));

        //Listeners
        event.register(new PlayerDataListener(playerDataManager, langHandler, rankManager, this));
        event.register(new PlayerJoinListener());

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
}
