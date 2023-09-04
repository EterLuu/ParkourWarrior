package ink.ziip.hammer.parkourwarrior.task;

import ink.ziip.hammer.parkourwarrior.api.user.User;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class ParkourWarriorTimer extends BukkitRunnable {

    private final User player;
    private boolean started;

    public ParkourWarriorTimer(User player) {
        this.player = player;
        this.started = true;
    }

    @Override
    public void run() {
        if (started) {
            if (player.isOnline()) {
                int timer = player.getTimer();

                player.setLevel(timer);

                if (timer == 5) {
                    player.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 0.5F);
                } else if (timer == 4) {
                    player.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 0.6F);
                } else if (timer == 3) {
                    player.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 0.7F);
                } else if (timer == 2) {
                    player.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 0.8F);
                } else if (timer == 1) {
                    player.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 0.9F);
                }

                if (timer == 0) {
                    player.endGame();
                    stop();
                }

                timer = timer - 1;

                player.setTimer(timer);
            }
        }
    }

    public void stop() {
        started = false;
        cancel();
    }

}
