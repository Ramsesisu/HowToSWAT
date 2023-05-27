package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.HowToSWAT;
import org.howtoswat.utils.AdminUtils;
import org.howtoswat.utils.DataUtils;

import java.util.UUID;

public class AdminsCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "ADMIN" + ChatColor.DARK_GRAY + "] " + ChatColor.RED;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (player.isOp()) {
                if (args.length > 0) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    String uuid = offlinePlayer.getUniqueId().toString();

                    if (AdminUtils.isAdmin(uuid)) {
                        HowToSWAT.admins.remove(uuid);

                        Bukkit.broadcastMessage(PREFIX + ChatColor.DARK_RED + offlinePlayer.getName() + ChatColor.RED + " wurde als Admin entlassen.");
                    } else {
                        HowToSWAT.admins.add(uuid);

                        Bukkit.broadcastMessage(PREFIX + ChatColor.DARK_RED + offlinePlayer.getName() + ChatColor.RED + " wurde zum Admin ernannt.");
                    }

                    DataUtils.saveValues(HowToSWAT.adminsave, HowToSWAT.adminconfig, "admins", HowToSWAT.admins);

                    return true;
                }
            }

            player.sendMessage(PREFIX + "Aktuelle Admins:");
            for (Object uuid : AdminUtils.getAdmins()) {
                player.sendMessage(ChatColor.DARK_GRAY + "                  - " + ChatColor.GRAY + Bukkit.getOfflinePlayer(UUID.fromString(uuid.toString())).getName());
            }
        }
        return true;
    }
}
