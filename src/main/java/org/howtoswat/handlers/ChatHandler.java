package org.howtoswat.handlers;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatHandler implements Listener {

    @EventHandler
    public static void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(ChatColor.GRAY + "" + ChatColor.ITALIC + "CHAT" + ChatColor.DARK_GRAY + " [" + ChatColor.WHITE + ChatColor.BOLD + event.getPlayer().getName() + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " » " + ChatColor.WHITE + event.getMessage());
    }
}
