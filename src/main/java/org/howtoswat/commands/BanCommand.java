package org.howtoswat.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.utils.AdminUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

public class BanCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "" + ChatColor.BOLD + "BAN" + ChatColor.DARK_GRAY + "] " + ChatColor.RED;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isAdmin(player.getUniqueId().toString())) {
                BanList banlist = Bukkit.getServer().getBanList(BanList.Type.NAME);

                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                if (args.length > 0) {
                    OfflinePlayer banned = Bukkit.getServer().getOfflinePlayer(args[0]);
                    if (banlist.isBanned(banned.getName())) {
                        banlist.pardon(banned.getName());

                        for (String uuid : AdminUtils.getAdmins()) {
                            Player admin = Bukkit.getServer().getPlayer(UUID.fromString(uuid));
                            if (admin != null) {
                                admin.sendMessage(PREFIX + ChatColor.DARK_RED + "" + ChatColor.BOLD + banned.getName() + ChatColor.RED + " wurde von " + ChatColor.DARK_RED + player.getName() + ChatColor.RED + " wieder entbannt.");;
                            }
                        }
                    } else {
                        if (args.length > 1) {
                            StringBuilder reason = new StringBuilder();
                            for (String arg : args) {
                                reason.append(" ").append(arg);
                            }
                            reason.delete(0, args[0].length() + args[1].length() + 3);
                            if (reason.length() == 0) {
                                reason.append("-");
                            }

                            Date expiration;
                            int duration = Integer.parseInt(args[1]);
                            if (duration <= 0) {
                                expiration = null;
                            } else {
                                LocalDateTime time = LocalDateTime.now().plus(duration, ChronoUnit.MINUTES);
                                expiration = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
                            }

                            banlist.addBan(banned.getName(), reason.toString(), expiration, player.getName());

                            Bukkit.getServer().broadcastMessage(PREFIX + ChatColor.DARK_RED + "" + ChatColor.BOLD + banned.getName() + ChatColor.RED + " wurde von " + ChatColor.DARK_RED + player.getName() + ChatColor.RED + " gebannt.");
                            String until;
                            if (expiration == null) {
                                until = "-";
                            } else {
                                until = format.format(expiration);
                            }
                            Bukkit.getServer().broadcastMessage(ChatColor.DARK_GRAY + "              Bis: " + ChatColor.GRAY + until);
                            Bukkit.getServer().broadcastMessage(ChatColor.DARK_GRAY + "              Grund: " + ChatColor.GRAY + reason);

                            if (banned.isOnline()) {
                                banned.getPlayer().kickPlayer("Du wurdest gebannt!\nGrund: " + reason);
                            }
                        } else {
                            player.sendMessage(PREFIX + "Gib eine Dauer in Minuten an!");
                        }
                    }
                } else {
                    player.sendMessage(PREFIX + "Gebannte Spieler:");
                    for (BanEntry entry : banlist.getBanEntries()) {
                        player.sendMessage(ChatColor.DARK_GRAY + "      -" + ChatColor.GRAY + " " + ChatColor.BOLD + entry.getTarget() + ChatColor.DARK_GRAY + ":");
                        player.sendMessage(ChatColor.DARK_GRAY + "          Grund: " + ChatColor.GRAY + entry.getReason());
                        player.sendMessage(ChatColor.DARK_GRAY + "          Admin: " + ChatColor.GRAY + entry.getSource());
                        player.sendMessage(ChatColor.DARK_GRAY + "          Von: " + ChatColor.GRAY + format.format(entry.getCreated()));
                        String until;
                        Date expiration = entry.getExpiration();
                        if (expiration == null) {
                            until = "-";
                        } else {
                            until = format.format(entry.getExpiration());
                        }
                        player.sendMessage(ChatColor.DARK_GRAY + "          Bis: " + ChatColor.GRAY + until);
                    }
                }
            } else {
                player.sendMessage(PREFIX + "Du bist kein Admin!");
            }
        }
        return true;
    }
}
