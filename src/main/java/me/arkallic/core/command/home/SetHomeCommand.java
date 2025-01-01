package me.arkallic.core.command.home;

import me.arkallic.core.handler.LangHandler;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.model.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.arkallic.core.handler.MessageHandler.send;

public class SetHomeCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;
    private final LangHandler langHandler;

    public SetHomeCommand(PlayerDataManager playerDataManager, LangHandler langHandler) {
        this.playerDataManager = playerDataManager;
        this.langHandler = langHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (sender instanceof Player p) {
            int currentHomes = playerDataManager.getHomesSection(p.getUniqueId()).getKeys(false).size();
            int maxHomes = playerDataManager.getMaxHomes(p.getUniqueId());

            if (args.length == 0) {
                if (playerDataManager.hasHomes(p.getUniqueId())) {
                    send(p, "&cUSAGE: &7/sethome [&4NAME&7]");
                    return true;
                }

                //If the player doesn't have any homes and didn't set another arg make the default home
                playerDataManager.setHome(p.getUniqueId(), "home", p.getLocation());
                send(p,  langHandler.HOME_SET.replace("%HOME%", "home"));
                return true;
            }
            String input = args[0].toLowerCase();

            if (currentHomes == maxHomes) {
                if (!playerDataManager.homeExists(p.getUniqueId(), input)) {
                    send(p,  langHandler.HOME_LIMIT);
                    return true;
                }
                playerDataManager.setHome(p.getUniqueId(), input, p.getLocation());
                send(p,  langHandler.HOME_MODIFIED.replace("%HOME%", input));
                return true;
            }

            if (playerDataManager.homeExists(p.getUniqueId(), input)) {
                playerDataManager.setHome(p.getUniqueId(), input, p.getLocation());
                send(p,  langHandler.HOME_MODIFIED.replace("%HOME%", input));
                return true;
            }

            playerDataManager.setHome(p.getUniqueId(), input, p.getLocation());
            send(p,  langHandler.HOME_SET.replace("%HOME%", input));
            return true;
        }
        return false;
    }
}
