package ink.ziip.hammer.parkourwarrior.command.sub;

import ink.ziip.hammer.parkourwarrior.api.command.BaseSubCommand;
import ink.ziip.hammer.parkourwarrior.api.user.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class StuckCommand extends BaseSubCommand {

    public StuckCommand() {
        super("stuck");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        User user = User.getUser(sender);

        user.stuck(true);

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.singletonList("");
    }
}
