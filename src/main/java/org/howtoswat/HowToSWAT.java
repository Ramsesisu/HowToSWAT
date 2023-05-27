package org.howtoswat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.howtoswat.commands.*;
import org.howtoswat.handlers.*;
import org.howtoswat.utils.DataUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class HowToSWAT extends JavaPlugin {

    public static HowToSWAT PLUGIN;

    public static String VERSION = "2.0";

    private final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "SERVER" + ChatColor.DARK_GRAY + "] " + ChatColor.RED;

    public static final List<Object> admins = new ArrayList<>();
    public static final List<Object> builder = new ArrayList<>();

    @Override
    public void onEnable() {
        PLUGIN = this;

        loadWorlds();
        loadData();

        registerCommands();
        registerHandlers();

        Bukkit.getScheduler().runTaskLater(PLUGIN, () -> Bukkit.broadcastMessage(PREFIX + "Der Server wurde reloaded!"), 10L);
    }

    @Override
    public void onDisable() {}

    private void loadWorlds() {
        new WorldCreator("Training").createWorld();
        new WorldCreator("Baustelle").createWorld();
    }

    public static File adminsave;
    public static YamlConfiguration adminconfig;
    public static File buildersave;
    public static YamlConfiguration builderconfig;

    private void loadData() {
        adminsave = new File("data" + File.separator + "server" + File.separator + "admins.yml");
        adminconfig = YamlConfiguration.loadConfiguration(adminsave);
        DataUtils.checkFile(adminsave, adminconfig, "admins", admins);
        DataUtils.setValues(adminconfig, "admins", admins);

        buildersave = new File("data" + File.separator + "server" + File.separator + "builder.yml");
        builderconfig = YamlConfiguration.loadConfiguration(buildersave);
        DataUtils.checkFile(buildersave, builderconfig, "builder", builder);
        DataUtils.setValues(builderconfig, "builder", builder);
    }

    private void registerCommands() {
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("camera").setExecutor(new CameraCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("use").setExecutor(new UseCommand());
        getCommand("equip").setExecutor(new EquipCommand());
        getCommand("cooldown").setExecutor(new CoolDownCommand());
        getCommand("save").setExecutor(new SaveCommand());
        getCommand("admins").setExecutor(new AdminsCommand());
        getCommand("builder").setExecutor(new BuilderCommand());
        getCommand("buildmode").setExecutor(new BuildmodeCommand());
        getCommand("baustelle").setExecutor(new BaustelleCommand());
        getCommand("warp").setExecutor(new WarpCommand());
    }

    private void registerHandlers() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinHandler(), this);
        pluginManager.registerEvents(new DamageHandler(), this);
        pluginManager.registerEvents(new BlockHandler(), this);
        pluginManager.registerEvents(new ChatHandler(), this);
        pluginManager.registerEvents(new GunHandler(), this);
        pluginManager.registerEvents(new ItemHandler(), this);
        pluginManager.registerEvents(new ExplosiveHandler(), this);
        pluginManager.registerEvents(new KillHandler(), this);
        pluginManager.registerEvents(new GrenadeHandler(), this);
    }
}
