package studio.semicolon.prc.api.constant.item.game;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.item.IConfigureItem;

import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;

public interface GameItems extends IConfigureItem {
    ItemStack FOOD_CAPSULE = IConfigureItem.builder(Material.GOLDEN_CARROT).itemName(Component.text("식량 캡슐")).amount(32).build();

    ItemStack FINAL_HELMET = IConfigureItem.builder(Material.DIAMOND_HELMET).unbreakable().build();
    ItemStack FINAL_CHESTPLATE = IConfigureItem.builder(Material.DIAMOND_CHESTPLATE).unbreakable().build();
    ItemStack FINAL_LEGGINGS = IConfigureItem.builder(Material.DIAMOND_LEGGINGS).unbreakable().build();
    @SuppressWarnings("UnstableApiUsage")
    ItemStack FINAL_BOOTS = IConfigureItem.builder(Material.DIAMOND_BOOTS)
            .unbreakable()
            .addAttribute(Attribute.STEP_HEIGHT, "step_height", 2.0, ADD_NUMBER, EquipmentSlotGroup.FEET)
            .build();
}
