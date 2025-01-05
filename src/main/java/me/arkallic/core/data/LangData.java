package me.arkallic.core.data;


import me.arkallic.core.Core;
import me.arkallic.core.wrappers.YMLFileWrapper;

public class LangData extends YMLFileWrapper {
    private final Core core;
    public String homeSet, homeModified, invalidHome, noHomes, homeLimit, teleportHome, homeDeleted, welcomeBack, newPlayer;

    public LangData(Core core) {
        super("", "lang.yml", core);
        this.core = core;

        //Set messages
        this.homeSet = this.getConfig().getString("home_set");
        this.homeModified = this.getConfig().getString("home_modified");
        this.invalidHome = this.getConfig().getString("invalid_home");
        this.noHomes = this.getConfig().getString("no_homes");
        this.homeLimit = this.getConfig().getString("home_limit");
        this.teleportHome = this.getConfig().getString("teleport_home");
        this.homeDeleted = this.getConfig().getString("home_deleted");
        this.welcomeBack = this.getConfig().getString("welcome_back");
        this.newPlayer = this.getConfig().getString("new_player");
    }

    public void initialize() {
        if (!this.getFile().exists()) {
            core.saveResource("lang.yml", false);
        }
    }




}
