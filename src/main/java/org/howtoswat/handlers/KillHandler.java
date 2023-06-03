package org.howtoswat.handlers;

import com.destroystokyo.paper.Title;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.howtoswat.commands.BuildmodeCommand;
import org.howtoswat.commands.EquipCommand;

import java.util.*;

import static org.howtoswat.HowToSWAT.PLUGIN;

public class KillHandler implements Listener {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "KILL" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;

    public static final HashMap<UUID, String> lastdamage = new HashMap<>();

    public static final List<UUID> deadplayers = new ArrayList<>();

    private static final HashMap<UUID, String> deathloadmsg = new HashMap<>();

    @EventHandler
    public static void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        player.getInventory().clear();

        Location loc = player.getLocation();

        ChatColor corpsecolor = ChatColor.GRAY;

        String message = ChatColor.GRAY + "" + ChatColor.BOLD + player.getName() + ChatColor.WHITE + " ist gestorben.";
        List<String> messages;
        String name = ChatColor.GRAY + "" + ChatColor.BOLD + player.getName() + ChatColor.WHITE;
        String killer;
        String item;
        switch (player.getLastDamageCause().getCause()) {
            case BLOCK_EXPLOSION:
            case WITHER:
                corpsecolor = ChatColor.DARK_GRAY;

                messages = Arrays.asList(
                        name + " ist in 1000 Stücke gesprengt worden.",
                        name + " wurde zu Hühnerfrikassee verarbeitet.",
                        name + " hätte Hiroshima nicht überlebt.",
                        name + " hat einen Creeper umarmt.",
                        name + " hat sich Dynamit-Stangen eingeführt.",
                        name + " ist jetzt bei seinen 72 Jungfrauen.",
                        name + " zerstob zu grober Mettwurst.");
                message = messages.get(new Random().nextInt(messages.size()));
                break;
            case FALL:
                messages = Arrays.asList(
                        name + " hat nicht auf Newton gehört.",
                        name + " wurde von der Gravitation hingerichtet.",
                        name + " hat die MLG Water Bucket Challenge verkackt.",
                        name + " dachte er könnte fliegen.",
                        name + " ist jetzt ein Pfannkuchen.",
                        name + " ist an einem Beinbruch gestorben #unangenehm.");
                message = messages.get(new Random().nextInt(messages.size()));
                break;
            case FIRE_TICK:
            case LAVA:
                messages = Arrays.asList(
                        name + " hat seine Chicken-Nuggets verbrannt.",
                        name + " wurde im Ofen eingeschlossen.",
                        name + " hatte nicht den High-Ground.",
                        name + " hat den Chili-Auflauf beim Asiaten unterschätzt.");
                message = messages.get(new Random().nextInt(messages.size()));
                break;
            case PROJECTILE:
                killer = ChatColor.GRAY + player.getKiller().getName() + ChatColor.WHITE;
                item = ChatColor.GRAY + "Unbekannt" + ChatColor.WHITE;
                if (lastdamage.containsKey(player.getUniqueId())) item = ChatColor.GRAY + StringUtils.capitalize(lastdamage.get(player.getUniqueId())) + ChatColor.WHITE;
                messages = Arrays.asList(
                        name + " wurde von " + killer + " mit einer " + item + " niedergeschossen.",
                        name + " fiel " + killer + " mit einer " + item + " zum Opfer.",
                        killer + " war mit seiner " + item + " zu gut für " + name + ".",
                        killer + " hat " + name + " mit seiner " + item + " zerfleischt.",
                        name + " wurde von " + killer + " mit einer " + item + " massakriert." ,
                        name + " hat " + killer + "'s " + item + " unterschätzt.",
                        name + " erlag " + killer + " und einem " + item + "-Bleihagel.",
                        killer + " richtete " + name + " mit einer " + item + " hin.",
                        name + " war doch nicht immun gegen " + killer + "'s " + item + ".",
                        name + " verlor seine Familienehre an " + killer + "'s " + item + ".",
                        name + " wurde von " + killer + " durch eine " + item + " präzise exekutiert.",
                        name + " blutete im Duell gegen " + killer + " durch eine " + item + " aus.",
                        name + " versagte peinlichst gegen " + killer + " und eine " + item + ".",
                        killer + " missbrauchte " + name + " als seine " + item + "-Zielscheibe.",
                        name + " lernte durch " + killer + "'s " + item + " in den Himmel zu fliegen.",
                        killer + " verwandelte " + name + "'s Kopf mit einer " + item + " in grobes Hack.",
                        name + " hat den Wilden Westen mit " + killer + " und einer " + item + " nicht überlebt.");
                message = messages.get(new Random().nextInt(messages.size()));
                break;
            case ENTITY_ATTACK:
                killer = ChatColor.GRAY + player.getKiller().getName() + ChatColor.WHITE;
                item = ChatColor.GRAY + "Unbekannt" + ChatColor.WHITE;
                if (lastdamage.containsKey(player.getUniqueId())) item = ChatColor.GRAY + StringUtils.capitalize(lastdamage.get(player.getUniqueId())) + ChatColor.WHITE;
                messages = Arrays.asList(
                        name + " wurde von " + killer + " mit einem " + item + " niedergestreckt.",
                        name + " wurde von " + killer + " mit einem " + item + " erstochen.",
                        name + " wurde von " + killer + "'s " + item + " erschlagen.",
                        killer + " rammte " + name + " sein " + item + " in den Bauch.",
                        killer + " zerteilte " + name + " mit einem " + item + " in Stücke.",
                        name + " wurde von " + killer + " mit einem überdimensionalen " + item + " vermöbelt.");
                message = messages.get(new Random().nextInt(messages.size()));
                break;
        }

