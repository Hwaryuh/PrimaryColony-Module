package studio.semicolon.prc.api.machine;

import com.google.common.collect.Maps;
import io.quill.paper.Bootable;
import io.quill.paper.util.bukkit.Locations;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import studio.semicolon.prc.core.machine.coffee.CoffeeMachine;
import studio.semicolon.prc.core.machine.furnace.FurnaceMachine;
import studio.semicolon.prc.core.machine.grinder.GrinderMachine;
import studio.semicolon.prc.core.machine.print.PrintMachine;

import java.util.Map;
import java.util.UUID;

public class MachineManager implements Bootable {
    private static MachineManager instance;
    private final Map<Location, AbstractMachine> machines = Maps.newHashMap();
    private final Map<Location, UUID> locks = Maps.newHashMap();

    @Override
    public void start(JavaPlugin plugin) {
        instance = this;
    }

    @Override
    public void end(JavaPlugin plugin) {
        machines.clear();
        locks.clear();
        instance = null;
    }

    public void register(Location location, AbstractMachine machine) {
        machines.put(location, machine);
    }

    public void unregister(Location location) {
        machines.remove(location);
        locks.remove(location);
    }

    public AbstractMachine getMachine(Location location) {
        if (machines.containsKey(location)) {
            return machines.get(location);
        }

        AbstractMachine machine = loadFromPDC(location);
        if (machine != null) {
            machines.put(location, machine);
            return machine;
        }

        Location below = Locations.offset(location, 0, -1, 0);
        if (machines.containsKey(below)) {
            return machines.get(below);
        }

        machine = loadFromPDC(below);
        if (machine != null) {
            machines.put(below, machine);
        }

        return machine;
    }

    public boolean hasMachine(Location location) {
        return getMachine(location) != null;
    }

    // PDC Loading

    private AbstractMachine loadFromPDC(Location location) {
        Chunk chunk = location.getChunk();
        PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();

        if (!chunkPDC.has(PDCKeys.of(MachineKeys.CHUNK_DATA), PersistentDataType.TAG_CONTAINER)) return null;

        PersistentDataContainer machineMap = chunkPDC.get(PDCKeys.of(MachineKeys.CHUNK_DATA), PersistentDataType.TAG_CONTAINER);
        if (machineMap == null) return null;

        String locKey = MachineKeys.locationKey(location);
        if (!machineMap.has(PDCKeys.of(locKey), PersistentDataType.TAG_CONTAINER)) return null;

        PersistentDataContainer machinePDC = machineMap.get(PDCKeys.of(locKey), PersistentDataType.TAG_CONTAINER);
        if (machinePDC == null) return null;

        String machineType = machinePDC.get(PDCKeys.of(MachineKeys.TYPE), PersistentDataType.STRING);
        if (machineType == null) return null;

        String uuidStr = machinePDC.get(PDCKeys.of(MachineKeys.DISPLAY_UUID), PersistentDataType.STRING);
        UUID displayUUID = uuidStr != null ? UUID.fromString(uuidStr) : null;

        return createMachineInstance(machineType, location, displayUUID);
    }

    private AbstractMachine createMachineInstance(String machineType, Location location, UUID displayUUID) {
        AbstractMachine machine = switch (machineType) {
            case "coffee_machine" -> new CoffeeMachine(location);
            case "print_machine" -> new PrintMachine(location);
            case "furnace_machine" -> new FurnaceMachine(location);
            case "grinder_machine" -> new GrinderMachine(location);
            default -> null;
        };

        if (machine != null) {
            machine.displayUUID = displayUUID;
        }

        return machine;
    }


    // Lock

    public boolean tryLock(Location location, UUID uuid) {
        if (locks.containsKey(location)) {
            return false;
        }
        locks.put(location, uuid);
        return true;
    }

    public void unlock(Location location) {
        locks.remove(location);
    }

    public boolean isLocked(Location location) {
        return locks.containsKey(location);
    }

    public UUID getLocker(Location location) {
        return locks.get(location);
    }

    public static MachineManager getInstance() {
        return instance;
    }
}