package studio.semicolon.prc.core.constant.item.machine;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.world.item.ItemUseAnimation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.item.IConfigureItem;

public interface CoffeeMachineItems extends IConfigureItem {
    ItemStack COFFEE_BEAN_AG = IConfigureItem.builder(Material.BLACK_DYE).customModelData(4).itemName(Component.text("커피콩")).build();
    ItemStack COFFEE_BEAN_AU = IConfigureItem.builder(Material.BLACK_DYE).customModelData(14).itemName(Component.text("커피콩")).build();
    ItemStack COFFEE_BEAN_TI = IConfigureItem.builder(Material.BLACK_DYE).customModelData(24).itemName(Component.text("커피콩")).build();
    ItemStack MUG = IConfigureItem.builder(Material.BOWL).customModelData(1).itemName(Component.text("머그잔")).build();
    ItemStack COFFEE = IConfigureItem.builder(Material.BOWL)
            .customModelData(2)
            .itemName(Component.text("커피"))
            .consumable(ItemUseAnimation.DRINK, true)
            .build();

    ItemStack START_BUTTON = IConfigureItem.builder(Material.GLASS_PANE)
            .customModelData(1)
            .itemName(Component.text("제조 시작", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
            .addLore(Component.text("재료를 배치 후 클릭하기", NamedTextColor.GRAY))
            .build();
}