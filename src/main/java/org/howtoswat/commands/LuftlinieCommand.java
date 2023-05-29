package org.howtoswat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LuftlinieCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "" + ChatColor.BOLD + "LUFTLINIE" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            NaviCommand.navitype.putIfAbsent(player.getUniqueId(), false);
            if (NaviCommand.navitype.get(player.getUniqueId())) {
                NaviCommand.navitype.put(player.getUniqueId(), false);

                player.sendMessage(PREFIX + "Luftlinien-Modus wurde " + ChatColor.RED + "deaktiviert" + ChatColor.GOLD + ".");
            } else {
                NaviCommand.navitype.put(player.getUniqueId(), true);

                player.sendMessage(PREFIX + "Luftlinien-Modus wurde " + ChatColor.GREEN + "aktiviert" + ChatColor.GOLD + ".");
            }
        }
        return true;
    }
}
