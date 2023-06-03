package org.howtoswat.commands;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.howtoswat.utils.AdminUtils;
import org.howtoswat.utils.VerifyUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.howtoswat.HowToSWAT.PLUGIN;

public class PrivateCommand implements CommandExecutor, TabCompleter {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "PRIVATE" + ChatColor.DARK_GRAY + "] " + ChatColor.BLUE;

    public static List<World> worlds = new ArrayList<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();


            if (player.getWorld().getName().startsWith("./private")) {
                String name = player.getWorld().getWorldFolder().getAbsoluteFile().getName();

                Location loc = player.getLocation();
                loc.setWorld(Bukkit.getServer().getWorld("Training"));
                player.teleport(loc);

                player.sendMessage(PREFIX + "Du hast die Welt von " + ChatColor.GRAY + name + ChatColor.BLUE + " verlassen.");
            } else {
                if (args.length == 0) {
                    for (World world : worlds) {
                        if (world.getWorldFolder().getAbsoluteFile().getName().equals(player.getName())) {
                            for (Player target : world.getPlayers()) {
                                Location loc = target.getLocation();
                                loc.setWorld(Bukkit.getServer().getWorld("Training"));
                                target.teleport(loc);

                                target.sendMessage(PREFIX + "Die Welt von " + ChatColor.GRAY + world.getWorldFolder().getAbsoluteFile().getName() + ChatColor.BLUE + " wurde geschlossen.");
                            }

                            Bukkit.getServer().unloadWorld(world, false);
                            worlds.remove(world);
                            try {
                                FileUtils.deleteDirectory(world.getWorldFolder());
                            } catch (IOException ignored) {
                            }

                            player.sendMessage(PREFIX + "Du hast deine private Welt geschlossen.");
                            return true;
                        }
                    }

                    if (AdminUtils.isVerified(player.getUniqueId().toString())) {
                        if (worlds.size() > 10) {
                            player.sendMessage(PREFIX + "Die Maximalanzahl an privaten Welten wurde bereits erreicht!");
                        } else {
                            File folder = new File(Bukkit.getWorldContainer() + "/private", player.getName());
                            try {
                                FileUtils.copyDirectory(Bukkit.getServer().getWorld("Training").getWorldFolder(), folder);
                                new File(folder.getPath(), "uid.dat").delete();
                                new File(folder.getPath(), "session.lock").delete();
                            } catch (IOException e) {
                                player.sendMessage(PREFIX + "Die Welt konnte nicht erstellt werden!");
                            }

                            World world = new WorldCreator(folder.getPath()).createWorld();
                            world.setAutoSave(false);

                            player.teleport(world.getSpawnLocation());

                            worlds.add(world);

                            player.sendMessage(PREFIX + "Deine private Welt wurde erstellt.");

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (world.getPlayers().size() == 0) {
                                        Bukkit.getServer().unloadWorld(world, false);
                                        worlds.remove(world);
                                        try {
                                            FileUtils.deleteDirectory(world.getWorldFolder());
                                        } catch (IOException ignored) {
                                        }

                                        player.sendMessage(PREFIX + "Deine private Welt wurde geschlossen.");
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(PLUGIN, 200L, 200L);
                        }
                    } else {
                        VerifyUtils.verifyMessage(player);
                    }
                } else {
                    World world = Bukkit.getServer().getWorld("./private/" + args[0]);
                    if (world != null) {
                        Player owner = Bukkit.getServer().getPlayer(world.getWorldFolder().getAbsoluteFile().getName());
                        if (owner != null) {
                            Runnable runnable = () -> {
                                player.teleport(world.getSpawnLocation());

                                player.sendMessage(PREFIX + "Du hast die Welt von " + ChatColor.GRAY + args[0] + ChatColor.BLUE + " betreten.");
                            };
                            if (owner.getName().equals(player.getName())) {
                                runnable.run();
                            } else {
                                VerifyUtils.addRequest(player, owner, PREFIX + ChatColor.GRAY + player.getName() + ChatColor.BLUE + " möchte deine private Welt betreten.", runnable);

                                player.sendMessage(VerifyUtils.PREFIX + "Deine Private-Anfrage wurde gestellt.");
                            }
                        } else {
                            player.sendMessage(PREFIX + "Der Owner " + ChatColor.GRAY + args[0] + ChatColor.BLUE + " ist nicht online!");
                        }
                    } else {
                        player.sendMessage(PREFIX + "Die Welt " + ChatColor.GRAY + args[0] + ChatColor.BLUE + " existiert nicht!");
                    }
                }
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        for (World world : worlds) {
            targets.add(world.getWorldFolder().getAbsoluteFile().getName());
        }

        for (String target : targets) {
            if (target.toUpperCase().startsWith(args[0].toUpperCase()))
                list.add(target);
        }
        return list;
    }
}
