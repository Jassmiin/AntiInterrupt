package me.jass.antiinterrupt;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
@Getter
public enum InterruptAPI {
    INSTANCE;

    private JavaPlugin plugin;
    private InterruptManager manager = new InterruptManager();
    private InterruptExpansion expansion;
    public boolean worldGuard = true;

    public void load(final JavaPlugin plugin) {
        assert plugin != null : "Error while loading AntiInterrupt";
        this.plugin = plugin;

        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            expansion = new InterruptExpansion();
        }
    }

    public void start(final JavaPlugin plugin) {
        assert plugin != null : "Error while starting AntiInterrupt";
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(new InterruptListener(), plugin);
    }

    public void stop(final JavaPlugin plugin) {
        assert plugin != null : "Error while stopping AntiInterrupt";
        this.plugin = plugin;
    }
}