        for (Entity entity : loc.getNearbyEntities(25, 25, 25)) {
            entity.sendMessage(PREFIX + message);
        }

        Entity corpse = player.getWorld().dropItem(loc, new ItemStack(Material.SKULL_ITEM));
        Bukkit.getScheduler().runTaskLater(PLUGIN, corpse::remove, 300L);
        corpse.setCustomName(corpsecolor + "✟ " + player.getName());

        Bukkit.getScheduler().runTaskLater(PLUGIN, () -> corpse.setCustomNameVisible(true), 10L);

        deadplayers.add(player.getUniqueId());

        event.setCancelled(true);

        player.setGameMode(GameMode.SPECTATOR);

        deathloadmsg.put(player.getUniqueId(), ChatColor.translateAlternateColorCodes('&', "&8&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛"));
        BukkitRunnable deathload = new BukkitRunnable() {
            @Override
            public void run() {
                deathloadmsg.put(player.getUniqueId(), deathloadmsg.get(player.getUniqueId()).replaceFirst("8", "a"));

                player.sendActionBar(deathloadmsg.get(player.getUniqueId()));

                if (!deathloadmsg.get(player.getUniqueId()).contains("8")) {
                    player.sendTitle(new Title(ChatColor.GREEN + "Du lebst nun wieder!", "", 10, 30, 20));

                    if (EquipCommand.equipments.containsKey(player.getUniqueId())) EquipCommand.equip(player, EquipCommand.equipments.get(player.getUniqueId()));

                    Location spawn = player.getBedSpawnLocation();
                    spawn.setWorld(player.getWorld());
                    player.teleport(spawn);

                    if (BuildmodeCommand.buildmode.contains(player.getUniqueId())) {
                        player.setGameMode(GameMode.CREATIVE);
                    } else {
                        player.setGameMode(GameMode.SURVIVAL);
                    }

                    for (PotionEffect effect : player.getActivePotionEffects()) player.removePotionEffect(effect.getType());

                    deadplayers.remove(player.getUniqueId());
                    GunHandler.startSpawnschutz(player);

                    cancel();
                }
            }
        };

        JoinHandler.playertasks.putIfAbsent(player.getUniqueId(), new ArrayList<>());
        List<BukkitTask> tasks = JoinHandler.playertasks.get(player.getUniqueId());
        tasks.add(deathload.runTaskTimer(PLUGIN, 0L, 20L));
        JoinHandler.playertasks.put(player.getUniqueId(), tasks);
    }
}
