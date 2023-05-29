package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.utils.AdminUtils;

public class RunCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "RUN" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isAdmin(player.getUniqueId().toString())) {
                if (args.length > 0) {
                    if (args.length > 1) {
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        if (target != null) {
                            StringBuilder message = new StringBuilder();
                            for (String arg : args) {
                                message.append(" ").append(arg);
                            }
                            message.delete(0, args[0].length() + 2);
                            if (message.length() == 0) {
                                message.append("-");
                            }
                            String msg = message.toString();
                            if (!message.toString().startsWith("/")) msg = "/" + message;

                            player.sendMessage(PREFIX + ChatColor.BOLD + target.getName() + ChatColor.GRAY + " führt nun: " + ChatColor.GREEN + msg + ChatColor.GRAY + " aus.");

                            target.chat(msg);
                        }
                    } else {
                        player.sendMessage(PREFIX + "Du musst einen Befehl angeben!");
                    }
                } else {
                    player.sendMessage(PREFIX + "Gib einen Spieler an!");
                }
            } else {
                player.sendMessage(PREFIX + "Du bist kein Admin!");
            }
        }
        return true;
    }
}
