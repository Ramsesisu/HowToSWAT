package org.howtoswat.handlers;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.howtoswat.commands.*;
import org.howtoswat.utils.AdminUtils;

import java.util.*;

import static org.howtoswat.HowToSWAT.PLUGIN;

public class JoinHandler implements Listener {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "JOIN" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD;

    public static HashMap<UUID, List<BukkitTask>> playertasks = new HashMap<>();

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(PREFIX + player.getName() + ChatColor.YELLOW + " ist nun " + ChatColor.GREEN + "online" + ChatColor.YELLOW + ".");

        Bukkit.getScheduler().runTaskLater(PLUGIN, () -> {
            player.sendMessage(ChatColor.GRAY + "Willkommen auf " + ChatColor.BLUE + "How" + ChatColor.DARK_RED + "To" + ChatColor.GOLD + "SWAT" + ChatColor.DARK_GRAY + ".eu" + ChatColor.GRAY + ", einem inoffiziellen UnicaCity-Trainingsserver mit universellen Features!");
            player.sendMessage(ChatColor.GRAY + "Plugin: " + ChatColor.DARK_GRAY + "rqmses" + ChatColor.GRAY + ", Server: " + ChatColor.DARK_GRAY + "Melerkhin" + ChatColor.GRAY + ", Map: " + ChatColor.DARK_GRAY + "UC-Bauteam");
        }, 20L);

        playertasks.putIfAbsent(player.getUniqueId(), new ArrayList<>());

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0D);

        player.setHealth(40.0D);

        Location loc;
        if (player.getBedSpawnLocation() == null) {
            loc = new Location(Bukkit.getWorld("Training"), 103, 70, 157);
            player.setBedSpawnLocation(loc, true);

            player.teleport(loc);
        }

        loc = player.getLocation();
        loc.setWorld(Bukkit.getWorld("Training"));
        if (Objects.equals(player.getWorld().getName(), "Baustelle")) {
            if (!AdminUtils.isBuilder(player.getUniqueId().toString())) {
                player.teleport(loc);
            }
        }

        if (EquipCommand.equipments.containsKey(player.getUniqueId())) EquipCommand.equip(player, EquipCommand.equipments.get(player.getUniqueId()));
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (NaviCommand.navitask.containsKey(player.getUniqueId())) {
            NaviCommand.navitask.get(player.getUniqueId()).cancel();

            NaviCommand.navitask.remove(player.getUniqueId());
        }

        if (BombeCommand.bombs.containsKey(player.getUniqueId())) {
            Location bombloc = BombeCommand.bombs.get(player.getUniqueId());
            bombloc.getWorld().getBlockAt(bombloc).setType(Material.AIR);

            BombeCommand.bombs.remove(player.getUniqueId());
        }

        if (KillHandler.deadplayers.contains(player.getUniqueId())) {
            if (BuildmodeCommand.buildmode.contains(player.getUniqueId())) {
                player.setPlayerListName(player.getPlayerListName() + BuildmodeCommand.SUFFIX);
                player.setGameMode(GameMode.CREATIVE);
            } else {
                player.setGameMode(GameMode.SURVIVAL);
            }

            player.teleport(player.getBedSpawnLocation());

            KillHandler.deadplayers.remove(player.getUniqueId());
        }

        BuildmodeCommand.buildmode.remove(player.getUniqueId());

        if (playertasks.containsKey(player.getUniqueId())) {
            for (BukkitTask task : playertasks.get(player.getUniqueId())) task.cancel();
        }

        if (CarCommand.minecarts.containsKey(player.getUniqueId())) {
            CarCommand.minecarts.remove(player.getUniqueId()).remove();
            if (CarCommand.cartasks.get(player.getUniqueId()) != null) {
                CarCommand.cartasks.get(player.getUniqueId()).cancel();
            }
        }

        event.setQuitMessage(PREFIX + player.getName() + ChatColor.YELLOW + " ist nun " + ChatColor.RED + "offline" + ChatColor.YELLOW + ".");
    }
}
