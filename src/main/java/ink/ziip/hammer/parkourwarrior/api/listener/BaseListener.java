package ink.ziip.hammer.parkourwarrior.api.listener;

import ink.ziip.hammer.parkourwarrior.ParkourWarrior;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class BaseListener implements Listener {

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, ParkourWarrior.getInstance());
    }

    public void unRegister() {
        HandlerList.unregisterAll(this);
    }
}
