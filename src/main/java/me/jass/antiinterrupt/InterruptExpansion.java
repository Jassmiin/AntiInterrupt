package me.jass.antiinterrupt;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;

public class InterruptExpansion {
    StateFlag INTERRUPT;

    public InterruptExpansion() {
        final FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        if (registry.get("interrupt") == null) {
            final StateFlag flag = new StateFlag("interrupt", false);
            registry.register(flag);
            INTERRUPT = flag;
        } else {
            final Flag<?> existing = registry.get("interrupt");
            if (existing instanceof StateFlag) {
                INTERRUPT = (StateFlag) existing;
            }
        }
    }

    public boolean isInRegion(Player player) {
        Location loc = BukkitAdapter.adapt(player.getLocation());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet regions = query.getApplicableRegions(loc);

        for (ProtectedRegion region : regions) {
            StateFlag.State state = region.getFlag(INTERRUPT);
            if (state != null && state.name() == "DENY") {
                return true;
            }
        }
        return false;
    }
}
