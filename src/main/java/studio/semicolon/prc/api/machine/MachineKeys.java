package studio.semicolon.prc.api.machine;

import org.bukkit.Location;

public final class MachineKeys {
    private MachineKeys() { }

    public static final String CHUNK_DATA = "chunk_machine_data";
    public static final String TYPE = "machine_type";
    public static final String DISPLAY_UUID = "display_uuid";

    public static String locationKey(Location loc) {
        return String.format("loc_%s_%d_%d_%d",
                loc.getWorld().getName(),
                loc.getBlockX(),
                loc.getBlockY(),
                loc.getBlockZ());
    }
}