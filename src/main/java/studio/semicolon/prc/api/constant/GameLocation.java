package studio.semicolon.prc.api.constant;

import io.quill.paper.util.bukkit.Locations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.World;

public enum GameLocation {
    HOME_MODULE(176.5, -34.0, 160.5, -45.0F, 0F, null),

    TUTORIAL_HOME_MODULE(591.0, -36.0, -417.5, 0F, 0F, Component.text("[홈 모듈]", TextColor.color(250, 130, 55))),
    TUTORIAL_MINE(589.5, -36.0, -398.5, -90.0F, 0F, Component.text("[광산/광물]", TextColor.color(250, 130, 55))),
    TUTORIAL_FARM(592.5, -36.0, -390.5, 90.0F, 0F, Component.text("[작물/농사]", TextColor.color(250, 130, 55))),
    TUTORIAL_MACHINE(589.5, -36.0, -382.5, -90.0F, 0F, Component.text("[설치형 모듈]", TextColor.color(250, 130, 55))),
    TUTORIAL_EXPLORE(592.5, -36.0, -374.0, 90.0F, 0F, Component.text("[탐사]", TextColor.color(250, 130, 55))),
    TUTORIAL_MODULE(591.0, -36.0, -374.0, 0F, 0F, Component.text("[빌드형 모듈]", TextColor.color(250, 130, 55))),
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

    public Component getLabel() { return label; }
}
