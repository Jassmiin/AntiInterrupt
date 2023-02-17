package me.jass.antiinterrupt;

import org.bukkit.plugin.java.JavaPlugin;

public final class AntiInterrupt extends JavaPlugin {
    @Override
    public void onLoad() {
        InterruptAPI.INSTANCE.load(this);
    }

    @Override
    public void onEnable() {
        InterruptAPI.INSTANCE.start(this);
    }

    @Override
    public void onDisable() {
        InterruptAPI.INSTANCE.stop(this);
    }
}