package me.arkallic.core.command;

import me.arkallic.core.Core;
import org.bukkit.command.CommandExecutor;

public class Commands {
    private final Core core;

    public Commands(Core core) {
        this.core = core;
    }

    public void register(String command, CommandExecutor commandExecutor) {
        core.getCommand(command).setExecutor(commandExecutor);
    }
}
