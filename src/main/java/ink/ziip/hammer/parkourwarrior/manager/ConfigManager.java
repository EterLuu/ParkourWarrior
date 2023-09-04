package ink.ziip.hammer.parkourwarrior.manager;

import ink.ziip.hammer.parkourwarrior.ParkourWarrior;
import ink.ziip.hammer.parkourwarrior.api.checkpoint.CheckPoint;
import ink.ziip.hammer.parkourwarrior.api.manager.BaseManager;
import ink.ziip.hammer.parkourwarrior.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigManager extends BaseManager {

    private final FileConfiguration config;

    public static Integer integralSmallCheckPoint;
    public static Integer integralBigCheckPoint;
    public static Integer integralAllCheckPoint;

    public static Location spawnLocation;

    public static List<CheckPoint> checkPoints = new ArrayList<>();

    public ConfigManager() {
        config = ParkourWarrior.getInstance().getConfig();
    }

    @Override
    public void load() {
        loadConfig();
    }

    @Override
    public void unload() {

    }

    @Override
    public void reload() {
        ParkourWarrior.getInstance().reloadConfig();
        load();
    }

    private void loadConfig() {
        integralSmallCheckPoint = config.getInt("integral.small-checkpoint");
        integralAllCheckPoint = config.getInt("integral.all-checkpoint");
        integralBigCheckPoint = config.getInt("integral.big-checkpoint");

        List<String> checkPointsList = config.getStringList("checkpoints");
        checkPointsList.forEach(checkPointString -> {
            String[] strings = Utils.splitList(checkPointString);
            checkPoints.add(
                    CheckPoint.builder()
                            .bigCheckpoint(Integer.valueOf(strings[0]))
                            .smallCheckpoint(Integer.valueOf(strings[1]))
                            .world(Bukkit.getWorld(strings[2]))
                            .x(Double.parseDouble(strings[3]))
                            .y(Double.parseDouble(strings[4]))
                            .z(Double.parseDouble(strings[5]))
                            .yaw(Float.parseFloat(strings[6]))
                            .range(Integer.parseInt(strings[7]))
                            .build());
        });

        World world = Bukkit.getWorld(Objects.requireNonNull(config.getString("spawn.world")));
        double x, y, z;
        float yaw, pitch;
        x = Double.parseDouble(Objects.requireNonNull(config.getString("spawn.x")));
        y = Double.parseDouble(Objects.requireNonNull(config.getString("spawn.y")));
        z = Double.parseDouble(Objects.requireNonNull(config.getString("spawn.z")));
        yaw = Float.parseFloat(Objects.requireNonNull(config.getString("spawn.yaw")));
        pitch = Float.parseFloat(Objects.requireNonNull(config.getString("spawn.pitch")));

        spawnLocation = new Location(world, x, y, z, yaw, pitch);
    }
}
