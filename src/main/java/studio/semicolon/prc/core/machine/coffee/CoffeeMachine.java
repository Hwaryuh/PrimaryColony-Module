package studio.semicolon.prc.core.machine.coffee;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import studio.semicolon.prc.api.constant.item.machine.MachineItems;
import studio.semicolon.prc.api.constant.item.machine.CoffeeMachineItems;
import studio.semicolon.prc.api.constant.sound.PRCSounds;
import studio.semicolon.prc.api.machine.MachineMenu;
import studio.semicolon.prc.api.machine.MachineState;
import studio.semicolon.prc.api.machine.AbstractMachine;
import studio.semicolon.prc.api.constant.text.MenuTitles;

import java.util.List;

public class CoffeeMachine extends AbstractMachine {
    private static final String MACHINE_TYPE = "coffee_machine";

    public CoffeeMachine(Location location) {
        super(location, MACHINE_TYPE);
    }

    @Override
    public Component getTitleForState(MachineState.State state) {
        return switch (state) {
            case IDLE, COMPLETED -> MenuTitles.COFFEE_MACHINE_IDLE;
            case PROCESSING -> MenuTitles.COFFEE_MACHINE_PROCESSING;
        };
    }

    @Override
    protected Material getDisplayMaterial() {
        return MachineItems.COFFEE_MACHINE.getType();
    }

    @Override
    protected int getCustomModelData() {
        return MachineItems.COFFEE_MACHINE.getItemMeta().getCustomModelData();
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
        return PRCSounds.COFFEE_MACHINE_START;
    }

    // Menu

    @Override
    public MachineMenu createMenu(Player player) {
        return new CoffeeMachineMenu(player, this);
    }

    // Drops

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> drops = Lists.newArrayList();

        drops.add(MachineItems.COFFEE_MACHINE.clone());
        if (loadState().isCompleted()) {
            drops.add(CoffeeMachineItems.COFFEE.clone());
        }

        return drops;
    }
}