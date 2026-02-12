package studio.semicolon.prc.core.machine.print;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import studio.semicolon.prc.api.machine.AbstractMachine;
import studio.semicolon.prc.api.machine.MachineMenu;
import studio.semicolon.prc.api.machine.MachineState;
import studio.semicolon.prc.core.constant.item.machine.MachineItems;
import studio.semicolon.prc.core.constant.sound.PRCSounds;
import studio.semicolon.prc.core.constant.text.MenuTitles;

import java.util.List;

public class PrintMachine extends AbstractMachine {
    private static final String MACHINE_TYPE = "print_machine";

    public PrintMachine(Location location) {
        super(location, MACHINE_TYPE);
    }

    // 페이지로 관리되는 Print Machine의 경우 사용되지 않음
    @Override
    public Component getTitleForState(MachineState.State state) {
        return switch (state) {
            case IDLE -> MenuTitles.PRINT_MACHINE_SELECTION;
            case PROCESSING, COMPLETED -> MenuTitles.PRINT_MACHINE_PROCESSING;
        };
    }

    @Override
    protected Material getDisplayMaterial() {
        return MachineItems.PRINT_MACHINE.getType();
    }

    @Override
    protected int getCustomModelData() {
        return MachineItems.PRINT_MACHINE.getItemMeta().getCustomModelData();
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
        return PRCSounds.PRINT_MACHINE_START;
    }

    @Override
    public MachineMenu createMenu(Player player) {
        return new PrintMachineMenu(player, this);
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> drops = Lists.newArrayList();

        drops.add(MachineItems.PRINT_MACHINE.clone());

        MachineState state = loadState();
        if (state.isCompleted()) {
            String recipeID = state.getProcessResult();
            PrintRecipe recipe = PrintRecipe.getByID(recipeID);

            if (recipe != null) {
                drops.add(recipe.getResult());
            }
        }

        return drops;
    }
}