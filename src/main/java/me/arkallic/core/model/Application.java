package me.arkallic.core.model;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Application {

    private final ItemStack book;
    private final UUID uuid;

    public Application(UUID uuid, ItemStack book) {
        this.uuid = uuid;
        this.book = book;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public ItemStack getBook() {
        return book;
    }
}
