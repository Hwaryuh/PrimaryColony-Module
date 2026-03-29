package studio.semicolon.prc.api.constant.item.machine;

import io.quill.paper.item.ItemMatcher;
import kr.eme.prcShop.api.PRCItems;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.item.IConfigureItem;

public interface MachineItems extends IConfigureItem {
//    ItemStack GRINDER_MACHINE = IConfigureItem.builder(Material.IRON_HORSE_ARMOR).customModelData(2).itemName(Component.text("분쇄기 모듈")).build();
//    ItemStack PRINT_MACHINE = IConfigureItem.builder(Material.IRON_HORSE_ARMOR).customModelData(3).itemName(Component.text("프린트 모듈")).build();
//    ItemStack COFFEE_MACHINE = IConfigureItem.builder(Material.IRON_HORSE_ARMOR).customModelData(4).itemName(Component.text("커피머신 모듈")).build();
//    ItemStack FURNACE_MACHINE = IConfigureItem.builder(Material.IRON_HORSE_ARMOR).customModelData(7).itemName(Component.text("용광로 모듈")).build();

    ItemStack GRINDER_MACHINE = PRCItems.INSTANCE.getGRINDER_MODULE().create(1);
    ItemStack PRINT_MACHINE = PRCItems.INSTANCE.getPRINTER_MODULE().create(1);
    ItemStack COFFEE_MACHINE = PRCItems.INSTANCE.getCOFFEE_MACHINE_MODULE().create(1);
    ItemStack FURNACE_MACHINE = PRCItems.INSTANCE.getFURNACE_MODULE().create(1);

    static boolean isMachineItem(ItemStack i) {
        return ItemMatcher.matches(i, GRINDER_MACHINE) ||
                ItemMatcher.matches(i, PRINT_MACHINE) ||
                ItemMatcher.matches(i, COFFEE_MACHINE) ||
                ItemMatcher.matches(i, FURNACE_MACHINE);
    }
}
