package studio.semicolon.prc.api.constant;

import io.quill.paper.util.bukkit.Locations;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import studio.semicolon.prc.api.constant.text.UtilMessages;

public enum GameLocation {
    HOME_MODULE(176.5, -34.0, 160.5, -45.0F, 0F, null),

    TUTORIAL_HOME_MODULE(591.0, -36.0, -417.5, 0F, 0F, UtilMessages.TUTORIAL_HOME_MODULE),
    TUTORIAL_MINE(589.5, -36.0, -398.5, -90.0F, 0F, UtilMessages.TUTORIAL_MINE),
    TUTORIAL_FARM(592.5, -36.0, -390.5, 90.0F, 0F, UtilMessages.TUTORIAL_FARM),
    TUTORIAL_MACHINE(589.5, -36.0, -382.5, -90.0F, 0F, UtilMessages.TUTORIAL_MACHINE),
    TUTORIAL_EXPLORE(592.5, -36.0, -374.0, 90.0F, 0F, UtilMessages.TUTORIAL_EXPLORE),
    TUTORIAL_MODULE(591.0, -36.0, -374.0, 0F, 0F, UtilMessages.TUTORIAL_MODULE),
    ;

    private final double x, y, z;
    private final float yaw, pitch;
    private final Component label;

    GameLocation(double x, double y, double z, float yaw, float pitch, Component label) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.label = label;
    }

    public Location toLocation(World world) {
        return Locations.of(world, x, y, z, yaw, pitch);
    }

    public Component getLabel() {
        return label;
    }
}
