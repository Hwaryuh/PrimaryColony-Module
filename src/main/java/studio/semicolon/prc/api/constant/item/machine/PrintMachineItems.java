package studio.semicolon.prc.api.constant.item.machine;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.item.IConfigureItem;

import java.util.List;
import java.util.Map;

public interface PrintMachineItems extends IConfigureItem {
    ItemStack YES_BUTTON = IConfigureItem.builder(Material.GLASS_PANE)
            .customModelData(1)
            .itemName(Component.text("제작 시작", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
            .addLore(Component.text("선택된 아이템을 제작합니다.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
            .addLore(Component.text("재료를 확인하고 클릭하세요.", NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false))
            .build();

    ItemStack NO_BUTTON = IConfigureItem.builder(Material.GLASS_PANE)
            .customModelData(1)
            .itemName(Component.text("취소", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
            .addLore(Component.text("선택을 취소합니다.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
            .addLore(Component.text("이전 화면으로 돌아갑니다.", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
            .build();


    static ItemStack withMaterials(ItemStack selected, Map<ItemStack, Integer> materials) {
        ItemStack item = selected.clone();
        item.editMeta(meta -> {
            List<Component> lore = Lists.newArrayList();
            lore.add(Component.text("필요 재료:", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));

            for (Map.Entry<ItemStack, Integer> entry : materials.entrySet()) {
                ItemStack material = entry.getKey();
                int amount = entry.getValue();
                Component materialName = material.getItemMeta().itemName();

                lore.add(Component.text("- ", NamedTextColor.GRAY)
                        .append(materialName)
                        .append(Component.text(" x" + amount, NamedTextColor.GRAY))
                        .decoration(TextDecoration.ITALIC, false));
            }

            meta.lore(lore);
        });
        return item;
    }
}