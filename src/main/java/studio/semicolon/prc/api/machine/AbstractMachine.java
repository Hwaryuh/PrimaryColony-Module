package studio.semicolon.prc.api.machine;

import io.quill.paper.util.bukkit.Locations;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import studio.semicolon.prc.api.constant.text.MachineMessages;
import studio.semicolon.prc.api.constant.sound.PRCSounds;

import java.util.List;
import java.util.UUID;

public abstract class AbstractMachine {
    protected final Location location;
    protected final String machineType;
    protected UUID displayUUID;

    public AbstractMachine(Location location, String machineType) {
        this.location = location;
        this.machineType = machineType;
    }

    public Location getLocation() {
        return location;
    }

    public String getMachineType() {
        return machineType;
    }

    protected abstract Material getDisplayMaterial();

    protected abstract int getCustomModelData();

    protected abstract Vector3f getScale();

    protected abstract List<Vector> getBarrierOffsets();

    protected abstract Vector getDisplayOffset();

    protected abstract PRCSounds.SoundData getCraftingStartSound();

    public abstract List<ItemStack> getDrops();

    public final boolean place(Player player) {
        for (Vector offset : getBarrierOffsets()) {
            Locations.offset(location, offset).getBlock().setType(Material.BARRIER);
        }

        this.displayUUID = createDisplay(player);

        MachineManager.getInstance().register(location, this);

        MachineState state = new MachineState();
        PersistentDataContainer pdc = getPDC();
        state.save(pdc);

        pdc.set(PDCKeys.of(MachineKeys.TYPE), PersistentDataType.STRING, machineType);
        pdc.set(PDCKeys.of(MachineKeys.DISPLAY_UUID), PersistentDataType.STRING, displayUUID.toString());

        savePDC(pdc);
        onPlaced(player);

        return true;
    }

    protected void onPlaced(Player player) {
        PRCSounds.MACHINE_PLACE.play(location);
        player.swingMainHand();
    }

    private UUID createDisplay(Player player) {
        float yaw = player.getLocation().getYaw() % 360;
        if (yaw < 0) yaw += 360;
        float rotation = ((Math.round(yaw / 90) * 90) + 180) % 360;

        Location loc = Locations.offset(location, getDisplayOffset());
        ItemDisplay display = loc.getWorld().spawn(loc, ItemDisplay.class, entity -> {
            ItemStack item = ItemStack.of(getDisplayMaterial());
            item.editMeta(meta -> meta.setCustomModelData(getCustomModelData()));

            entity.setItemStack(item);
            entity.setBillboard(Display.Billboard.FIXED);
            entity.setRotation(rotation, 0f);
            entity.setTransformation(new Transformation(
                    new Vector3f(),
                    new AxisAngle4f(),
                    getScale(),
                    new AxisAngle4f()
            ));
        });

        return display.getUniqueId();
    }

    // State

    public MachineState loadState() {
        PersistentDataContainer pdc = getPDC();
        return MachineState.load(pdc);
    }

    public void saveState(MachineState state) {
        PersistentDataContainer pdc = getPDC();
        state.save(pdc);
        savePDC(pdc);
    }

    protected final PersistentDataContainer getPDC() {
        PersistentDataContainer machineMap = getMachineMap();
        String locKey = MachineKeys.locationKey(location);

        if (machineMap.has(PDCKeys.of(locKey), PersistentDataType.TAG_CONTAINER)) {
            return machineMap.get(PDCKeys.of(locKey), PersistentDataType.TAG_CONTAINER);
        }
        return machineMap.getAdapterContext().newPersistentDataContainer();
    }

    protected final void savePDC(PersistentDataContainer pdc) {
        PersistentDataContainer machineMap = getMachineMap();
        String locKey = MachineKeys.locationKey(location);
        machineMap.set(PDCKeys.of(locKey), PersistentDataType.TAG_CONTAINER, pdc);
        saveMachineMap(machineMap);
    }

    protected final void destroyPDC() {
        PersistentDataContainer machineMap = getMachineMap();
        String locKey = MachineKeys.locationKey(location);

        if (machineMap.has(PDCKeys.of(locKey), PersistentDataType.TAG_CONTAINER)) {
            machineMap.remove(PDCKeys.of(locKey));
            saveMachineMap(machineMap);
        }
    }

    private PersistentDataContainer getMachineMap() {
        Chunk chunk = location.getChunk();
        PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();

        if (chunkPDC.has(PDCKeys.of(MachineKeys.CHUNK_DATA), PersistentDataType.TAG_CONTAINER)) {
            return chunkPDC.get(PDCKeys.of(MachineKeys.CHUNK_DATA), PersistentDataType.TAG_CONTAINER);
        }
        return chunkPDC.getAdapterContext().newPersistentDataContainer();
    }

    private void saveMachineMap(PersistentDataContainer machineMap) {
        Chunk chunk = location.getChunk();
        PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();
        chunkPDC.set(PDCKeys.of(MachineKeys.CHUNK_DATA), PersistentDataType.TAG_CONTAINER, machineMap);
    }

    // Menu

    public abstract MachineMenu createMenu(Player player);

    public abstract Component getTitleForState(MachineState.State state);

    public void openMenu(Player player) {
        MachineManager manager = MachineManager.getInstance();

        if (!manager.tryLock(location, player.getUniqueId())) {
            UUID locker = manager.getLocker(location);
            Player lockerPlayer = player.getServer().getPlayer(locker);

            if (lockerPlayer != null) {
                player.sendMessage(MachineMessages.INTERACT_LOCKED);
                PRCSounds.GLOBAL_ERROR.play(player);
            } else {
                manager.unlock(location);
                manager.tryLock(location, player.getUniqueId());
                MachineMenu menu = createMenu(player);
                menu.open();
            }
            return;
        }

        MachineMenu menu = createMenu(player);
        menu.open();
    }

    // Destruction

    public boolean canDestroy() {
        return !loadState().isProcessing();
    }

    public void destroy() {
        if (displayUUID != null) {
            Entity entity = Bukkit.getEntity(displayUUID);
            if (entity != null) entity.remove();
        }

        for (Vector offset : getBarrierOffsets()) {
            Locations.offset(location, offset).getBlock().setType(Material.AIR);
        }

        this.destroyPDC();
        PRCSounds.MACHINE_DESTROY.play(location);
    }
}