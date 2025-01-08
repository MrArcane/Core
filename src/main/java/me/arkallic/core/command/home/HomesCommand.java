package me.arkallic.core.command.home;

import me.arkallic.core.data.LangData;
import me.arkallic.core.data.PlayerData;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.model.Home;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.arkallic.core.util.MessageUtil.*;

public class HomesCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;
    private final LangData langData;

    public HomesCommand(PlayerDataManager playerDataManager, LangData langData) {
        this.playerDataManager = playerDataManager;
        this.langData = langData;
    }
    /**
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            log(ONLY_PLAYERS_COMMAND);
            return true;
        }

        if (!player.hasPermission("core.homes")) {
            log(NO_PERMISSION_COMMAND);
            return true;
        }

        PlayerData pd = playerDataManager.getPlayerData(player.getUniqueId());

        if (pd.getHomes().isEmpty()) {
            send(sender, langData.noHomes);
            return true;
        }

        String msgHeader = String.format("%s&7[&aHomes&7] &7(&f%s&7/&f%s&7)",
                " ".repeat(28),
                pd.getCurrentHomes(),
                pd.getMaxHomes());

        send(sender, msgHeader);

        send(sender, "&8&m ".repeat(70));

        for (String s : pd.getHomes().keySet()) {
            Home home = pd.getHome(s);
            String homeName = home.getName();
            Location loc = home.getLocation();
            int x = (int) loc.getX();
            int y = (int) loc.getY();
            int z = (int) loc.getZ();
            String worldName = loc.getWorld().getName();

            String msg = String.format(
                    "&8- &f%s &7[&9%s&7] &8(&7X: &b%s&7, &7Y: &b%s&7, &7Z: &b%s&7) &7[&f%s&7]",
                    homeName,
                    worldName,
                    x, y, z,
                    worldName
            );
            send(sender, msg);
        }
        send(sender, "&8&m ".repeat(70));

        return false;
    }
}
