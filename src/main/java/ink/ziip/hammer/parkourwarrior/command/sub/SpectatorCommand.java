package ink.ziip.hammer.parkourwarrior.command.sub;

import ink.ziip.hammer.parkourwarrior.api.command.BaseSubCommand;
import ink.ziip.hammer.parkourwarrior.api.user.User;
import ink.ziip.hammer.parkourwarrior.manager.ConfigManager;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class SpectatorCommand extends BaseSubCommand {

    public SpectatorCommand() {
        super("spectator");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        User user = User.getUser(sender);

        Player player = user.getPlayer();

        if (player != null && !user.getInGame() && user.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            user.sendMessage("&6[Parkour Warrior] &c退出旁观。");
            user.teleport(ConfigManager.spawnLocation);
            player.setGameMode(GameMode.ADVENTURE);
            return true;
        }
        if (player != null && !user.getInGame() && user.getPlayer().getGameMode() == GameMode.ADVENTURE) {
            user.sendMessage("&6[Parkour Warrior] &c旁观游戏。");
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(ConfigManager.checkPoints.get(0).getLocation());
            return true;
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.singletonList("");
    }
}
