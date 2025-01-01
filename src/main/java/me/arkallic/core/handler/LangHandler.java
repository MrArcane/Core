package me.arkallic.core.handler;


import me.arkallic.core.Core;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class LangHandler {
    private final FileHandler fileHandler;
    private final Core core;
    public String HOME_SET, HOME_MODIFIED, INVALID_HOME, NO_HOMES, HOME_LIMIT, TELEPORT_HOME, HOME_DELETED, WELCOME_BACK, NEW_PLAYER;

    public LangHandler(Core core) {
        this.core = core;
        this.fileHandler = new FileHandler("", "lang.yml", core);

        //Set messages
        this.HOME_SET = getString("HOME_SET");
        this.HOME_MODIFIED = getString("HOME_MODIFIED");
        this.INVALID_HOME = getString("INVALID_HOME");
        this.NO_HOMES = getString("NO_HOMES");
        this.HOME_LIMIT = getString("HOME_LIMIT");
        this.TELEPORT_HOME = getString("TELEPORT_HOME");
        this.HOME_DELETED = getString("HOME_DELETED");
        this.WELCOME_BACK = getString("WELCOME_BACK");
        this.NEW_PLAYER = getString("NEW_PLAYER");
    }

    public void initialize() {
        if (!fileHandler.getFile().exists()) {
            core.saveResource("lang.yml", false);
        }
    }

    public List<String> getList(String path) {
        return this.fileHandler.getConfig().getStringList(path);
    }

    public String getString(String path) {
        return this.fileHandler.getConfig().getString(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return this.fileHandler.getConfig().getConfigurationSection(path);
    }


}
