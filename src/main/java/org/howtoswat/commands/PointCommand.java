package org.howtoswat.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.howtoswat.HowToSWAT;
import org.howtoswat.utils.AdminUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PointCommand implements CommandExecutor, TabCompleter {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "POINT" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isSupporter(player.getUniqueId().toString())) {
                if (args.length > 0) {
                    if (HowToSWAT.warps.containsKey(args[0].toLowerCase())) {
                        HowToSWAT.warps.remove(args[0].toLowerCase());

                        player.sendMessage(PREFIX + "Der Custom-Punkt " + ChatColor.YELLOW + args[0] + ChatColor.GOLD + " wurde entfernt.");
                    } else {
                        HowToSWAT.warps.put(args[0].toLowerCase(), player.getLocation());

                        player.sendMessage(PREFIX + "Du hast den Custom-Punkt " + ChatColor.YELLOW + args[0] + ChatColor.GOLD + " erstellt.");
                    }

                    HowToSWAT.warpsconfig.set("warps", HowToSWAT.warps);
                    try {
                        HowToSWAT.warpsconfig.save(HowToSWAT.warpssave);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    player.sendMessage(PREFIX + "Gib einen Namen für den Punkt an!");
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
        List<String> targets = new ArrayList<>();
        for (String point : HowToSWAT.warps.keySet()) targets.add(StringUtils.capitalize(point));

        if (args.length == 1) for (String target : targets) if (target.toUpperCase().startsWith(args[0].toUpperCase())) list.add(target);
        return list;
    }
}
