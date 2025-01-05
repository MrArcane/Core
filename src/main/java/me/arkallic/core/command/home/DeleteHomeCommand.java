package me.arkallic.core.command.home;

import me.arkallic.core.data.PlayerData;
import me.arkallic.core.data.LangData;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.model.Home;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static me.arkallic.core.util.MessageUtil.send;

public class DeleteHomeCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;
    private final LangData langData;

    public DeleteHomeCommand(PlayerDataManager playerDataManager, LangData langData) {
        this.playerDataManager = playerDataManager;
        this.langData = langData;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player p) {
            UUID uuid = p.getUniqueId();
            PlayerData pd = playerDataManager.getPlayerData(uuid);

            if (pd.getHomes().isEmpty()) {
                send(p,  langData.noHomes);
                return true;
            }

            if (args.length == 0) {
                send(p, "&cUSAGE: &7/deletehome [NAME]");
                return true;
            }

            String input = args[0].toLowerCase();
            if (!pd.getHomes().containsKey(input)) {
                send(p,  langData.invalidHome);
                return true;
            }

            Home home = pd.getHome(input);
            pd.deleteHome(home);
            send(p,  langData.homeDeleted.replace("%HOME%", input));
            return true;
        }
        return false;
    }
}


