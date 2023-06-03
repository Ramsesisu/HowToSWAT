package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.utils.AdminUtils;

public class TeleportCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "TELEPORT" + ChatColor.DARK_GRAY + "] " + ChatColor.YELLOW;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isSupporter(player.getUniqueId().toString())) {
                if (args.length == 0) {
                    player.sendMessage(PREFIX + "Du hast keinen Spieler angegeben!");
                } else if (args.length == 1) {
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    if (target != null) {
                        player.teleport(target);

                        player.sendMessage(PREFIX + "Du hast dich zu " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " teleportiert.");
                    } else {
                        player.sendMessage(PREFIX + "Der Spieler " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " wurde nicht gefunden!");
                    }
                } else if (args.length == 2) {
                    Player to = Bukkit.getServer().getPlayer(args[1].replace("@s", player.getName()));
                    if (to != null) {
                        if (args[0].equalsIgnoreCase("@a")) {
                            for (Player from : Bukkit.getServer().getOnlinePlayers()) {
                                from.teleport(to);
                            }

                            player.sendMessage(PREFIX + "Alle Spieler wurden zu " + ChatColor.GOLD + to.getName() + ChatColor.YELLOW + " teleportiert.");
                        } else {
                            Player from = Bukkit.getServer().getPlayer(args[0].replace("@s", player.getName()));
                            if (from != null) {
                                from.teleport(to);

                                player.sendMessage(PREFIX + ChatColor.GOLD + from.getName() + ChatColor.YELLOW + " wurde zu " + ChatColor.GOLD + to.getName() + ChatColor.YELLOW + " teleportiert.");
                            } else {
                                player.sendMessage(PREFIX + "Der Spieler " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " wurde nicht gefunden!");
                            }
                        }
                    } else {
                        player.sendMessage(PREFIX + "Der Spieler " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " wurde nicht gefunden!");
                    }
                } else if (args.length > 3) {
                    Player from = Bukkit.getServer().getPlayer(args[0].replace("@s", player.getName()));
                    if (from != null) {
                        double x = getCoord(player.getLocation().getX(), args[1]);
                        double y = getCoord(player.getLocation().getY(), args[2]);
                        double z = getCoord(player.getLocation().getZ(), args[3]);
                        from.teleport(new Location(from.getWorld(), x, y, z));

                        player.sendMessage(PREFIX + ChatColor.GOLD + from.getName() + ChatColor.YELLOW + " wurde zu " + ChatColor.GOLD + (int) x + ChatColor.YELLOW + ", " + ChatColor.GOLD + (int) y + ChatColor.YELLOW + ", " + ChatColor.GOLD + (int) z + ChatColor.YELLOW + " teleportiert.");
                    } else {
                        player.sendMessage(PREFIX + "Der Spieler " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " wurde nicht gefunden!");
                    }
                } else {
                    player.sendMessage(PREFIX + "Gib vollständige Koordinaten an!");
                }
            } else {
                player.sendMessage(PREFIX + "Du bist kein Supporter!");
            }
        }
        return true;
    }

    private static double getCoord(double loc, String arg) {
        String string = arg.replace("~", "");
        if (string.isEmpty()) {
            string = "0";
        }
        double coord = Double.parseDouble(string);
        if (arg.startsWith("~")) {
            coord = loc + coord;
        }
        return coord;
    }
}
