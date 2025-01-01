package me.arkallic.core.listener;

import me.arkallic.core.Core;
import org.bukkit.event.Listener;
import org.checkerframework.checker.units.qual.C;

public class Listeners {

    private final Core core;
    public Listeners(Core core) {
        this.core = core;
    }
    public void register(Listener listener) {
        core.getServer().getPluginManager().registerEvents(listener, core);
    }

}
