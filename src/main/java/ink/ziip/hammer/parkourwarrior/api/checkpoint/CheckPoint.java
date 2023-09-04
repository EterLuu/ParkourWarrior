package ink.ziip.hammer.parkourwarrior.api.checkpoint;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.World;

@Data
@Builder
public class CheckPoint {

    private Integer smallCheckpoint;
    private Integer bigCheckpoint;
    private double x, y, z;
    private float yaw;
    private World world;
    private int range;

    public boolean isCheckPoint(Location location) {
        if (location.getWorld() == null)
            return false;

        if (!world.getName().equals(location.getWorld().getName()))
            return false;

        if (!(Math.abs(location.getX() - x) < range))
            return false;

        if (location.getY() < 75)
            return false;


        return (Math.abs(location.getZ() - z) < range);
    }

    public Location getLocation() {
        return new Location(world, x, y, z, yaw, 0);
    }
}
