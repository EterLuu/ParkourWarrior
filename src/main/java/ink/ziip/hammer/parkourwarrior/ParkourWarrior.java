package ink.ziip.hammer.parkourwarrior;

import ink.ziip.hammer.parkourwarrior.manager.CommandManager;
import ink.ziip.hammer.parkourwarrior.manager.ConfigManager;
import ink.ziip.hammer.parkourwarrior.manager.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParkourWarrior extends JavaPlugin {

    private static ParkourWarrior instance;

    private CommandManager commandManager;
    private ConfigManager configManager;
    private ListenerManager listenerManager;

    public ParkourWarrior() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();

        configManager = new ConfigManager();
        commandManager = new CommandManager();
        listenerManager = new ListenerManager();

        commandManager.load();
        listenerManager.load();
        configManager.load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        commandManager.unload();
        listenerManager.unload();
        configManager.unload();
    }

    public static ParkourWarrior getInstance() {
        return instance;
    }
}
