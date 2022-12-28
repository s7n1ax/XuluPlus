/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.Mod$Instance
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.Display
 */
package me.alpha432.oyvey;

import me.alpha432.oyvey.manager.ColorManager;
import me.alpha432.oyvey.manager.CommandManager;
import me.alpha432.oyvey.manager.ConfigManager;
import me.alpha432.oyvey.manager.EventManager;
import me.alpha432.oyvey.manager.FileManager;
import me.alpha432.oyvey.manager.FriendManager;
import me.alpha432.oyvey.manager.HoleManager;
import me.alpha432.oyvey.manager.InventoryManager;
import me.alpha432.oyvey.manager.ModuleManager;
import me.alpha432.oyvey.manager.NotificationManager;
import me.alpha432.oyvey.manager.PacketManager;
import me.alpha432.oyvey.manager.PositionManager;
import me.alpha432.oyvey.manager.PotionManager;
import me.alpha432.oyvey.manager.ReloadManager;
import me.alpha432.oyvey.manager.RotationManager;
import me.alpha432.oyvey.manager.ServerManager;
import me.alpha432.oyvey.manager.SpeedManager;
import me.alpha432.oyvey.manager.TextManager;
import me.alpha432.oyvey.manager.TimerManager;
import me.alpha432.oyvey.manager.TotemPopManager;
import me.alpha432.oyvey.mixin.OyVeyLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid="xulu+", name="xulu+", version="1.0.6")
public class OyVey {
    public static final String MODID = "Xulu+";
    public static final String MODNAME = "Xulu+";
    public static final String MODVER = "1.0.6-DEV";
    public static final Logger LOGGER = LogManager.getLogger(OyVey.class);
    public static TotemPopManager totemPopManager;
    public static TimerManager timerManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    public static NotificationManager notificationManager;
    public static OyVeyLoader oyVeyLoader;
    @Mod.Instance
    public static OyVey INSTANCE;
    private static boolean unloaded;

    public static void load() {
        LOGGER.info("\n\nLoading Xulu+ by ddevil69gfg");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }
        totemPopManager = new TotemPopManager();
        timerManager = new TimerManager();
        textManager = new TextManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        packetManager = new PacketManager();
        eventManager = new EventManager();
        speedManager = new SpeedManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        serverManager = new ServerManager();
        fileManager = FileManager.getInstance();
        colorManager = new ColorManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager();
        holeManager = new HoleManager();
        LOGGER.info("Managers loaded.");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        LOGGER.info("Config loaded.");
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        LOGGER.info("TextManager loaded.");
        moduleManager.onLoad();
        LOGGER.info("ModuleManager loaded.");
        LOGGER.info("Xulu+ successfully loaded!\n");
    }

    public static void unload(boolean unload) {
        LOGGER.info("\n\nUnloading Xulu+ ):");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }
        OyVey.onUnload();
        timerManager = null;
        eventManager = null;
        friendManager = null;
        speedManager = null;
        holeManager = null;
        positionManager = null;
        rotationManager = null;
        configManager = null;
        commandManager = null;
        colorManager = null;
        serverManager = null;
        fileManager = null;
        potionManager = null;
        inventoryManager = null;
        moduleManager = null;
        textManager = null;
        LOGGER.info("Xulu+ unloaded!\n");
    }

    public static void reload() {
        OyVey.unload(false);
        OyVey.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(OyVey.configManager.config.replaceFirst("Xuluplus/", ""));
            moduleManager.onUnloadPost();
            unloaded = true;
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("CLICKED ON EZ-Ele");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle((String)"Xulu+ | 1.0.6-DEV");
        OyVey.load();
    }

    static {
        unloaded = false;
    }
}

