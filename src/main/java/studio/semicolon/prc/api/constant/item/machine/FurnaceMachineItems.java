package studio.semicolon.prc.api.constant.item.machine;

import io.quill.paper.item.ItemMatcher;
import kr.eme.prcShop.api.PRCItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.item.IConfigureItem;

public interface FurnaceMachineItems extends IConfigureItem {
//    ItemStack MG_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(19).itemName(Component.text("마그네슘 주괴")).build();
//    ItemStack AL_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(20).itemName(Component.text("알루미늄 주괴")).build();
//    ItemStack FE_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(21).itemName(Component.text("철 주괴")).build();
//    ItemStack CU_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(22).itemName(Component.text("구리 주괴")).build();
//    ItemStack LI_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(23).itemName(Component.text("리튬 주괴")).build();
//    ItemStack AU_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(24).itemName(Component.text("금 주괴")).build();
//    ItemStack PT_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(25).itemName(Component.text("백금 주괴")).build();
//    ItemStack NI_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(26).itemName(Component.text("니켈 주괴")).build();
//    ItemStack TI_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(27).itemName(Component.text("티타늄 주괴")).build();
//    ItemStack AL_MG_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(28).itemName(Component.text("Al-Mg 합금 주괴")).build();
//    ItemStack AL_CU_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(29).itemName(Component.text("Al-Cu 합금 주괴")).build();
//    ItemStack AL_LI_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(30).itemName(Component.text("Al-Li 합금 주괴")).build();
//    ItemStack CU_AU_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(31).itemName(Component.text("Cu-Au 합금 주괴")).build();
//    ItemStack NI_FE_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(32).itemName(Component.text("Ni-Fe 합금 주괴")).build();
//    ItemStack TI_PT_AU_INGOT = IConfigureItem.builder(Material.RED_DYE).customModelData(33).itemName(Component.text("Ti-Pt-Au 합금 주괴")).build();

    ItemStack MG_INGOT = PRCItems.INSTANCE.getMAGNESIUM_INGOT().create(1);
    ItemStack AL_INGOT = PRCItems.INSTANCE.getALUMINUM_INGOT().create(1);
    ItemStack FE_INGOT = PRCItems.INSTANCE.getIRON_INGOT().create(1);
    ItemStack CU_INGOT = PRCItems.INSTANCE.getCOPPER_INGOT().create(1);
    ItemStack LI_INGOT = PRCItems.INSTANCE.getLITHIUM_INGOT().create(1);
    ItemStack AU_INGOT = PRCItems.INSTANCE.getGOLD_INGOT().create(1);
    ItemStack PT_INGOT = PRCItems.INSTANCE.getPLATINUM_INGOT().create(1);
    ItemStack NI_INGOT = PRCItems.INSTANCE.getNICKEL_INGOT().create(1);
    ItemStack TI_INGOT = PRCItems.INSTANCE.getTITANIUM_INGOT().create(1);
    ItemStack AL_MG_INGOT = PRCItems.INSTANCE.getAL_MG_ALLOY_INGOT().create(1);
    ItemStack AL_CU_INGOT = PRCItems.INSTANCE.getAL_CU_ALLOY_INGOT().create(1);
    ItemStack AL_LI_INGOT = PRCItems.INSTANCE.getAL_LI_ALLOY_INGOT().create(1);
    ItemStack CU_AU_INGOT = PRCItems.INSTANCE.getCU_AU_ALLOY_INGOT().create(1);
    ItemStack NI_FE_INGOT = PRCItems.INSTANCE.getNI_FE_ALLOY_INGOT().create(1);
    ItemStack TI_PT_AU_INGOT = PRCItems.INSTANCE.getTI_PT_AU_ALLOY_INGOT().create(1);


//    ItemStack MG_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(23).itemName(Component.text("마그네슘 주괴 레시피")).build();
//    ItemStack AL_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(24).itemName(Component.text("알루미늄 주괴 레시피")).build();
//    ItemStack FE_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(25).itemName(Component.text("철 주괴 레시피")).build();
//    ItemStack CU_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(26).itemName(Component.text("구리 주괴 레시피")).build();
//    ItemStack LI_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(27).itemName(Component.text("리튬 주괴 레시피")).build();
//    ItemStack AU_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(28).itemName(Component.text("금 주괴 레시피")).build();
//    ItemStack PT_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(29).itemName(Component.text("백금 주괴 레시피")).build();
//    ItemStack NI_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(30).itemName(Component.text("니켈 주괴 레시피")).build();
//    ItemStack TI_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(31).itemName(Component.text("티타늄 주괴 레시피")).build();
//    ItemStack AL_MG_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(32).itemName(Component.text("Al-Mg 합금 주괴 레시피")).build();
//    ItemStack AL_CU_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(33).itemName(Component.text("Al-Cu 합금 주괴 레시피")).build();
//    ItemStack AL_LI_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(34).itemName(Component.text("Al-Li 합금 주괴 레시피")).build();
//    ItemStack CU_AU_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(35).itemName(Component.text("Cu-Au 합금 주괴 레시피")).build();
//    ItemStack NI_FE_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(36).itemName(Component.text("Ni-Fe 합금 주괴 레시피")).build();
//    ItemStack TI_PT_AU_RECIPE = IConfigureItem.builder(Material.SADDLE).customModelData(37).itemName(Component.text("Ti-Pt-Au 합금 주괴 레시피")).build();

