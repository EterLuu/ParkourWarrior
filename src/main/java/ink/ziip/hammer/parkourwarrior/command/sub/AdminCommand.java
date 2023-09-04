package ink.ziip.hammer.parkourwarrior.command.sub;

import ink.ziip.hammer.parkourwarrior.ParkourWarrior;
import ink.ziip.hammer.parkourwarrior.api.command.BaseSubCommand;
import ink.ziip.hammer.parkourwarrior.api.user.User;
import ink.ziip.hammer.parkourwarrior.util.Utils;
import ink.ziip.hammer.teams.api.object.Team;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdminCommand extends BaseSubCommand {

    private final String[] commandList = new String[]{
            "join",
            "joinall"
    };

    public AdminCommand() {
        super("admin");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return true;
        }
        if (args[0].equals("join")) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player != null) {
                User user = User.getUser(player);
                user.startGame();
            } else
                sender.sendMessage(Utils.translateColorCodes("&6[Parkour Warrior] &c玩家不在线。"));
        }
        if (args[0].equals("joinall")) {

            new BukkitRunnable() {

                private int count = 6;

                @Override
                public void run() {

                    if (count > 1) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            User user = User.getUser(player);
                            user.sendTitle("&b[Parkour Warrior]", "&c游戏即将开始：&6" + (count - 1));
                        }
                    }


                    if (count == 1) {
                        Team.getTeamList().forEach(team -> {
                            team.getMemberNames().forEach(name -> {
                                Player player = Bukkit.getPlayer(name);
                                if (player != null) {
                                    User user = User.getUser(player);
                                    user.startGame();
                                } else {
                                    sender.sendMessage(Utils.translateColorCodes("&6[Parkour Warrior] &c玩家 " + name + " 不在线。"));
                                }
                            });
                        });

                    }

                    if (count == 0) {
                        cancel();
                    }

                    count--;
                }
            }.runTaskTimer(ParkourWarrior.getInstance(), 0L, 20L);
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            List<String> returnList = Arrays.asList(commandList);
            try {
                returnList.removeIf(s -> !s.startsWith(args[0]));
            } catch (Exception ignored) {
            }
            return returnList;
        }
        if (args[0].equals("join") && args.length == 2) {
            List<String> returnList = new java.util.ArrayList<>(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
            try {
                returnList.removeIf(s -> !s.startsWith(args[1]));
            } catch (Exception ignored) {
            }
            return returnList;
        }

        return Collections.singletonList("");
    }
}
