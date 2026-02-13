package studio.semicolon.prc.core.module;

import io.quill.paper.item.require.InventoryRequirement;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.constant.item.module.ModuleItems;
import studio.semicolon.prc.core.module.validate.connect.ConnectRule;

public enum ModuleType {
    DEFAULT(new ModuleConfig(1, ConnectRule.TWO_WAY, 21, "module_default", ModuleItems.DEFAULT)),
    T_SHAPED(new ModuleConfig(1, ConnectRule.THREE_WAY, 22, "module_t_shaped", ModuleItems.T_SHAPED)),
    CROSS_SHAPED(new ModuleConfig(1, ConnectRule.ALL_WAY, 23, "module_cross_shaped", ModuleItems.CROSS_SHAPED)),
    MINE(new ModuleConfig(1, ConnectRule.TWO_WAY, 24, "module_mine", ModuleItems.MINE)),
    ENTRY(new ModuleConfig(1, ConnectRule.FRONT_ONLY, 29, "module_entry", ModuleItems.ENTRY)),
    FARM_M(new ModuleConfig(2, ConnectRule.FRONT_ONLY, 25, "module_farm_m", ModuleItems.FARM_M)),
    STORAGE_M(new ModuleConfig(2, ConnectRule.FRONT_ONLY, 27, "module_storage_m", ModuleItems.STORAGE_M)),
    FARM_L(new ModuleConfig(3, ConnectRule.FRONT_ONLY, 26, "module_farm_l", ModuleItems.FARM_L)),
    STORAGE_L(new ModuleConfig(3, ConnectRule.FRONT_ONLY, 28, "module_storage_l", ModuleItems.STORAGE_L));

    private final ModuleConfig config;

    ModuleType(ModuleConfig config) {
        this.config = config;
    }

    public int getChunkCount() {
        return config.chunkCount();
    }

    public ConnectRule getConnectRule() {
        return config.connectRule();
    }

    public int getIndicatorModelData() {
        return config.indicatorModelData();
    }

    public String getStructureKey() {
        return config.structureKey();
    }

    public double getCameraOffset() {
        return config.getCameraOffset();
    }

    public ItemStack getRequiredItem() {
        return config.requiredItem();
    }

    public InventoryRequirement getPlacementRequirement() {
        return config.getPlacementRequirement();
    }
}