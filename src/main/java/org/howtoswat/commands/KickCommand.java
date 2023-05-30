package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.utils.AdminUtils;

public class KickCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "" + ChatColor.BOLD + "KICK" + ChatColor.DARK_GRAY + "] " + ChatColor.RED;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isSupporter(player.getUniqueId().toString())) {
                if (args.length > 0) {
                    Player kicked = Bukkit.getServer().getPlayer(args[0]);
                    if (kicked != null) {
                        StringBuilder reason = new StringBuilder();
                        for (String arg : args) {
                            reason.append(" ").append(arg);
                        }
                        reason.delete(0, args[0].length() + 2);
                        if (reason.length() == 0) {
                            reason.append("-");
                        }

                        Bukkit.getServer().broadcastMessage(PREFIX + ChatColor.DARK_RED + "" + ChatColor.BOLD + kicked.getName() + ChatColor.RED + " wurde von " + ChatColor.DARK_RED + player.getName() + ChatColor.RED + " gekickt.");
                        Bukkit.getServer().broadcastMessage(ChatColor.DARK_GRAY + "              Grund: " + ChatColor.GRAY + reason);

                        kicked.kickPlayer("Du wurdest gekickt!\nGrund: " + reason);
                    }
                }
            } else {
                player.sendMessage(PREFIX + "Du bist kein Supporter!");
            }
        }
        return true;
    }
}
