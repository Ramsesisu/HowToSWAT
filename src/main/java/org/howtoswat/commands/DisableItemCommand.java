package org.howtoswat.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.howtoswat.enums.Items;
import org.howtoswat.utils.AdminUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DisableItemCommand implements CommandExecutor, TabCompleter {

    public static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "" + ChatColor.BOLD + "ITEM" + ChatColor.DARK_GRAY + "] " + ChatColor.BLUE;
    public static final List<String> disabled = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isSupporter(player.getUniqueId().toString())) {
                if (args.length > 0) {
                    for (Items items : Items.values()) {
                        if (items.getName().equalsIgnoreCase(args[0])) {
                            if (disabled.remove(items.getItem().getItemMeta().getDisplayName())) {
                                Bukkit.broadcastMessage(PREFIX + "Das Item " + ChatColor.AQUA + StringUtils.capitalize(items.getName()) + ChatColor.BLUE + " wurde " + ChatColor.GREEN + "aktiviert" + ChatColor.BLUE + ".");
                            } else {
                                disabled.add(items.getItem().getItemMeta().getDisplayName());

                                Bukkit.broadcastMessage(PREFIX + "Das Item " + ChatColor.AQUA + StringUtils.capitalize(items.getName()) + ChatColor.BLUE + " wurde " + ChatColor.RED + "deaktiviert" + ChatColor.BLUE + ".");
                            }
                        }
                    }
                } else {
                    player.sendMessage(PREFIX + "Gib ein Item an!");
                }
            } else {
                player.sendMessage(PREFIX + "Du bist kein Supporter!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        Set<String> targets = new HashSet<>();
        for (Items items : Items.values()) targets.add(StringUtils.capitalize(items.getName()));
        if (args.length == 1) for (String target : targets) if (target.toUpperCase().startsWith(args[0].toUpperCase())) list.add(target);
        return list;
    }
}
