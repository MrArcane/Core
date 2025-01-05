package me.arkallic.core.wrappers;

import me.arkallic.core.Core;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class YMLFileWrapper {
    private final File file;
    private final FileConfiguration config;

    public YMLFileWrapper(String dir, String name, Core core)  {
        this.file = new File(core.getDataFolder() + File.separator + dir, name);

        File directory = file.getParentFile();

        if (!directory.exists()) {
            directory.mkdirs();
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() throws IOException {
        getConfig().save(file);
    }


    public File getFile() {
        return file;
    }
    public FileConfiguration getConfig() {
        return config;
    }

    public File getDirectory() {
        return file.getParentFile();
    }

    public String getName() {
        return file.getName();
    }
}
