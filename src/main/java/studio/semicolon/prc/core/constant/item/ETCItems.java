package studio.semicolon.prc.core.constant.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.item.IConfigureItem;

public interface ETCItems extends IConfigureItem {
    ItemStack MACHINE_GUIDE_ITEM = IConfigureItem.builder(Material.GLASS_PANE).customModelData(1).itemName(Component.text("")).build();
    ItemStack BLOCKED_SLOT_ITEM = IConfigureItem.builder(Material.BARRIER).customModelData(1)
            .itemName(Component.text("업그레이드 전에는 사용할 수 없습니다.", NamedTextColor.RED))
            .build();
}