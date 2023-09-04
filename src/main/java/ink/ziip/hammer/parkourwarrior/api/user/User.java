package ink.ziip.hammer.parkourwarrior.api.user;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import ink.ziip.hammer.parkourwarrior.ParkourWarrior;
import ink.ziip.hammer.parkourwarrior.api.checkpoint.CheckPoint;
import ink.ziip.hammer.parkourwarrior.manager.ConfigManager;
import ink.ziip.hammer.parkourwarrior.task.ParkourWarriorTimer;
import ink.ziip.hammer.parkourwarrior.util.Utils;
import ink.ziip.hammer.teams.api.object.GameTypeEnum;
import ink.ziip.hammer.teams.api.object.Team;
import ink.ziip.hammer.teams.manager.TeamRecordManager;
import lombok.Data;
import lombok.NonNull;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class User {

    private static final Map<UUID, User> users = new ConcurrentHashMap<>();

    private final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    private final UUID playerUUID;
    private final CommandSender commandSender;

    private User(@NonNull UUID playerUUID) {
        this.playerUUID = playerUUID;
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
        this.commandSender = offlinePlayer.isOnline() ? offlinePlayer.getPlayer() : null;
        users.put(this.playerUUID, this);
    }

    private User(@NonNull Player player) {
        this.playerUUID = player.getUniqueId();
        this.commandSender = player;
        users.put(this.playerUUID, this);
    }

    private User(@NonNull CommandSender commandSender) {
        this.commandSender = commandSender;
        Player player = Bukkit.getPlayer(commandSender.getName());
        this.playerUUID = player != null ? player.getUniqueId() : null;
        users.put(this.playerUUID, this);
    }

    public static User getUser(@NonNull Player player) {
        if (users.containsKey(player.getUniqueId())) {
            return users.get(player.getUniqueId());
        }
        return new User(player);
    }

    public static User getUser(@NonNull UUID uuid) {
        if (users.containsKey(uuid)) {
            return users.get(uuid);
        }
        return new User(uuid);
    }

    public static User getUser(@NonNull CommandSender commandSender) {
        if (commandSender instanceof Player)
            return getUser(((Player) commandSender).getUniqueId());

        return new User(commandSender);
    }

    public static void removeHammerUser(@NonNull Player player) {
        users.remove(player.getUniqueId());
    }

    public static void removeHammerUser(@NonNull OfflinePlayer offlinePlayer) {
        users.remove(offlinePlayer.getUniqueId());
    }

    public static void removeHammerUser(@NonNull UUID uuid) {
        users.remove(uuid);
    }

    public static void cleanUsers() {
        users.clear();
    }

    public static void hideAndShowPlayer(Player player) {
        if (getUser(player).inGame) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(ParkourWarrior.getInstance(), onlinePlayer);
            }
        } else {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                player.showPlayer(ParkourWarrior.getInstance(), onlinePlayer);
            }
        }
        for (Map.Entry<UUID, User> userEntry : users.entrySet()) {
            if (userEntry.getValue().inGame) {
                if (userEntry.getValue().getPlayer() != null)
                    userEntry.getValue().getPlayer().hidePlayer(ParkourWarrior.getInstance(), player);
            } else {
                if (userEntry.getValue().getPlayer() != null)
                    userEntry.getValue().getPlayer().showPlayer(ParkourWarrior.getInstance(), player);
            }
        }
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(playerUUID);
    }

    public String setPlaceholders(String content) {
        // Using offlinePlayer to avoid issues
        return Utils.translateColorCodes(PlaceholderAPI.setPlaceholders(getOfflinePlayer(), content));
    }

    public void sendActionBar(String content, boolean filtered) {
        WrappedChatComponent wrappedChatComponent = WrappedChatComponent.fromLegacyText(setPlaceholders(content));
        PacketContainer packetContainer = protocolManager.createPacket(PacketType.Play.Server.SYSTEM_CHAT);
        StructureModifier<Integer> integers = packetContainer.getIntegers();
        if (integers.size() == 1) {
            integers.write(0, (int) EnumWrappers.ChatType.GAME_INFO.getId());
        } else {
            packetContainer.getBooleans().write(0, true);
        }
        packetContainer.getStrings().write(0, wrappedChatComponent.getJson());
        if (filtered) {
            packetContainer.setMeta("signed", true);
        }
        protocolManager.sendServerPacket(getPlayer(), packetContainer);
    }

    public void sendMessage(String content) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) {
            commandSender.sendMessage(Utils.translateColorCodes(setPlaceholders(content)));
            return;
        }

        player.sendMessage(Utils.translateColorCodes(setPlaceholders(content)));
    }

    public void setLevel(int level) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null) {
            player.setLevel(level);
        }
    }

    public boolean isOnline() {
        return Bukkit.getPlayer(playerUUID) != null;
    }

    public void playSound(Sound sound, float volume, float pitch) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void hideOnlinePlayers() {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(ParkourWarrior.getInstance(), onlinePlayer);
            }
        }
    }

    public void showOnlinePlayers() {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                player.showPlayer(ParkourWarrior.getInstance(), onlinePlayer);
            }
        }
    }

    public void sendTitle(String title, String subTitle) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null) {
            player.sendTitle(Utils.translateColorCodes(setPlaceholders(title)), Utils.translateColorCodes(setPlaceholders(subTitle)), 1, 20, 1);
        }
    }

    public void teleport(Location location) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(location);
                }
            }.runTaskLater(ParkourWarrior.getInstance(), 1L);
        }
    }

    private Integer timer = 0;
    private CheckPoint checkPoint;
    private Boolean inGame = false;

    private ParkourWarriorTimer parkourWarriorTimer;

    public static Color hex2Rgb(String colorStr) {
        try {
            return Color.fromBGR(
                    Integer.valueOf(colorStr.substring(1, 3), 16),
                    Integer.valueOf(colorStr.substring(3, 5), 16),
                    Integer.valueOf(colorStr.substring(5, 7), 16));
        } catch (Exception ignored) {
            return Color.fromBGR(0, 0, 0);
        }
    }

    public void startGame() {
        if (!inGame && getPlayer() != null) {
            timer = 600;
            checkPoint = ConfigManager.checkPoints.get(0);
            inGame = true;
            parkourWarriorTimer = new ParkourWarriorTimer(this);
            parkourWarriorTimer.runTaskTimer(ParkourWarrior.getInstance(), 0L, 20L);
            teleport(checkPoint.getLocation());
            ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
            ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            Team team = Team.getTeamByPlayer(getPlayer());
            if (leatherArmorMeta != null && team != null) {
                leatherArmorMeta.setColor(hex2Rgb(team.getColorCode()));
                item.setItemMeta(leatherArmorMeta);
                getPlayer().getInventory().setBoots(item);
            }
            hideOnlinePlayers();
            playSound(Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
            sendTitle("&6[Parkour Warrior]", "&b游戏开始！");
            sendMessage("&6[Parkour Warrior] &b游戏开始，限时10分钟。");
            return;
        }

        sendMessage("&6[Parkour Warrior] &b你已经在游戏里了！");
    }

    public void endGame() {
        if (inGame && getPlayer() != null) {
            sendTitle("&6[Parkour Warrior]", "&b游戏结束！");

            getPlayer().getInventory().clear();

            int points;
            int bigCheckpoints = checkPoint.getBigCheckpoint() - 1 + ((checkPoint.getSmallCheckpoint() % 3) == 0 ? 1 : 0);
            points = bigCheckpoints * 20 + checkPoint.getSmallCheckpoint() * 15;
            if (checkPoint.getSmallCheckpoint() == 31) {
                points = points + 280;
                points = points - 15;
            }

            TeamRecordManager.addPlayerRecord(getPlayer().getName(), Team.getTeamByPlayer(getPlayer()), GameTypeEnum.ParkourWarrior.name(), "default", points, false);

            sendMessage("&6[Parkour Warrior] &b游戏结束，你获得了 " + points + " 积分。");
            ParkourWarrior.getInstance().getLogger().info("&6[Parkour Warrior] &b玩家 " + getPlayer().getName() + "获得了积分：" + points);
            timer = 0;
            checkPoint = ConfigManager.checkPoints.get(0);
            inGame = false;
            parkourWarriorTimer.stop();

            showOnlinePlayers();

            teleport(ConfigManager.spawnLocation);

            setLevel(0);
        }
    }

    public void quitGame() {
        if (inGame && getPlayer() != null) {
            endGame();
            sendTitle("&6[Parkour Warrior]", "&b退出游戏！");
            sendMessage("&6[Parkour Warrior] &b已退出游戏，进度已重置。");
            return;
        }
        sendMessage("&6[Parkour Warrior] &b你尚未加入游戏。");
    }

    public void stuck(boolean show) {
        if (inGame && getPlayer() != null) {
            getPlayer().teleport(checkPoint.getLocation());
            if (show) {
                sendTitle("&6[Parkour Warrior]", "&b返回存档点！");
            }
            sendMessage("&6[Parkour Warrior] &b已带你返回到上一个存档点。");
            return;
        }
        sendMessage("&6[Parkour Warrior] &b你尚未加入游戏。");
    }

    public void updateCheckPoint(Location location) {
        if (inGame && getPlayer() != null) {
            ConfigManager.checkPoints.forEach(newCheckPoints -> {
                if (newCheckPoints.isCheckPoint(location)) {
                    if (newCheckPoints.getBigCheckpoint() > checkPoint.getBigCheckpoint()) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player != getPlayer())
                                player.sendMessage(Utils.translateColorCodes("&6[Parkour Warrior] &c玩家 " + getPlayer().getName() + " &c成功在存档点存档&f（大关卡：" + newCheckPoints.getBigCheckpoint() + "）"));
                        }
                    }
                    if (newCheckPoints.getSmallCheckpoint() > checkPoint.getSmallCheckpoint()) {
                        playSound(Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
                        setCheckPoint(newCheckPoints);
                        sendMessage("&6[Parkour Warrior] &b成功在存档点存档&f（大关卡：" + newCheckPoints.getBigCheckpoint() + "，小关卡：" + newCheckPoints.getSmallCheckpoint() + "）");
                        ParkourWarrior.getInstance().getLogger().info("&6[Parkour Warrior] &b玩家 " + getPlayer().getName() + " &b成功在存档点存档&f（大关卡：" + newCheckPoints.getBigCheckpoint() + "，小关卡：" + newCheckPoints.getSmallCheckpoint() + "）");
                    }
                    if (checkPoint.getSmallCheckpoint().equals(ConfigManager.checkPoints.get(ConfigManager.checkPoints.size() - 1).getSmallCheckpoint())) {
                        endGame();
                    }
                }
            });
            return;
        }
        sendMessage("&6[Parkour Warrior] &b你尚未加入游戏。");
    }
}