    ItemStack MG_RECIPE = PRCItems.INSTANCE.getRECIPE_MAGNESIUM_INGOT().create(1);
    ItemStack AL_RECIPE = PRCItems.INSTANCE.getRECIPE_ALUMINUM_INGOT().create(1);
    ItemStack FE_RECIPE = PRCItems.INSTANCE.getRECIPE_IRON_INGOT().create(1);
    ItemStack CU_RECIPE = PRCItems.INSTANCE.getRECIPE_COPPER_INGOT().create(1);
    ItemStack LI_RECIPE = PRCItems.INSTANCE.getRECIPE_LITHIUM_INGOT().create(1);
    ItemStack AU_RECIPE = PRCItems.INSTANCE.getRECIPE_GOLD_INGOT().create(1);
    ItemStack PT_RECIPE = PRCItems.INSTANCE.getRECIPE_PLATINUM_INGOT().create(1);
    ItemStack NI_RECIPE = PRCItems.INSTANCE.getRECIPE_NICKEL_INGOT().create(1);
    ItemStack TI_RECIPE = PRCItems.INSTANCE.getRECIPE_TITANIUM_INGOT().create(1);
    ItemStack AL_MG_RECIPE = PRCItems.INSTANCE.getRECIPE_AL_MG_ALLOY().create(1);
    ItemStack AL_CU_RECIPE = PRCItems.INSTANCE.getRECIPE_AL_CU_ALLOY().create(1);
    ItemStack AL_LI_RECIPE = PRCItems.INSTANCE.getRECIPE_AL_LI_ALLOY().create(1);
    ItemStack CU_AU_RECIPE = PRCItems.INSTANCE.getRECIPE_CU_AU_ALLOY().create(1);
    ItemStack NI_FE_RECIPE = PRCItems.INSTANCE.getRECIPE_NI_FE_ALLOY().create(1);
    ItemStack TI_PT_AU_RECIPE = PRCItems.INSTANCE.getRECIPE_TI_PT_AU_ALLOY().create(1);


//    ItemStack SLOT_EXTENSION = IConfigureItem.builder(Material.SADDLE).customModelData(17).itemName(Component.text("용광로 용해 슬롯 확장")).build();
//    ItemStack ICE_MOLD = IConfigureItem.builder(Material.SADDLE).customModelData(18).itemName(Component.text("Cu-Au 합금 냉각 몰드")).build();
//    ItemStack TORCH = IConfigureItem.builder(Material.SADDLE).customModelData(19).itemName(Component.text("Ni-Fe 합금 토치")).build();

    ItemStack SLOT_EXTENSION = PRCItems.INSTANCE.getFURNACE_SLOT_UPGRADE().create(1);
    ItemStack ICE_MOLD = PRCItems.INSTANCE.getALLOY_COOLING_MOLD_CU_AU().create(1);
    ItemStack TORCH = PRCItems.INSTANCE.getALLOY_TORCH_NI_FE().create(1);


    ItemStack START_BUTTON = IConfigureItem.builder(Material.GLASS_PANE).customModelData(1)
            .itemName(Component.text("용해 시작", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
            .addLore(Component.text("재료를 배치 후 클릭하기", NamedTextColor.GRAY))
            .build();
    ItemStack GUIDE_FUEL = IConfigureItem.builder(Material.GLASS_PANE).customModelData(1)
            .itemName(Component.text("마그네슘 파우더", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
            .addLore(Component.text("모듈을 작동시키려면 마그네슘 파우더가 필요합니다.", NamedTextColor.GRAY))
            .build();
    ItemStack GUIDE_POWDER = IConfigureItem.builder(Material.GLASS_PANE).customModelData(1)
            .itemName(Component.text("파우더", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
            .addLore(Component.text("용해시킬 파우더를 놓아주세요. (3개 필요)", NamedTextColor.GRAY))
            .build();
    ItemStack GUIDE_RECIPE = IConfigureItem.builder(Material.GLASS_PANE).customModelData(1)
            .itemName(Component.text("레시피", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
            .addLore(Component.text("제작할 주괴의 레시피를 놓아주세요.", NamedTextColor.GRAY))
            .build();

    static boolean isIngotRecipe(ItemStack i) {
        return ItemMatcher.matches(i, MG_RECIPE) ||
                ItemMatcher.matches(i, AL_RECIPE) ||
                ItemMatcher.matches(i, FE_RECIPE) ||
                ItemMatcher.matches(i, CU_RECIPE) ||
                ItemMatcher.matches(i, LI_RECIPE) ||
                ItemMatcher.matches(i, AU_RECIPE) ||
                ItemMatcher.matches(i, PT_RECIPE) ||
                ItemMatcher.matches(i, NI_RECIPE) ||
                ItemMatcher.matches(i, TI_RECIPE) ||
                ItemMatcher.matches(i, AL_MG_RECIPE) ||
                ItemMatcher.matches(i, AL_CU_RECIPE) ||
                ItemMatcher.matches(i, AL_LI_RECIPE) ||
                ItemMatcher.matches(i, CU_AU_RECIPE) ||
                ItemMatcher.matches(i, NI_FE_RECIPE) ||
                ItemMatcher.matches(i, TI_PT_AU_RECIPE);
    }
}
