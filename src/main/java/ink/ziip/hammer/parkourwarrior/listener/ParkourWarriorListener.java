package ink.ziip.hammer.parkourwarrior.listener;

import ink.ziip.hammer.parkourwarrior.api.listener.BaseListener;
import ink.ziip.hammer.parkourwarrior.api.user.User;
import ink.ziip.hammer.parkourwarrior.manager.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class ParkourWarriorListener extends BaseListener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        User.hideAndShowPlayer(event.getPlayer());

        User user = User.getUser(event.getPlayer());

        if (!user.getInGame()) {
            user.teleport(ConfigManager.spawnLocation);
            user.setLevel(0);
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        User user = User.getUser(event.getPlayer());

        if (user.getInGame()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerHungry(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        User user = User.getUser(event.getPlayer());
        if (!user.getInGame()) {
            if (event.getPlayer().getLocation().getY() < 20) {
                user.teleport(ConfigManager.spawnLocation);
            }
            return;
        }

        if (user.getTimer() > 600) {
            event.setCancelled(true);
            return;
        }

        if (event.getPlayer().getLocation().getY() < 50) {
            user.stuck(false);
            return;
        }

        user.updateCheckPoint(event.getPlayer().getLocation());
    }
}
