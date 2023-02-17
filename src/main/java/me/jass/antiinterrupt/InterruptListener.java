package me.jass.antiinterrupt;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InterruptListener implements Listener {
    InterruptManager manager = InterruptAPI.INSTANCE.getManager();
    InterruptExpansion expansion = InterruptAPI.INSTANCE.getExpansion();

    @EventHandler
    public void onInterrupt(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) {
            return;
        }

        Player requester = (Player) event.getDamager();
        Player receiver = (Player) event.getEntity();

        if (expansion != null && !expansion.isInRegion(receiver)) {
            return;
        }

        if (!manager.isDueling(requester, receiver)) {
            if (!manager.isInDuel(receiver) || manager.isRequesting(receiver, requester)) {
                manager.sendRequest(requester, receiver);

                if (manager.readyToDuel(requester, receiver)) {
                    manager.startDuel(requester, receiver);
                }
            } else {
                manager.blockInterruption(requester);
            }
            event.setCancelled(true);
        } else {
            manager.stopRequesting(requester);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        manager.endDuel(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        manager.endDuel(event.getPlayer());
        manager.stopRequesting(event.getPlayer());
    }
}