package studio.semicolon.prc.core.machine.furnace;

import io.quill.paper.item.ItemMatcher;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.core.constant.item.machine.FurnaceMachineItems;
import studio.semicolon.prc.core.constant.item.machine.GrinderMachineItems;

import java.util.List;

public enum FurnaceRecipe {
    MG(FurnaceMachineItems.MG_RECIPE, FurnaceMachineItems.MG_INGOT, 7, 5,
            List.of(GrinderMachineItems.MG_POWDER)),
    AL(FurnaceMachineItems.AL_RECIPE, FurnaceMachineItems.AL_INGOT, 7, 5,
            List.of(GrinderMachineItems.AL_POWDER)),
    FE(FurnaceMachineItems.FE_RECIPE, FurnaceMachineItems.FE_INGOT, 7, 5,
            List.of(GrinderMachineItems.FE_POWDER)),
    CU(FurnaceMachineItems.CU_RECIPE, FurnaceMachineItems.CU_INGOT, 7, 5,
            List.of(GrinderMachineItems.CU_POWDER)),
    LI(FurnaceMachineItems.LI_RECIPE, FurnaceMachineItems.LI_INGOT, 7, 5,
            List.of(GrinderMachineItems.LI_POWDER)),
    AU(FurnaceMachineItems.AU_RECIPE, FurnaceMachineItems.AU_INGOT, 7, 5,
            List.of(GrinderMachineItems.AU_POWDER)),
    PT(FurnaceMachineItems.PT_RECIPE, FurnaceMachineItems.PT_INGOT, 7, 5,
            List.of(GrinderMachineItems.PT_POWDER)),
    NI(FurnaceMachineItems.NI_RECIPE, FurnaceMachineItems.NI_INGOT, 7, 5,
            List.of(GrinderMachineItems.NI_POWDER)),
    TI(FurnaceMachineItems.TI_RECIPE, FurnaceMachineItems.TI_INGOT, 7, 5,
            List.of(GrinderMachineItems.TI_POWDER)),

    AL_MG(FurnaceMachineItems.AL_MG_RECIPE, FurnaceMachineItems.AL_MG_INGOT, 12, 9,
            List.of(GrinderMachineItems.AL_POWDER, GrinderMachineItems.MG_POWDER)),
    AL_CU(FurnaceMachineItems.AL_CU_RECIPE, FurnaceMachineItems.AL_CU_INGOT, 12, 9,
            List.of(GrinderMachineItems.AL_POWDER, GrinderMachineItems.CU_POWDER)),
    AL_LI(FurnaceMachineItems.AL_LI_RECIPE, FurnaceMachineItems.AL_LI_INGOT, 12, 9,
            List.of(GrinderMachineItems.AL_POWDER, GrinderMachineItems.LI_POWDER)),
    CU_AU(FurnaceMachineItems.CU_AU_RECIPE, FurnaceMachineItems.CU_AU_INGOT, 12, 9,
            List.of(GrinderMachineItems.CU_POWDER, GrinderMachineItems.AU_POWDER)),
    NI_FE(FurnaceMachineItems.NI_FE_RECIPE, FurnaceMachineItems.NI_FE_INGOT, 12, 9,
            List.of(GrinderMachineItems.NI_POWDER, GrinderMachineItems.FE_POWDER)),

    TI_PT_AU(FurnaceMachineItems.TI_PT_AU_RECIPE, FurnaceMachineItems.TI_PT_AU_INGOT, 15, 11,
            List.of(GrinderMachineItems.TI_POWDER, GrinderMachineItems.PT_POWDER, GrinderMachineItems.AU_POWDER));

    private final ItemStack recipe;
    private final ItemStack result;
    private final int duration;
    private final int upgradedDuration;
    private final List<ItemStack> requiredPowders;

    FurnaceRecipe(ItemStack recipe, ItemStack result, int duration, int upgradedDuration, List<ItemStack> requiredPowders) {
        this.recipe = recipe;
        this.result = result;
        this.duration = duration;
        this.upgradedDuration = upgradedDuration;
        this.requiredPowders = requiredPowders;
    }

    public ItemStack getRecipe() {
        return recipe;
    }

    public ItemStack getResult() {
        return result.clone();
    }

    public int getDuration(boolean hasTorch) {
        return hasTorch ? upgradedDuration : duration;
    }

    public List<ItemStack> getRequiredPowders() {
        return requiredPowders;
    }

    public static FurnaceRecipe fromRecipeItem(ItemStack item) {
        for (FurnaceRecipe recipe : values()) {
            if (ItemMatcher.matches(item, recipe.recipe)) {
                return recipe;
            }
        }
        return null;
    }
}