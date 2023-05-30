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

public class BuilderCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "BUILDER" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isAdmin(player.getUniqueId().toString())) {
                if (args.length > 0) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    String uuid = offlinePlayer.getUniqueId().toString();

                    if (AdminUtils.isBuilder(uuid)) {
                        HowToSWAT.builder.remove(uuid);

                        Bukkit.broadcastMessage(PREFIX + ChatColor.DARK_GREEN + offlinePlayer.getName() + ChatColor.GREEN + " wurde als Builder entlassen.");
                    } else {
                        HowToSWAT.builder.add(uuid);

                        Bukkit.broadcastMessage(PREFIX + ChatColor.DARK_GREEN + offlinePlayer.getName() + ChatColor.GREEN + " wurde zum Builder ernannt.");
                    }

                    DataUtils.saveValues(HowToSWAT.buildersave, HowToSWAT.builderconfig, "builder", HowToSWAT.builder);
                    return true;
                }
            }

            player.sendMessage(PREFIX + "Aktuelle Builder:");
            for (Object uuid : AdminUtils.getBuilder()) {
                player.sendMessage(ChatColor.DARK_GRAY + "                  - " + ChatColor.GRAY + Bukkit.getOfflinePlayer(UUID.fromString(uuid.toString())).getName());
            }
        }
        return true;
    }
}
