package org.howtoswat.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.howtoswat.commands.DutyCommand;

import java.util.UUID;

public class CommandHandler implements Listener {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "" + ChatColor.BOLD + "CMD" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_GRAY;

    @EventHandler
    public static void onCommand(PlayerCommandPreprocessEvent event) {
        for (UUID uuid : DutyCommand.duty) {
            Player admin = Bukkit.getServer().getPlayer(uuid);
            if (admin != null) {
                admin.sendMessage(PREFIX + ChatColor.GRAY + "" + ChatColor.BOLD + event.getPlayer().getName() + ChatColor.DARK_GRAY + " > " + ChatColor.GRAY + "" + ChatColor.ITALIC + event.getMessage());
            }
        }
    }
}
