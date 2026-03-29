package studio.semicolon.prc.api.constant.item.machine;

import io.quill.paper.item.ItemMatcher;
import kr.eme.prcShop.api.PRCItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.item.IConfigureItem;

public interface GrinderMachineItems extends IConfigureItem {
//    ItemStack MG = IConfigureItem.builder(Material.RED_DYE).customModelData(1).itemName(Component.text("마그네슘 광석")).build();
//    ItemStack AL = IConfigureItem.builder(Material.RED_DYE).customModelData(2).itemName(Component.text("알루미늄 광석")).build();
//    ItemStack FE = IConfigureItem.builder(Material.RED_DYE).customModelData(3).itemName(Component.text("철 광석")).build();
//    ItemStack CU = IConfigureItem.builder(Material.RED_DYE).customModelData(4).itemName(Component.text("구리 광석")).build();
//    ItemStack LI = IConfigureItem.builder(Material.RED_DYE).customModelData(5).itemName(Component.text("리튬 광석")).build();
//    ItemStack AU = IConfigureItem.builder(Material.RED_DYE).customModelData(6).itemName(Component.text("금 광석")).build();
//    ItemStack PT = IConfigureItem.builder(Material.RED_DYE).customModelData(7).itemName(Component.text("백금 광석")).build();
//    ItemStack NI = IConfigureItem.builder(Material.RED_DYE).customModelData(8).itemName(Component.text("니켈 광석")).build();
//    ItemStack TI = IConfigureItem.builder(Material.RED_DYE).customModelData(9).itemName(Component.text("티타늄 광석")).build();

    ItemStack MG = PRCItems.INSTANCE.getMAGNESIUM().create(1);
    ItemStack AL = PRCItems.INSTANCE.getALUMINUM().create(1);
    ItemStack FE = PRCItems.INSTANCE.getIRON().create(1);
    ItemStack CU = PRCItems.INSTANCE.getCOPPER().create(1);
    ItemStack LI = PRCItems.INSTANCE.getLITHIUM().create(1);
    ItemStack AU = PRCItems.INSTANCE.getGOLD().create(1);
    ItemStack PT = PRCItems.INSTANCE.getPLATINUM().create(1);
    ItemStack NI = PRCItems.INSTANCE.getNICKEL().create(1);
    ItemStack TI = PRCItems.INSTANCE.getTITANIUM().create(1);

//    ItemStack MG_POWDER = IConfigureItem.builder(Material.RED_DYE).customModelData(10).itemName(Component.text("마그네슘 파우더")).build();
//    ItemStack AL_POWDER = IConfigureItem.builder(Material.RED_DYE).customModelData(11).itemName(Component.text("알루미늄 파우더")).build();
//    ItemStack FE_POWDER = IConfigureItem.builder(Material.RED_DYE).customModelData(12).itemName(Component.text("철 파우더")).build();
//    ItemStack CU_POWDER = IConfigureItem.builder(Material.RED_DYE).customModelData(13).itemName(Component.text("구리 파우더")).build();
//    ItemStack LI_POWDER = IConfigureItem.builder(Material.RED_DYE).customModelData(14).itemName(Component.text("리튬 파우더")).build();
//    ItemStack AU_POWDER = IConfigureItem.builder(Material.RED_DYE).customModelData(15).itemName(Component.text("금 파우더")).build();
//    ItemStack PT_POWDER = IConfigureItem.builder(Material.RED_DYE).customModelData(16).itemName(Component.text("백금 파우더")).build();
//    ItemStack NI_POWDER = IConfigureItem.builder(Material.RED_DYE).customModelData(17).itemName(Component.text("니켈 파우더")).build();
//    ItemStack TI_POWDER = IConfigureItem.builder(Material.RED_DYE).customModelData(18).itemName(Component.text("티타늄 파우더")).build();

