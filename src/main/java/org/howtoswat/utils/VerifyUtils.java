package org.howtoswat.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.howtoswat.commands.VerifyCommand;

import java.util.HashMap;
import java.util.UUID;

import static org.howtoswat.HowToSWAT.PLUGIN;

public class VerifyUtils {
    public static final String PREFIX = org.bukkit.ChatColor.DARK_GRAY + "[" + org.bukkit.ChatColor.DARK_AQUA + "" + org.bukkit.ChatColor.BOLD + "REQUEST" + org.bukkit.ChatColor.DARK_GRAY + "] " + org.bukkit.ChatColor.GRAY;
    public static HashMap<HashMap<UUID, UUID>, Boolean> requests = new HashMap<>();

    public static void addRequest(Player fromPlayer, Player toPlayer, String message, Runnable runnable) {
        HashMap<UUID, UUID> request = new HashMap<>();
        request.put(fromPlayer.getUniqueId(), toPlayer.getUniqueId());
        requests.put(request, false);
        Bukkit.getScheduler().runTaskLater(PLUGIN, () -> requests.remove(request), 60 * 20L);
        toPlayer.sendMessage(message);
        toPlayer.sendMessage(getAcceptMessage());
        toPlayer.sendMessage(getDeclineMessage());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (requests.containsKey(request)) {
                    if (requests.get(request)) {
                        runnable.run();
                        requests.remove(request);
                        cancel();
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(PLUGIN, 0L, 20L);
    }

    public static void accept(Player player) {
        for (HashMap<UUID, UUID> request : requests.keySet()) {
            if (request.containsValue(player.getUniqueId())) {
                requests.put(request, true);

                player.sendMessage(PREFIX + "Du hast die Anfrage " + ChatColor.GREEN + "angenommen" + ChatColor.GRAY + ".");
                return;
            }
        }
    }

    public static void decline(Player player) {
        for (HashMap<UUID, UUID> request : requests.keySet()) {
            if (request.containsValue(player.getUniqueId())) {
                requests.remove(request);

                player.sendMessage(PREFIX + "Du hast die Anfrage " + ChatColor.RED + "abgelehnt" + ChatColor.GRAY + ".");
                return;
            }
        }
    }

    public static void verifyMessage(Player player) {
        if (!AdminUtils.isVerified(player.getUniqueId().toString())) {
            player.sendMessage(VerifyCommand.PREFIX + "Du bist noch nicht verifiziert. Beantrage deinen Verify bei einem Admin:");
            player.sendMessage(getVerifyMessage());
        }
    }

    private static TextComponent getAcceptMessage() {
        TextComponent message = new TextComponent (ChatColor.GRAY + "          » " + ChatColor.GREEN + "Annehmen");
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.GREEN + "/annehmen")));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/annehmen"));
        return message;
    }

    private static TextComponent getDeclineMessage() {
        TextComponent message = new TextComponent (ChatColor.GRAY + "          » " + ChatColor.RED + "Ablehnen");
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.RED + "/ablehnen")));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ablehnen"));
        return message;
    }

    private static TextComponent getVerifyMessage() {
        TextComponent message = new TextComponent (net.md_5.bungee.api.ChatColor.GRAY + "          » " + ChatColor.RED + "Verify beantragen");
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(net.md_5.bungee.api.ChatColor.AQUA + "/requestverify")));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/requestverify"));
        return message;
    }
}
