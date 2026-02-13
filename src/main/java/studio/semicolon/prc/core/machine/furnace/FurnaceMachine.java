package studio.semicolon.prc.core.machine.furnace;

import com.google.common.collect.Lists;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import studio.semicolon.prc.api.machine.AbstractMachine;
import studio.semicolon.prc.api.machine.MachineMenu;
import studio.semicolon.prc.api.machine.MachineState;
import studio.semicolon.prc.api.machine.Upgradeable;
import studio.semicolon.prc.api.constant.item.machine.FurnaceMachineItems;
import studio.semicolon.prc.api.constant.item.machine.MachineItems;
import studio.semicolon.prc.api.constant.sound.PRCSounds;
import studio.semicolon.prc.api.constant.text.MenuTitles;

import java.util.List;

public class FurnaceMachine extends AbstractMachine implements Upgradeable {
    private static final String MACHINE_TYPE = "furnace_machine";

    public FurnaceMachine(Location location) {
        super(location, MACHINE_TYPE);
    }

    @Override
    public Component getTitleForState(MachineState.State state) {
        return switch (state) {
            case IDLE -> MenuTitles.FURNACE_MACHINE_IDLE;
            case PROCESSING, COMPLETED -> MenuTitles.FURNACE_MACHINE_PROCESSING;
        };
    }

    @Override
    protected Material getDisplayMaterial() {
        return MachineItems.FURNACE_MACHINE.getType();
    }

    @Override
    protected int getCustomModelData() {
        return MachineItems.FURNACE_MACHINE.getItemMeta().getCustomModelData();
    }

    @Override
    protected Vector3f getScale() {
        return new Vector3f(1.0f, 1.0f, 1.0f);
    }

    @Override
    protected List<Vector> getBarrierOffsets() {
        return List.of(
                new Vector(0, 0, 0),
                new Vector(0, 1, 0)
        );
    }

    @Override
    protected Vector getDisplayOffset() {
        return new Vector(0.5, 0.5, 0.5);
    }

    @Override
    protected PRCSounds.SoundData getCraftingStartSound() {
        return PRCSounds.FURNACE_MACHINE_START;
    }

    @Override
    public MachineMenu createMenu(Player player) {
        return new FurnaceMachineMenu(player, this);
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> drops = Lists.newArrayList();
        drops.add(MachineItems.FURNACE_MACHINE.clone());

        MachineState state = loadState();

        if (state.isCompleted()) {
            String recipeID = state.getProcessResult();
            if (recipeID != null) {
                try {
                    FurnaceRecipe recipe = FurnaceRecipe.valueOf(recipeID);
                    drops.add(recipe.getResult());
                } catch (IllegalArgumentException ignored) { }
            }
        }

        int level = getUpgradeLevel();
        if (level >= 1) drops.add(FurnaceMachineItems.SLOT_EXTENSION.clone());
        if (level >= 2) drops.add(FurnaceMachineItems.ICE_MOLD.clone());
        if (level >= 3) drops.add(FurnaceMachineItems.TORCH.clone());

        return drops;
    }

    @Override
    public int getUpgradeLevel() {
        var pdc = getPDC();
        return pdc.getOrDefault(PDCKeys.of(KEY_UPGRADE_LEVEL), PersistentDataType.INTEGER, 0);
    }

    @Override
    public void setUpgradeLevel(int level) {
        var pdc = getPDC();
        pdc.set(PDCKeys.of(KEY_UPGRADE_LEVEL), PersistentDataType.INTEGER, level);
        savePDC(pdc);
    }
}