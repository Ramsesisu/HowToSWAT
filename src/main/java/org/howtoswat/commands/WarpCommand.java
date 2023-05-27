package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.howtoswat.enums.NaviPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WarpCommand implements CommandExecutor, TabCompleter {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "WARP" + ChatColor.DARK_GRAY + "] " + ChatColor.YELLOW;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (args.length > 0) {
                for (NaviPoint point : NaviPoint.values()) {
                    if (Objects.equals(point.getName().replace(" ", "-"), args[0])) {
                        player.teleport(point.getLocation(player.getWorld()));
                        return true;
                    }
                }
                for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                    if (Objects.equals(target.getName(), args[0])) {
                        player.teleport(target.getLocation());
                        return true;
                    }
                }
                String[] arg = args[0].split("/");
                if (arg.length == 3) {
                    List<Double> coords = new ArrayList<>();
                    for (String coord : arg) coords.add(Double.valueOf(coord));
                    player.teleport(new Location(player.getWorld(), coords.get(0), coords.get(1), coords.get(2)));
                }
            } else {
                player.sendMessage(PREFIX + "Du hast keinen Punkt angegeben!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        if (sender instanceof Player) {
            Location loc = ((Player) sender).getLocation();
            targets.add(loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ());
        }
        for (NaviPoint point : NaviPoint.values()) targets.add(point.getName().replace(" ", "-"));
        for (Player player : Bukkit.getServer().getOnlinePlayers()) targets.add(player.getName());
        if (args.length == 1) for (String target : targets) if (target.toUpperCase().startsWith(args[0].toUpperCase())) list.add(target);
        return list;
    }
}
