package me.jass.antiinterrupt;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.logging.log4j.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class InterruptListener implements Listener {
    private Map<Player, Player> duelists = new HashMap<Player, Player>();
    private Map<Player, Player> requests = new HashMap<Player, Player>();

    @EventHandler
    public void onInterrupt(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        //Damager isn't dueling victim
        if (duelists.get(damager) != victim) {
            //Accept request
            if (requests.get(victim) == damager) {
                duelists.remove(duelists.get(damager));
                duelists.remove(duelists.get(victim));
                duelists.put(damager, victim);
                duelists.put(victim, damager);
                requests.remove(damager);
                requests.remove(victim);
            } else {
                //Send request
                if (duelists.get(victim) == null) {
                    //Victim wishes to change opponent
                    if (requests.get(victim) == damager) {
                        duelists.remove(duelists.get(damager));
                        duelists.put(damager, victim);
                        duelists.put(victim, damager);
                        requests.remove(damager);
                        requests.remove(victim);
                    } else {
                        //Ask victim to duel
                        requests.put(damager, victim);
                        victim.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GRAY + "Hit " + ChatColor.WHITE + damager.getName() + ChatColor.GRAY +  " back to start fighting!"));

                        //Requests match
                        if (requests.get(damager) == victim && requests.get(victim) == damager) {
                            //Start duel
                            duelists.put(damager, victim);
                            duelists.put(victim, damager);
                            requests.remove(damager);
                            requests.remove(victim);
                        }
                    }
                } else {
                    //Interrupting
                    damager.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GRAY + "Don't Interrupt!"));
                }
            }
            event.setCancelled(true);
        } else {
            requests.remove(damager);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Entity killer = event.getPlayer().getKiller();
        if (killer instanceof Player) {
            duelists.remove((Player) killer);
        }

        duelists.remove(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        duelists.remove(event.getPlayer());
        requests.remove(event.getPlayer());
    }
}