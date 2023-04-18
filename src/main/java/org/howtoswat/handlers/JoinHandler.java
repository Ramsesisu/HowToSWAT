package org.howtoswat.handlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinHandler implements Listener {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "JOIN" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD;

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(PREFIX + player.getName() + ChatColor.YELLOW + " ist nun " + ChatColor.GREEN + "online" + ChatColor.YELLOW + ".");
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(PREFIX + player.getName() + ChatColor.YELLOW + " ist nun " + ChatColor.RED + "offline" + ChatColor.YELLOW + ".");
    }
}
