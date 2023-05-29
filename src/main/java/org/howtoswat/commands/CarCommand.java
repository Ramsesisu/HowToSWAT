package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.material.Rails;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.howtoswat.HowToSWAT.PLUGIN;

public class CarCommand implements CommandExecutor, TabCompleter {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "" + ChatColor.BOLD + "CAR" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    public static final HashMap<UUID, Minecart> minecarts = new HashMap<>();
    public static final HashMap<UUID, BukkitTask> cartasks = new HashMap<>();

    public static final HashMap<UUID, Double> kilometer = new HashMap<>();
    public static final HashMap<UUID, Integer> zustand = new HashMap<>();
    public static final HashMap<UUID, Double> tacho = new HashMap<>();
    public static final HashMap<UUID, Double> tank = new HashMap<>();
    public static final HashMap<UUID, Integer> gang = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (minecarts.containsKey(player.getUniqueId())) {
                    minecarts.get(player.getUniqueId()).remove();
                    minecarts.remove(player.getUniqueId());
                    player.sendMessage(PREFIX + "Du hast dein Auto gelöscht.");
                } else {
                    Minecart minecart = player.getWorld().spawn(player.getLocation(), Minecart.class);
                    minecart.setCustomName(player.getName());
                    minecart.setMaxSpeed(Double.MAX_VALUE);
                    minecarts.put(player.getUniqueId(), minecart);
                    tank.put(player.getUniqueId(), 100.0);
                    player.sendMessage(PREFIX + "Du hast dein Auto gesetzt.");
                }
            } else {
                switch (args[0]) {
                    case "find":
                        if (minecarts.get(player.getUniqueId()) != null) {
                            player.teleport(minecarts.get(player.getUniqueId()).getLocation());

                            player.sendMessage(PREFIX + "Du wurdest zu deinem Auto teleportiert.");
                        }
                        break;
                    case "start":
                        if (player.isInsideVehicle()) {
                            if (player.getVehicle() instanceof Minecart) {
                                Minecart car = minecarts.get(player.getUniqueId());
                                cartasks.putIfAbsent(player.getUniqueId(), null);
                                if (cartasks.get(player.getUniqueId()) != null) {
                                    cartasks.get(player.getUniqueId()).cancel();
                                    cartasks.put(player.getUniqueId(), null);
                                }

                                BukkitRunnable cartask = new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        Block carblock = car.getLocation().add(0, 1, 0).getBlock();
                                        if (carblock.getType() == Material.AIR && car.getLocation().getBlock() instanceof Rails) {
                                            carblock.setType(Material.RAILS);
                                            carblock.setType(Material.AIR);
                                        }

                                        if (gang.get(player.getUniqueId()) == 0) {
                                            tacho.put(player.getUniqueId(), tacho.get(player.getUniqueId()) + 0.1);

                                            if (tacho.get(player.getUniqueId()) >= 1) {
                                                gang.put(player.getUniqueId(), 1);
                                            }
                                        }
                                        if (gang.get(player.getUniqueId()) == 1) {
                                            tacho.put(player.getUniqueId(), tacho.get(player.getUniqueId()) + 0.2);

                                            if (tacho.get(player.getUniqueId()) >= 5) {
                                                gang.put(player.getUniqueId(), 2);
                                            }
                                        }
                                        if (gang.get(player.getUniqueId()) == 2) {
                                            tacho.put(player.getUniqueId(), tacho.get(player.getUniqueId()) + 0.4);

                                            if (tacho.get(player.getUniqueId()) >= 15) {
                                                gang.put(player.getUniqueId(), 3);
                                            }
                                        }
                                        if (gang.get(player.getUniqueId()) == 3) {
                                            tacho.put(player.getUniqueId(), tacho.get(player.getUniqueId()) + 0.8);

                                            if (tacho.get(player.getUniqueId()) >= 30) {
                                                gang.put(player.getUniqueId(), 4);
                                            }
                                        }
                                        if (gang.get(player.getUniqueId()) == 4) {
                                            tacho.put(player.getUniqueId(), tacho.get(player.getUniqueId()) + 1.6);

                                            if (tacho.get(player.getUniqueId()) >= 60) {
                                                gang.put(player.getUniqueId(), 5);
                                            }
                                        }
                                        if (gang.get(player.getUniqueId()) == 5) {
                                            tacho.put(player.getUniqueId(), tacho.get(player.getUniqueId()) + 3.2);

                                            if (tacho.get(player.getUniqueId()) >= 120) {
                                                gang.put(player.getUniqueId(), 6);
                                            }
                                        }
                                        if (gang.get(player.getUniqueId()) == 6) {
                                            tacho.put(player.getUniqueId(), tacho.get(player.getUniqueId()) + 6.4);

                                            if (tacho.get(player.getUniqueId()) >= 167) {
                                                tacho.put(player.getUniqueId(), 167.0);
                                            }
                                        }

                                        if (player.getLocation().getPitch() == 90) {
                                            cancel();
                                        }
                                        if (player.getLocation().getPitch() < 0) {
                                            cancel();
                                        }
                                        float pitch = Math.abs(player.getLocation().getPitch());
                                        if (pitch < 15) {
                                            pitch = 10;
                                        }
                                        double speed = tacho.get(player.getUniqueId()) / (pitch * 10);
                                        car.setVelocity(player.getLocation().getDirection().multiply(1 + speed));
                                        kilometer.put(player.getUniqueId(), kilometer.get(player.getUniqueId()) + 0.002);
                                        tank.put(player.getUniqueId(), tank.get(player.getUniqueId()) - 0.002);

                                        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
                                        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

                                        final Objective[] objective = new Objective[1];
                                        objective[0] = scoreboard.registerNewObjective(ChatColor.GOLD + "Bimborgini", "dummy");
                                        objective[0].setDisplaySlot(DisplaySlot.SIDEBAR);

                                        Score kilometerscore = objective[0].getScore(ChatColor.translateAlternateColorCodes('&', "&aKilometer&8:"));
                                        kilometerscore.setScore((int) Math.ceil(kilometer.get(player.getUniqueId())));
                                        Score zustandscore = objective[0].getScore(ChatColor.translateAlternateColorCodes('&', "&aZustand&8:"));
                                        zustandscore.setScore(zustand.get(player.getUniqueId()));
                                        Score tachoscore = objective[0].getScore(ChatColor.translateAlternateColorCodes('&', "&aTacho&8:"));
                                        tachoscore.setScore((int) Math.round(tacho.get(player.getUniqueId())));
                                        Score tankscore = objective[0].getScore(ChatColor.translateAlternateColorCodes('&', "&aTank&8:"));
                                        tankscore.setScore((int) Math.floor(tank.get(player.getUniqueId())));
                                        Score gangscore = objective[0].getScore(ChatColor.translateAlternateColorCodes('&', "&aGang&8:"));
                                        gangscore.setScore(gang.get(player.getUniqueId()));

                                        player.setScoreboard(scoreboard);
                                    }
                                };
                                cartasks.put(player.getUniqueId(), cartask.runTaskTimer(PLUGIN, 0L, 1L));

                                kilometer.putIfAbsent(player.getUniqueId(), 0.0);
                                zustand.putIfAbsent(player.getUniqueId(), 1500);
                                tacho.put(player.getUniqueId(), 0.0);
                                tank.putIfAbsent(player.getUniqueId(), 100.0);
                                gang.put(player.getUniqueId(), 0);

                                if (args.length == 1) {
                                    player.sendMessage(PREFIX + "Du hast dein Auto gestartet.");
                                }
                            }
                        }
                        break;
                    default:
                        player.sendMessage(PREFIX + ChatColor.DARK_GRAY + args[0] + ChatColor.GRAY + " ist kein gültiges Argument!");
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        String[] targets = new String[] {"find", "start"};
        if (args.length == 1) for (String target : targets) if (target.toUpperCase().startsWith(args[0].toUpperCase())) list.add(target);
        return list;
    }
}
