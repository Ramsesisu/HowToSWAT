package org.howtoswat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.howtoswat.commands.FlyCommand;
import org.howtoswat.handlers.JoinHandler;

public final class HowToSWAT extends JavaPlugin {

    public static String VERSION = Bukkit.getServer().getVersion();

    private final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "SERVER" + ChatColor.DARK_GRAY + "] " + ChatColor.RED;

    @Override
    public void onEnable() {
        loadWorlds();

        registerCommands();
        registerHandlers();

        Bukkit.broadcastMessage(PREFIX + "Der Server wurde reloaded!");
        Bukkit.broadcastMessage("     ➥ Version: " + VERSION);
    }

    @Override
    public void onDisable() {
        // Soon
    }

    private void loadWorlds() {
        new WorldCreator("Training").createWorld();
    }

    private void registerCommands() {
        getCommand("fly").setExecutor(new FlyCommand());
    }

    private void registerHandlers() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinHandler(), this);
    }
}
