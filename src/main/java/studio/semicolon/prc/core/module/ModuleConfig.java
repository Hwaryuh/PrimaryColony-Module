package studio.semicolon.prc.core.module;

import io.quill.paper.item.require.InventoryRequirement;
import io.quill.paper.item.require.InventoryRequirements;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.core.module.validate.connect.ConnectRule;

public record ModuleConfig(int chunkCount, ConnectRule connectRule, int indicatorModelData, String structureKey, ItemStack requiredItem) {
    public double getCameraOffset() {
        return switch (chunkCount) {
            case 1 -> 16.0 * 2.5;
            case 2 -> 16.0 * 3.5;
            case 3 -> 16.0 * 4.5;
            default -> throw new IllegalStateException("Unexpected chunk count: " + chunkCount);
        };
    }

    public InventoryRequirement getPlacementRequirement() {
        return InventoryRequirements.requireLegacyModelData(requiredItem.getType(), requiredItem.getItemMeta().getCustomModelData(), 1);
    }
}