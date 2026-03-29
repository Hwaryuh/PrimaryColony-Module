package studio.semicolon.prc.api.constant.item.module;

import kr.eme.prcShop.api.PRCItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.item.IConfigureItem;

public interface ModuleItems extends IConfigureItem {
//    ItemStack DEFAULT = IConfigureItem.builder(Material.SADDLE).customModelData(1).itemName(Component.text("기본 연장 모듈")).build();
//    ItemStack T_SHAPED = IConfigureItem.builder(Material.SADDLE).customModelData(2).itemName(Component.text("T 연장 모듈")).build();
//    ItemStack CROSS_SHAPED = IConfigureItem.builder(Material.SADDLE).customModelData(3).itemName(Component.text("십자 연장 모듈")).build();
//    ItemStack MINE = IConfigureItem.builder(Material.SADDLE).customModelData(6).itemName(Component.text("광산 모듈")).build();
//    ItemStack FARM_M = IConfigureItem.builder(Material.SADDLE).customModelData(7).itemName(Component.text("농사 모듈 (중형)")).build();
//    ItemStack FARM_L = IConfigureItem.builder(Material.SADDLE).customModelData(8).itemName(Component.text("농사 모듈 (대형)")).build();
//    ItemStack STORAGE_M = IConfigureItem.builder(Material.SADDLE).customModelData(4).itemName(Component.text("스토리지 모듈 (중형)")).build();
//    ItemStack STORAGE_L = IConfigureItem.builder(Material.SADDLE).customModelData(5).itemName(Component.text("스토리지 모듈 (대형)")).build();
//    ItemStack ENTRY = IConfigureItem.builder(Material.SADDLE).customModelData(9).itemName(Component.text("출입 모듈")).build();

    ItemStack DEFAULT = PRCItems.INSTANCE.getBASIC_TOOL_MODULE().create(1);
    ItemStack T_SHAPED = PRCItems.INSTANCE.getT_TOOL_MODULE().create(1);
    ItemStack CROSS_SHAPED = PRCItems.INSTANCE.getCROSS_TOOL_MODULE().create(1);
    ItemStack MINE = PRCItems.INSTANCE.getMINE_MODULE().create(1);
    ItemStack FARM_M = PRCItems.INSTANCE.getFARM_MODULE_MEDIUM().create(1);
    ItemStack FARM_L = PRCItems.INSTANCE.getFARM_MODULE_LARGE().create(1);
    ItemStack STORAGE_M = PRCItems.INSTANCE.getSTORAGE_MODULE_MEDIUM().create(1);
    ItemStack STORAGE_L = PRCItems.INSTANCE.getSTORAGE_MODULE_LARGE().create(1);
    ItemStack ENTRY = PRCItems.INSTANCE.getGATE_MODULE().create(1);

    ItemStack MODULE_MARKER_ADD = IConfigureItem.builder(Material.CREEPER_BANNER_PATTERN)
            .itemName(Component.text("모듈 마커 추가"))
            .addLore(Component.text("원하는 위치에 상호작용 시 모듈을 마킹할 수 있다.", NamedTextColor.GRAY))
            .addLore(Component.text("어떤 모듈이 연결되어 있는지 기록할 때 쓰인다.", NamedTextColor.GRAY))
            .build();
    ItemStack MODULE_MARKER_REMOVE = IConfigureItem.builder(Material.SKULL_BANNER_PATTERN)
            .itemName(Component.text("모듈 마커 제거"))
            .addLore(Component.text("원하는 위치에 상호작용 시 해당 위치의 마커를 제거한다.", NamedTextColor.GRAY))
            .build();

    ItemStack BUILD_MODE_OVERLAY = IConfigureItem.builder(Material.ENDER_EYE)
            .equippable(EquipmentSlot.HEAD, NamespacedKey.minecraft("misc/build_module_overlay"))
            .build();
}
