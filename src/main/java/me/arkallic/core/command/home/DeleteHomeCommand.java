package me.arkallic.core.command.home;

import me.arkallic.core.handler.LangHandler;
import me.arkallic.core.manager.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.arkallic.core.util.MessageUtil.send;

public class DeleteHomeCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;
    private final LangHandler langHandler;

    public DeleteHomeCommand(PlayerDataManager playerDataManager, LangHandler langHandler) {
        this.playerDataManager = playerDataManager;
        this.langHandler = langHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player p) {
            if (!playerDataManager.hasHomes(p.getUniqueId())) {
                send(p,  langHandler.NO_HOMES);
                return true;
            }

            if (args.length == 0) {
                send(p, "&cUSAGE: &7/deletehome [NAME]");
                return true;
            }

            String input = args[0].toLowerCase();
            if (!playerDataManager.homeExists(p.getUniqueId(), input)) {
                send(p,  langHandler.INVALID_HOME);
                return true;
            }

            playerDataManager.deleteHome(p.getUniqueId(), input);
            send(p,  langHandler.HOME_DELETED.replace("%HOME%", input));
            return true;
        }
        return false;
    }
}


