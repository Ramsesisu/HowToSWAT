package org.howtoswat.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.howtoswat.commands.DisableCommandCommand;
import org.howtoswat.commands.DutyCommand;
import org.howtoswat.utils.AdminUtils;

import java.util.UUID;

public class CommandHandler implements Listener {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "" + ChatColor.BOLD + "CMD" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_GRAY;

    @EventHandler
    public static void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        String message = event.getMessage();
        for (Command cmd : DisableCommandCommand.disabled) {
            if (message.startsWith("/" + cmd.getName()) || message.contains(":" + cmd.getName())) {
                player.sendMessage(DisableCommandCommand.PREFIX + "Der Befehl " + ChatColor.AQUA + "/" + cmd.getName() + ChatColor.BLUE + " ist deaktiviert!");

                if (!AdminUtils.isAdmin(player.getUniqueId().toString())) {
                    event.setCancelled(true);
                }
            }
        }

        for (UUID uuid : DutyCommand.duty) {
            Player admin = Bukkit.getServer().getPlayer(uuid);
            if (admin != null) {
                admin.sendMessage(PREFIX + ChatColor.GRAY + "" + ChatColor.BOLD + player.getName() + ChatColor.DARK_GRAY + " > " + ChatColor.GRAY + "" + ChatColor.ITALIC + message);
            }
        }
    }
}
