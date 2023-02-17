package me.jass.antiinterrupt;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class InterruptManager {
    private Map<Player, Player> duelists = new HashMap<Player, Player>();
    private Map<Player, Player> requests = new HashMap<Player, Player>();

    public void sendRequest(Player requester, Player receiver) {
        requests.put(requester, receiver);
        sendAlert(requester, "&7You sent a request to &r" + receiver.getName() + "&7!");
        sendAlert(receiver, "&7Hit &r" + requester.getName() + " &7back to start fighting!");
    }

    public void startDuel(Player duelistA, Player duelistB) {
        endDuel(duelistA);
        endDuel(duelistB);
        stopRequesting(duelistA);
        stopRequesting(duelistB);
        duelists.put(duelistA, duelistB);
        duelists.put(duelistB, duelistA);
        sendAlert(duelistA, "&7You are now fighting &r" + duelistB.getName() + "&7!");
        sendAlert(duelistB, "&7You are now fighting &r" + duelistA.getName() + "&7!");
    }

    public boolean isDueling(Player duelistA, Player duelistB) {
        return duelists.get(duelistA) == duelistB && duelists.get(duelistB) == duelistA;
    }

    public boolean readyToDuel(Player duelistA, Player duelistB) {
        return requests.get(duelistA) == duelistB && requests.get(duelistB) == duelistA;
    }

    public boolean isInDuel(Player player) {
        return duelists.get(player) != null;
    }

    public boolean isRequesting(Player requester, Player receiver) {
        return requests.get(requester) == receiver;
    }

    public void blockInterruption(Player player) {
        sendAlert(player, "&7Don't Interrupt!");
    }

    public void stopRequesting(Player player) {
        requests.remove(player);
    }

    public void endDuel(Player player) {
        duelists.remove(duelists.get(player));
        duelists.remove(player);
    }

    public void sendAlert(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
    }
}
