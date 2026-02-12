package studio.semicolon.prc.core.machine.grinder;

import io.quill.paper.item.ItemMatcher;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.core.constant.item.machine.GrinderMachineItems;

public enum GrinderRecipe {
    MG(GrinderMachineItems.MG, GrinderMachineItems.MG_POWDER, new int[]{5, 4, 2}, 0),
    AL(GrinderMachineItems.AL, GrinderMachineItems.AL_POWDER, new int[]{5, 4, 2}, 0),
    FE(GrinderMachineItems.FE, GrinderMachineItems.FE_POWDER, new int[]{7, 6, 4}, 0),
    CU(GrinderMachineItems.CU, GrinderMachineItems.CU_POWDER, new int[]{7, 6, 4}, 0),
    LI(GrinderMachineItems.LI, GrinderMachineItems.LI_POWDER, new int[]{7, 6, 4}, 0),
    AU(GrinderMachineItems.AU, GrinderMachineItems.AU_POWDER, new int[]{-1, 8, 6}, 2),
    PT(GrinderMachineItems.PT, GrinderMachineItems.PT_POWDER, new int[]{-1, 8, 6}, 2),
    NI(GrinderMachineItems.NI, GrinderMachineItems.NI_POWDER, new int[]{-1, 10, 6}, 2),
    TI(GrinderMachineItems.TI, GrinderMachineItems.TI_POWDER, new int[]{-1, 10, 6}, 2);

    private final ItemStack ore;
    private final ItemStack powder;
    private final int[] durations;
    private final int requireLevel;

    GrinderRecipe(ItemStack ore, ItemStack powder, int[] durations, int requireLevel) {
        this.ore = ore;
        this.powder = powder;
        this.durations = durations;
        this.requireLevel = requireLevel;
    }

    public ItemStack getPowder() { return powder.clone(); }

    public int getDuration(int upgradeLevel) {
        if (upgradeLevel == 0) return durations[0];
        if (upgradeLevel == 1) return durations[0];
        if (upgradeLevel == 2) return durations[1];
        return durations[2];
    }

    public boolean canProcess(int upgradeLevel) {
        if (upgradeLevel < requireLevel) return false;
        int duration = durations[Math.min(upgradeLevel, 2)];
        return duration > 0;
    }

    public static GrinderRecipe fromOre(ItemStack item) {
        for (GrinderRecipe recipe : values()) {
            if (ItemMatcher.matches(item, recipe.ore)) {
                return recipe;
            }
        }
        return null;
    }
}