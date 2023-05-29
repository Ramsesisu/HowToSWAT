package org.howtoswat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.utils.AdminUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DutyCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "DUTY" + ChatColor.DARK_GRAY + "] " + ChatColor.RED;
    public static final List<UUID> duty = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isAdmin(player.getUniqueId().toString())) {
                if (duty.contains(player.getUniqueId())) {
                    duty.remove(player.getUniqueId());

                    player.sendMessage(PREFIX + "Admin-Modus wurde " + ChatColor.DARK_RED + "deaktiviert" + ChatColor.RED + ".");
                } else {
                    duty.add(player.getUniqueId());

                    player.sendMessage(PREFIX + "Admin-Modus wurde " + ChatColor.GREEN + "aktiviert" + ChatColor.RED + ".");
                }
            }
        }
        return true;
    }
}
