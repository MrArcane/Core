package me.arkallic.core.command.home;

import me.arkallic.core.data.PlayerData;
import me.arkallic.core.data.LangData;
import me.arkallic.core.manager.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static me.arkallic.core.util.MessageUtil.send;

public class SetHomeCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;
    private final LangData langData;

    public SetHomeCommand(PlayerDataManager playerDataManager, LangData langData) {
        this.playerDataManager = playerDataManager;
        this.langData = langData;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (sender instanceof Player p) {

            UUID uuid = p.getUniqueId();
            PlayerData pd = playerDataManager.getPlayerData(uuid);
            int currentHomes = pd.getCurrentHomes();
            int maxHomes = pd.getMaxHomes();

            if (args.length == 0) {
                if (pd.getCurrentHomes() >= 1) {
                    send(p, "&cUSAGE: &7/sethome [&4NAME&7]");
                    return true;
                }

                //If the player doesn't have any homes and didn't set another arg make the default home
                pd.setHome("home", p.getLocation());
                send(p,  langData.homeSet.replace("%HOME%", "home"));
                return true;
            }
            String input = args[0].toLowerCase();

            if (currentHomes == maxHomes) {
                if (!pd.homeExists(input)) {
                    send(p,  langData.homeLimit);
                    return true;
                }
                pd.setHome(input, p.getLocation());
                send(p,  langData.homeModified.replace("%HOME%", input));
                return true;
            }

            if (pd.homeExists(input)) {
                pd.setHome(input, p.getLocation());
                send(p,  langData.homeModified.replace("%HOME%", input));
                return true;
            }

            pd.setHome(input, p.getLocation());
            send(p,  langData.homeSet.replace("%HOME%", input));
            return true;
        }
        return false;
    }
}
