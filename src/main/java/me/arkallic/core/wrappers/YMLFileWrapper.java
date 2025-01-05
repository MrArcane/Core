package me.arkallic.core.wrappers;

import me.arkallic.core.Core;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class YMLFileWrapper {
    private final String DIR, NAME;
    private final File file;
    private final FileConfiguration CONFIG;
    private final Core core;

    public YMLFileWrapper(String dir, String name, Core core)  {
        this.DIR = dir;
        this.NAME = name;
        this.core = core;
        this.file = new File(core.getDataFolder() + File.separator + getDir(), getName());
        CONFIG = YamlConfiguration.loadConfiguration(file);
    }

    public void saveFile() throws IOException {
        getConfig().save(file);
    }


    public File getFile() {
        return file;
    }
    public FileConfiguration getConfig() {
        return CONFIG;
    }
    public String getDir() {
        return DIR;
    }

    public @NotNull String getName() {
        return NAME;
    }
}
