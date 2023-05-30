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

public class SupporterCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "" + ChatColor.BOLD + "SUPPORTER" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isAdmin(player.getUniqueId().toString())) {
                if (args.length > 0) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    String uuid = offlinePlayer.getUniqueId().toString();

                    if (AdminUtils.isSupporter(uuid)) {
                        HowToSWAT.supporter.remove(uuid);

                        Bukkit.broadcastMessage(PREFIX + ChatColor.YELLOW + offlinePlayer.getName() + ChatColor.GOLD + " wurde als Supporter entlassen.");
                    } else {
                        HowToSWAT.supporter.add(uuid);

                        Bukkit.broadcastMessage(PREFIX + ChatColor.YELLOW + offlinePlayer.getName() + ChatColor.GOLD + " wurde zum Supporter ernannt.");
                    }

                    DataUtils.saveValues(HowToSWAT.supportersave, HowToSWAT.supporterconfig, "supporter", HowToSWAT.supporter);
                    return true;
                }
            }

            player.sendMessage(PREFIX + "Aktuelle Supporter:");
            for (Object uuid : AdminUtils.getSupporter()) {
                player.sendMessage(ChatColor.DARK_GRAY + "                  - " + ChatColor.GRAY + Bukkit.getOfflinePlayer(UUID.fromString(uuid.toString())).getName());
            }
        }
        return true;
    }
}
