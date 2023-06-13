package org.howtoswat;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Minecart;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.howtoswat.commands.*;
import org.howtoswat.handlers.*;
import org.howtoswat.utils.DataUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HowToSWAT extends JavaPlugin {

    public static HowToSWAT PLUGIN;

    public static String VERSION = "2.1";

    private final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "SERVER" + ChatColor.DARK_GRAY + "] " + ChatColor.RED;

    public static final List<Object> admins = new ArrayList<>();
    public static final List<Object> supporter = new ArrayList<>();
    public static final List<Object> builder = new ArrayList<>();
    public static final List<Object> verifies = new ArrayList<>();

    public static final HashMap<String, Location> warps = new HashMap<>();

    public static World training;
    public static World baustelle;

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
    public void onDisable() {
        for (Location bombloc : BombeCommand.bombs.values()) {
            bombloc.getWorld().getBlockAt(bombloc).setType(Material.AIR);
        }

        for (Minecart minecart : CarCommand.minecarts.values()) {
            minecart.remove();
        }

        for (World world : PrivateCommand.worlds) {
            Bukkit.getServer().unloadWorld(world, false);
        }
    }

    private void loadWorlds() {
        try {
            FileUtils.deleteDirectory(new File("private"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        training = new WorldCreator("Training").createWorld();
        baustelle = new WorldCreator("Baustelle").createWorld();

        for (World world : Bukkit.getServer().getWorlds()) {
            world.setGameRuleValue("announceAdvancements", "false");
            world.setGameRuleValue("disableElytraMovementCheck", "true");
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("keepInventory", "true");
            world.setGameRuleValue("mobGriefing", "false");
            world.setGameRuleValue("naturalRegeneration", "true");
            world.setGameRuleValue("sendCommandFeedback", "false");
            world.setGameRuleValue("showDeathMessages", "false");
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(PLUGIN, () -> {
            training.save();
            baustelle.save();
        }, 0L, 100L);
    }

    public static File adminsave;
    public static YamlConfiguration adminconfig;
    public static File supportersave;
    public static YamlConfiguration supporterconfig;
    public static File buildersave;
    public static YamlConfiguration builderconfig;
    public static File verifysave;
    public static YamlConfiguration verifyconfig;
    public static File warpssave;
    public static YamlConfiguration warpsconfig;

    private void loadData() {
        adminsave = new File("data" + File.separator + "server" + File.separator + "admins.yml");
        adminconfig = DataUtils.loadData(adminsave, "admins", admins);

        supportersave = new File("data" + File.separator + "server" + File.separator + "supporter.yml");
        supporterconfig = DataUtils.loadData(supportersave, "supporter", supporter);

        buildersave = new File("data" + File.separator + "server" + File.separator + "builder.yml");
        builderconfig = DataUtils.loadData(buildersave, "builder", builder);

        verifysave = new File("data" + File.separator + "server" + File.separator + "verified.yml");
        verifyconfig = DataUtils.loadData(verifysave, "verify", verifies);

        warpssave = new File("data" + File.separator + "server" + File.separator + "warps.yml");
        warpsconfig = YamlConfiguration.loadConfiguration(warpssave);
        DataUtils.checkFile(warpssave, warpsconfig, "warps", new ArrayList<>());
        try {
            MemorySection section = (MemorySection) warpsconfig.getValues(false).get("warps");
            Map<String, Object> values = section.getValues(false);
            for (Object value : values.keySet()) {
                warps.put(value.toString(), (Location) values.get(value));
            }
        } catch (ClassCastException ignored) {
        }
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
        getCommand("navi").setExecutor(new NaviCommand());
        getCommand("luftlinie").setExecutor(new LuftlinieCommand());
        getCommand("sprenggürtel").setExecutor(new SprengguertelCommand());
        getCommand("bombe").setExecutor(new BombeCommand());
        getCommand("annehmen").setExecutor(new AnnehmenCommand());
        getCommand("ablehnen").setExecutor(new AblehnenCommand());
        getCommand("verify").setExecutor(new VerifyCommand());
        getCommand("requestverify").setExecutor(new RequestVerifyCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("run").setExecutor(new RunCommand());
        getCommand("duty").setExecutor(new DutyCommand());
        getCommand("help").setExecutor(new HelpCommand());
        getCommand("car").setExecutor(new CarCommand());
        getCommand("disablecommand").setExecutor(new DisableCommandCommand());
        getCommand("disableitem").setExecutor(new DisableItemCommand());
        getCommand("adminchat").setExecutor(new AdminChatCommand());
        getCommand("msg").setExecutor(new MsgCommand());
        getCommand("supporter").setExecutor(new SupporterCommand());
        getCommand("teleport").setExecutor(new TeleportCommand());
        getCommand("private").setExecutor(new PrivateCommand());
        getCommand("point").setExecutor(new PointCommand());
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
        pluginManager.registerEvents(new CommandHandler(), this);
        pluginManager.registerEvents(new CarHandler(), this);
    }
}
