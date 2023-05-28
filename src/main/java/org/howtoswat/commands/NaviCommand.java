package org.howtoswat.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.howtoswat.enums.NaviPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.howtoswat.HowToSWAT.PLUGIN;

public class NaviCommand implements CommandExecutor, TabCompleter {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "NAVI" + ChatColor.DARK_GRAY + "] " + ChatColor.YELLOW;
    public static final HashMap<UUID, BukkitTask> navitask = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (navitask.containsKey(player.getUniqueId())) {
                navitask.get(player.getUniqueId()).cancel();

                navitask.remove(player.getUniqueId());

                player.sendMessage(PREFIX + "Du hast dein Navi zurückgesetzt.");
            } else {
                Location loc = null;
                if (args.length > 0) {
                    for (NaviPoint point : NaviPoint.values()) {
                        if (point.getName().replace(" ", "-").equalsIgnoreCase(args[0])) {
                            loc = point.getLocation(player.getWorld());

                            player.sendMessage(PREFIX + "Dir wird nun die Route zu Punkt " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " angezeigt.");
                        }
                    }
                    String[] arg = args[0].split("/");
                    if (arg.length == 3) {
                        List<Double> coords = new ArrayList<>();
                        for (String coord : arg) coords.add(Double.valueOf(coord));
                        loc = new Location(player.getWorld(), coords.get(0), coords.get(1), coords.get(2));

                        player.sendMessage(PREFIX + "Dir wird nun die Route zu Koordinate " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " angezeigt.");
                    }
                    if (args[0].equalsIgnoreCase("Bombe")) {
                        if (BombeCommand.bombs.containsKey(player.getUniqueId())) {
                            loc = BombeCommand.bombs.get(player.getUniqueId());

                            player.sendMessage(PREFIX + "Dir wird nun die Route zu deiner Bombe angezeigt.");
                        }
                    }
                    if (loc == null) {
                        player.sendMessage(PREFIX + "Der Punkt " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " wurde nicht gefunden!");
                        return true;
                    }

                    Location finalLoc = loc;
                    BukkitRunnable navi = new BukkitRunnable() {
                        @Override
                        public void run() {
                            Location origin = player.getLocation().add(0, 0.5, 0);
                            Vector direction = finalLoc.clone().subtract(player.getLocation()).toVector();

                            direction.normalize();
                            for (int i = 0; i < 20; i++) {
                                Location temploc = origin.add(direction);
                                player.spawnParticle(Particle.REDSTONE, temploc.subtract(direction.clone().multiply(0.75)), 1, 0.05, 0.05, 0.05, 0);
                            }

                        /*
                        if (navitype.get(player.getName())) {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&6Noch &l" + ((int) Math.floor(Math.sqrt(Math.pow(player.getLocation().getX() - finalLoc.getX(), 2) + Math.pow(player.getLocation().getZ() - finalLoc.getZ(), 2)))) + "m&6 bis zum Ziel.")));
                        } else {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&6Noch &l" + ((int) Math.floor(Math.sqrt(Math.pow(player.getLocation().getX() - finalLoc.getX(), 2) + Math.pow(player.getLocation().getY() - finalLoc.getY(), 2) + Math.pow(player.getLocation().getZ() - finalLoc.getZ(), 2)))) + "m&6 bis zum Ziel.")));
                        }
                         */
                        }
                    };

                    navitask.put(player.getUniqueId(), navi.runTaskTimer(PLUGIN, 0L, 5L));
                } else {
                    player.sendMessage(PREFIX + "Du hast keinen Punkt angegeben!");
                }
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
        if (args.length == 1) for (String target : targets) if (target.toUpperCase().startsWith(args[0].toUpperCase())) list.add(target);
        return list;
    }
}
