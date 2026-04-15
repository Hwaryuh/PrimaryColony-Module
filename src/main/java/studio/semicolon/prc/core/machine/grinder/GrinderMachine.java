package studio.semicolon.prc.core.machine.grinder;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import studio.semicolon.prc.api.machine.MachineMenu;
import studio.semicolon.prc.api.machine.MachineState;
import studio.semicolon.prc.api.constant.item.machine.GrinderMachineItems;
import studio.semicolon.prc.api.constant.item.machine.MachineItems;
import studio.semicolon.prc.api.constant.sound.PRCSounds;
import studio.semicolon.prc.api.constant.text.MenuTitles;
import studio.semicolon.prc.api.machine.UpgradeableMachine;

import java.util.List;

public class GrinderMachine extends UpgradeableMachine {
    private static final String MACHINE_TYPE = "grinder_machine";

    public GrinderMachine(Location location) {
        super(location, MACHINE_TYPE);
    }

    @Override
    public Component getTitleForState(MachineState.State state) {
        return switch (state) {
            case IDLE -> MenuTitles.GRINDER_MACHINE_IDLE;
            case PROCESSING, COMPLETED -> MenuTitles.GRINDER_MACHINE_PROCESSING;
        };
    }

    @Override
    protected Material getDisplayMaterial() {
        return MachineItems.GRINDER_MACHINE.getType();
    }

    @Override
    protected int getCustomModelData() {
        return MachineItems.GRINDER_MACHINE.getItemMeta().getCustomModelData();
    }

    @Override
    protected Vector3f getScale() {
        return new Vector3f(1.0f, 1.0f, 1.0f);
    }

    @Override
    protected List<Vector> getBarrierOffsets() {
        return List.of(new Vector(0, 0, 0));
    }

    @Override
    protected Vector getDisplayOffset() {
        return new Vector(0.5, 0.5, 0.5);
    }

    @Override
    protected PRCSounds.SoundData getCraftingStartSound() {
        return PRCSounds.GRINDER_MACHINE_START;
    }

    @Override
    public MachineMenu createMenu(Player player) {
        return new GrinderMachineMenu(player, this);
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> drops = Lists.newArrayList();
        drops.add(MachineItems.GRINDER_MACHINE.clone());

        MachineState state = loadState();

        if (state.isCompleted()) {
            String processResult = state.getProcessResult();
            for (GrinderRecipe.GrindingResult result : GrinderRecipe.parseResults(processResult)) {
                drops.add(result.recipe().getPowder().asQuantity(result.powderAmount()));
            }
        }

        int level = getUpgradeLevel();
        if (level >= 1) drops.add(GrinderMachineItems.SLOT_EXTENSION.clone());
        if (level >= 2) drops.add(GrinderMachineItems.GEAR.clone());
        if (level >= 3) drops.add(GrinderMachineItems.DRILL.clone());

        return drops;
    }
}
