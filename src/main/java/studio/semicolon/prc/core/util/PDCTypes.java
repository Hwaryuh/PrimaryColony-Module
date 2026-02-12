package studio.semicolon.prc.core.util;

import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public final class PDCTypes {
    private PDCTypes() {
        throw new AssertionError("Cannot instantiate");
    }

    public static void writeLocation(PersistentDataContainer pdc, NamespacedKey key, Location loc) {
        PersistentDataContainer container = pdc.getAdapterContext().newPersistentDataContainer();

        container.set(PDCKeys.of("w"), PersistentDataType.STRING, loc.getWorld().getName());
        container.set(PDCKeys.of("x"), PersistentDataType.DOUBLE, loc.getX());
        container.set(PDCKeys.of("y"), PersistentDataType.DOUBLE, loc.getY());
        container.set(PDCKeys.of("z"), PersistentDataType.DOUBLE, loc.getZ());
        container.set(PDCKeys.of("yaw"), PersistentDataType.FLOAT, loc.getYaw());
        container.set(PDCKeys.of("pitch"), PersistentDataType.FLOAT, loc.getPitch());

        pdc.set(key, PersistentDataType.TAG_CONTAINER, container);
    }

    public static Optional<Location> readLocation(PersistentDataContainer pdc, NamespacedKey key) {
        PersistentDataContainer container = pdc.get(key, PersistentDataType.TAG_CONTAINER);
        if (container == null) return Optional.empty();

        String worldName = container.get(PDCKeys.of("w"), PersistentDataType.STRING);
        if (worldName == null) return Optional.empty();

        World world = Bukkit.getWorld(worldName);
        if (world == null) return Optional.empty();

        Double x = container.get(PDCKeys.of("x"), PersistentDataType.DOUBLE);
        Double y = container.get(PDCKeys.of("y"), PersistentDataType.DOUBLE);
        Double z = container.get(PDCKeys.of("z"), PersistentDataType.DOUBLE);
        Float yaw = container.get(PDCKeys.of("yaw"), PersistentDataType.FLOAT);
        Float pitch = container.get(PDCKeys.of("pitch"), PersistentDataType.FLOAT);

        if (x == null || y == null || z == null || yaw == null || pitch == null) {
            return Optional.empty();
        }

        return Optional.of(new Location(world, x, y, z, yaw, pitch));
    }
}