package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MsgCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "" + ChatColor.BOLD + "MSG" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (args.length > 0) {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target != null) {
                    if (args.length > 1) {
                        StringBuilder message = new StringBuilder();
                        for (String arg : args) {
                            message.append(" ").append(arg);
                        }
                        message.delete(0, args[0].length() + 1);

                        player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "MSG" + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + ChatColor.BOLD + player.getName() + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " >" + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + ChatColor.BOLD + target.getName() + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_GRAY + " »" + ChatColor.GRAY + message);
                        target.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "MSG" + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + ChatColor.BOLD + target.getName() + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " <" + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + ChatColor.BOLD + player.getName() + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_GRAY + " »" + ChatColor.GRAY + message);
                    } else {
                        player.sendMessage(PREFIX + "Gib eine Nachricht an!");
                    }
                } else {
                    player.sendMessage(PREFIX + "Der Spieler " + ChatColor.GRAY + args[0] + ChatColor.WHITE + " wurde nicht gefunden!");
                }
            } else {
                player.sendMessage(PREFIX + "Gib einen Spieler an!");
            }
        }
        return true;
    }
}
