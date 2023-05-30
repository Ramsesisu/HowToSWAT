package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.utils.AdminUtils;

import java.util.UUID;

public class AdminChatCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "" + ChatColor.BOLD + "ADMIN" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isSupporter(player.getUniqueId().toString())) {
                if (args.length > 0) {
                    StringBuilder message = new StringBuilder();
                    for (String arg : args) {
                        message.append(" ").append(arg);
                    }

                    for (String uuid : AdminUtils.getSupporter()) {
                        Player supporter = Bukkit.getServer().getPlayer(UUID.fromString(uuid));
                        if (supporter != null) {
                            supporter.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "ADMIN" + ChatColor.DARK_GRAY + " [" + ChatColor.DARK_RED + ChatColor.BOLD + player.getName() + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " »" + ChatColor.WHITE + message);
                        }
                    }
                } else {
                    player.sendMessage(PREFIX + "Gib eine Nachricht an!");
                }
            } else {
                player.sendMessage(PREFIX + "Du bist kein Supporter!");
            }
        }
        return true;
    }
}
