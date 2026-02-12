package studio.semicolon.prc.core.constant.item.module;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.item.IConfigureItem;

public interface ModuleItems extends IConfigureItem {
    ItemStack DEFAULT = IConfigureItem.builder(Material.SADDLE).customModelData(1).itemName(Component.text("기본 연장 모듈")).build();
    ItemStack T_SHAPED = IConfigureItem.builder(Material.SADDLE).customModelData(2).itemName(Component.text("T 연장 모듈")).build();
    ItemStack CROSS_SHAPED = IConfigureItem.builder(Material.SADDLE).customModelData(3).itemName(Component.text("십자 연장 모듈")).build();
    ItemStack MINE = IConfigureItem.builder(Material.SADDLE).customModelData(6).itemName(Component.text("광산 모듈")).build();
    ItemStack FARM_M = IConfigureItem.builder(Material.SADDLE).customModelData(7).itemName(Component.text("농사 모듈 (중형)")).build();
    ItemStack FARM_L = IConfigureItem.builder(Material.SADDLE).customModelData(8).itemName(Component.text("농사 모듈 (대형)")).build();
    ItemStack STORAGE_M = IConfigureItem.builder(Material.SADDLE).customModelData(4).itemName(Component.text("스토리지 모듈 (중형)")).build();
    ItemStack STORAGE_L = IConfigureItem.builder(Material.SADDLE).customModelData(5).itemName(Component.text("스토리지 모듈 (대형)")).build();
    ItemStack ENTRY = IConfigureItem.builder(Material.SADDLE).customModelData(9).itemName(Component.text("출입 모듈")).build();

    ItemStack BUILD_MODE_OVERLAY = IConfigureItem.builder(Material.ENDER_EYE)
            .equippable(EquipmentSlot.HEAD, NamespacedKey.minecraft("misc/build_module_overlay"))
            .build();
}