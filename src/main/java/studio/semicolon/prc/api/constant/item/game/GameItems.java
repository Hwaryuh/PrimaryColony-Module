package studio.semicolon.prc.api.constant.item.game;

import kr.eme.prcShop.api.PRCItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.item.IConfigureItem;

import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;

public interface GameItems extends IConfigureItem {
//    ItemStack FOOD_CAPSULE = IConfigureItem.builder(Material.GOLDEN_CARROT).itemName(Component.text("식량 캡슐")).amount(32).build();
    ItemStack FOOD_CAPSULE = PRCItems.INSTANCE.getFOOD_CAPSULE().create(32);

    ItemStack FINAL_HELMET = IConfigureItem.builder(Material.DIAMOND_HELMET).unbreakable().build();
    ItemStack FINAL_CHESTPLATE = IConfigureItem.builder(Material.DIAMOND_CHESTPLATE).unbreakable().build();
    ItemStack FINAL_LEGGINGS = IConfigureItem.builder(Material.DIAMOND_LEGGINGS).unbreakable().build();
    @SuppressWarnings("UnstableApiUsage")
    ItemStack FINAL_BOOTS = IConfigureItem.builder(Material.DIAMOND_BOOTS)
            .unbreakable()
            .addAttribute(Attribute.STEP_HEIGHT, "step_height", 2.0, ADD_NUMBER, EquipmentSlotGroup.FEET)
            .build();

    static ItemStack nametagOf(int index) {
        return IConfigureItem.builder(Material.ORANGE_DYE)
                .customModelData(9)
                .itemName(Component.text("누군가의 인식표"))
                .addLore(Component.text("희미하게 ", NamedTextColor.GRAY)
                        .append(Component.text("["))
                        .append(Component.text(nametagLoreOf(index)))
                        .append(Component.text("]"))
                        .append(Component.text("... 이라고 적혀있는 것 같다"))
                )
                .build();
    }

    private static String nametagLoreOf(int index) {
        return switch (index) {
            case 1 -> "리플";
            case 2 -> "이래도";
            case 3 -> "법마";
            case 4 -> "신삼두";
            case 5 -> "이난오";
            case 6 -> "훈";
            case 7 -> "명훈";
            case 8 -> "에메";
            case 9 -> "이해미";
            case 10 -> "폭마군";
            case 11 -> "카페인";
            case 12 -> "무린";
            case 13 -> "엘";
            default -> throw new IllegalArgumentException("Invalid statue index: " + index);
        };
    }
}
