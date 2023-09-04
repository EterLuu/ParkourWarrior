package ink.ziip.hammer.parkourwarrior.manager;

import ink.ziip.hammer.parkourwarrior.ParkourWarrior;
import ink.ziip.hammer.parkourwarrior.api.listener.BaseListener;
import ink.ziip.hammer.parkourwarrior.api.manager.BaseManager;
import ink.ziip.hammer.parkourwarrior.listener.ParkourWarriorListener;
import org.bukkit.event.HandlerList;

public class ListenerManager extends BaseManager {

    private final BaseListener parkourWarriorListener;

    public ListenerManager() {
        parkourWarriorListener = new ParkourWarriorListener();
    }

    @Override
    public void load() {

        parkourWarriorListener.register();

    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(ParkourWarrior.getInstance());
    }

    @Override
    public void reload() {

    }
}