    ItemStack MG_POWDER = PRCItems.INSTANCE.getMAGNESIUM_POWDER().create(1);
    ItemStack AL_POWDER = PRCItems.INSTANCE.getALUMINUM_POWDER().create(1);
    ItemStack FE_POWDER = PRCItems.INSTANCE.getIRON_POWDER().create(1);
    ItemStack CU_POWDER = PRCItems.INSTANCE.getCOPPER_POWDER().create(1);
    ItemStack LI_POWDER = PRCItems.INSTANCE.getLITHIUM_POWDER().create(1);
    ItemStack AU_POWDER = PRCItems.INSTANCE.getGOLD_POWDER().create(1);
    ItemStack PT_POWDER = PRCItems.INSTANCE.getPLATINUM_POWDER().create(1);
    ItemStack NI_POWDER = PRCItems.INSTANCE.getNICKEL_POWDER().create(1);
    ItemStack TI_POWDER = PRCItems.INSTANCE.getTITANIUM_POWDER().create(1);


//    ItemStack SLOT_EXTENSION = IConfigureItem.builder(Material.SADDLE).customModelData(20).itemName(Component.text("분쇄기 용량 확장")).build();
//    ItemStack GEAR = IConfigureItem.builder(Material.SADDLE).customModelData(21).itemName(Component.text("Al-Cu 합금 기어")).build();
//    ItemStack DRILL = IConfigureItem.builder(Material.SADDLE).customModelData(22).itemName(Component.text("Ti-Pt-Au 합금 드릴")).build();

    ItemStack SLOT_EXTENSION = PRCItems.INSTANCE.getGRINDER_CAPACITY_UPGRADE().create(1);
    ItemStack GEAR = PRCItems.INSTANCE.getALLOY_GEAR_AL_CU().create(1);
    ItemStack DRILL = PRCItems.INSTANCE.getALLOY_DRILL_TI_PT_AU().create(1);


    ItemStack LEVER_BUTTON = IConfigureItem.builder(Material.GLASS_PANE).customModelData(1)
            .itemName(Component.text("분쇄 시작", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
            .addLore(Component.text("재료를 배치 후 클릭하기", NamedTextColor.GRAY))
            .build();
    ItemStack GUIDE_ORE = IConfigureItem.builder(Material.GLASS_PANE).customModelData(1)
            .itemName(Component.text("광석", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
            .addLore(Component.text("분쇄할 광석을 놓아주세요.", NamedTextColor.GRAY))
            .build();

    static boolean isOre(ItemStack item) {
        return ItemMatcher.matches(item, GrinderMachineItems.MG) ||
                ItemMatcher.matches(item, GrinderMachineItems.AL) ||
                ItemMatcher.matches(item, GrinderMachineItems.FE) ||
                ItemMatcher.matches(item, GrinderMachineItems.CU) ||
                ItemMatcher.matches(item, GrinderMachineItems.LI) ||
                ItemMatcher.matches(item, GrinderMachineItems.AU) ||
                ItemMatcher.matches(item, GrinderMachineItems.PT) ||
                ItemMatcher.matches(item, GrinderMachineItems.NI) ||
                ItemMatcher.matches(item, GrinderMachineItems.TI);
    }

    static boolean isPowder(ItemStack item) {
        return ItemMatcher.matches(item, GrinderMachineItems.MG_POWDER) ||
                ItemMatcher.matches(item, GrinderMachineItems.AL_POWDER) ||
                ItemMatcher.matches(item, GrinderMachineItems.FE_POWDER) ||
                ItemMatcher.matches(item, GrinderMachineItems.CU_POWDER) ||
                ItemMatcher.matches(item, GrinderMachineItems.LI_POWDER) ||
                ItemMatcher.matches(item, GrinderMachineItems.AU_POWDER) ||
                ItemMatcher.matches(item, GrinderMachineItems.PT_POWDER) ||
                ItemMatcher.matches(item, GrinderMachineItems.NI_POWDER) ||
                ItemMatcher.matches(item, GrinderMachineItems.TI_POWDER);
    }
}