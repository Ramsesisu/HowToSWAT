package org.howtoswat.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.howtoswat.HowToSWAT;
import org.howtoswat.enums.NaviPoint;
import org.howtoswat.utils.VerifyUtils;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "WARP" + ChatColor.DARK_GRAY + "] " + ChatColor.YELLOW;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (args.length > 0) {
                for (NaviPoint point : NaviPoint.values()) {
                    if (point.getName().replace(" ", "-").equalsIgnoreCase(args[0])) {
                        player.teleport(point.getLocation(player.getWorld()));

                        player.sendMessage(PREFIX + "Du wurdest zu Punkt " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " teleportiert.");
                        return true;
                    }
                }
                for (String point : HowToSWAT.warps.keySet()) {
                    if (point.equalsIgnoreCase(args[0])) {
                        Location target = HowToSWAT.warps.get(point);
                        target.setWorld(player.getWorld());
                        player.teleport(target);

                        player.sendMessage(PREFIX + "Du wurdest zu Custom-Punkt " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " teleportiert.");
                        return true;
                    }
                }
                for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                    if (target.getName().equalsIgnoreCase(args[0])) {
                        if (target.getWorld() == player.getWorld()) {
                            VerifyUtils.addRequest(player, target, PREFIX + "Der Spieler " + ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " möchte sich zu dir warpen.", () -> {
                                player.teleport(target.getLocation());

                                player.sendMessage(PREFIX + "Du wurdest zu Spieler " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " teleportiert.");
                            });

                            player.sendMessage(VerifyUtils.PREFIX + "Deine Warp-Anfrage wurde gestellt.");
                        } else  {
                            player.sendMessage(PREFIX + "Der Spieler " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " befindet sich in einer anderen Welt!");
                        }
                        return true;
                    }
                }
                String[] arg = args[0].split("/");
                if (arg.length == 3) {
                    List<Double> coords = new ArrayList<>();
                    for (String coord : arg) coords.add(Double.valueOf(coord));
                    player.teleport(new Location(player.getWorld(), coords.get(0) + 0.5, coords.get(1), coords.get(2) + 0.5));

                    player.sendMessage(PREFIX + "Du wurdest zu Koordinate " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " teleportiert.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("Bombe")) {
                    if (BombeCommand.bombs.containsKey(player.getUniqueId())) {
                        player.teleport(BombeCommand.bombs.get(player.getUniqueId()));

                        player.sendMessage(PREFIX + "Du wurdest zu deiner Bombe teleportiert.");
                        return true;
                    }
                }
                player.sendMessage(PREFIX + "Der Punkt " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " wurde nicht gefunden!");
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
            Player player = (Player) sender;
            Location loc = player.getLocation();
            targets.add(loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ());

            if (BombeCommand.bombs.containsKey(player.getUniqueId())) targets.add("Bombe");
        }
        for (NaviPoint point : NaviPoint.values()) targets.add(point.getName().replace(" ", "-"));
        for (String point : HowToSWAT.warps.keySet()) targets.add(StringUtils.capitalize(point));
        for (Player player : Bukkit.getServer().getOnlinePlayers()) targets.add(player.getName());

        if (args.length == 1) for (String target : targets) if (target.toUpperCase().startsWith(args[0].toUpperCase())) list.add(target);
        return list;
    }
}
