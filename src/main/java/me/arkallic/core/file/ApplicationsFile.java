package me.arkallic.core.file;

import me.arkallic.core.Core;
import me.arkallic.core.handler.FileHandler;
import me.arkallic.core.model.Application;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApplicationsFile {

    private final Core core;
    private final FileHandler fileHandler;

    public ApplicationsFile(Core core) {
        this.core = core;
        this.fileHandler = new FileHandler("", "applications.yml", core);
    }

    public void initialize() {
        if (!fileHandler.getFile().exists()) {
            core.saveResource("applications.yml", false);
        }
    }

    public void set(Application application) {
        this.getConfig().set(application.getUUID().toString(), application.getBook());
    }

    public void remove(Application application) {
        this.getConfig().set(application.getUUID().toString(), null);
    }

    public List<String> getQuestions() {
        return this.core.getConfig().getStringList("Applications.Questions");
    }


    public ItemStack getBook(UUID uuid) {
        return this.fileHandler.getConfig().getItemStack(uuid.toString());
    }

    public List<String> getApplications() {
        return new ArrayList<>(this.getConfig().getKeys(false));
    }

    private FileConfiguration getConfig() {
        return this.fileHandler.getConfig();
    }

    public void save() {
        try {
            this.fileHandler.saveFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
