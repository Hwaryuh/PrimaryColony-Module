package studio.semicolon.prc.core.menu;

import com.google.common.collect.Lists;
import io.quill.paper.menu.InventoryMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import studio.semicolon.prc.api.constant.item.game.GameItems;
import studio.semicolon.prc.api.constant.item.game.ToolItems;
import studio.semicolon.prc.api.constant.item.machine.CoffeeMachineItems;
import studio.semicolon.prc.api.constant.item.machine.FurnaceMachineItems;
import studio.semicolon.prc.api.constant.item.machine.GrinderMachineItems;
import studio.semicolon.prc.api.constant.item.machine.MachineItems;
import studio.semicolon.prc.api.constant.item.module.ModuleItems;
import studio.semicolon.prc.api.item.IConfigureItem;

import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class AdminMenu extends InventoryMenu {
    private static final int PAGE_SIZE = 45;
    private static final int PREV_SLOT = 45;
    private static final int NEXT_SLOT = 53;

    private static final List<ItemStack> ITEMS = Lists.newArrayList(
            CoffeeMachineItems.COFFEE_BEAN_AG,
            CoffeeMachineItems.COFFEE_BEAN_AU,
            CoffeeMachineItems.COFFEE_BEAN_TI,
            CoffeeMachineItems.MUG,
            CoffeeMachineItems.COFFEE,

            GrinderMachineItems.MG,
            GrinderMachineItems.AL,
            GrinderMachineItems.FE,
            GrinderMachineItems.CU,
            GrinderMachineItems.LI,
            GrinderMachineItems.AU,
            GrinderMachineItems.PT,
            GrinderMachineItems.NI,
            GrinderMachineItems.TI,

            GrinderMachineItems.MG_POWDER,
            GrinderMachineItems.AL_POWDER,
            GrinderMachineItems.FE_POWDER,
            GrinderMachineItems.CU_POWDER,
            GrinderMachineItems.LI_POWDER,
            GrinderMachineItems.AU_POWDER,
            GrinderMachineItems.PT_POWDER,
            GrinderMachineItems.NI_POWDER,
            GrinderMachineItems.TI_POWDER,

            GrinderMachineItems.SLOT_EXTENSION,
            GrinderMachineItems.GEAR,
            GrinderMachineItems.DRILL,

            FurnaceMachineItems.MG_INGOT,
            FurnaceMachineItems.AL_INGOT,
            FurnaceMachineItems.FE_INGOT,
            FurnaceMachineItems.CU_INGOT,
            FurnaceMachineItems.LI_INGOT,
            FurnaceMachineItems.AU_INGOT,
            FurnaceMachineItems.PT_INGOT,
            FurnaceMachineItems.NI_INGOT,
            FurnaceMachineItems.TI_INGOT,
            FurnaceMachineItems.AL_MG_INGOT,
            FurnaceMachineItems.AL_CU_INGOT,
            FurnaceMachineItems.AL_LI_INGOT,
            FurnaceMachineItems.CU_AU_INGOT,
            FurnaceMachineItems.NI_FE_INGOT,
            FurnaceMachineItems.TI_PT_AU_INGOT,

            FurnaceMachineItems.MG_RECIPE,
            FurnaceMachineItems.AL_RECIPE,
            FurnaceMachineItems.FE_RECIPE,
            FurnaceMachineItems.CU_RECIPE,
            FurnaceMachineItems.LI_RECIPE,
            FurnaceMachineItems.AU_RECIPE,
            FurnaceMachineItems.PT_RECIPE,
            FurnaceMachineItems.NI_RECIPE,
            FurnaceMachineItems.TI_RECIPE,
            FurnaceMachineItems.AL_MG_RECIPE,
            FurnaceMachineItems.AL_CU_RECIPE,
            FurnaceMachineItems.AL_LI_RECIPE,
            FurnaceMachineItems.CU_AU_RECIPE,
            FurnaceMachineItems.NI_FE_RECIPE,
            FurnaceMachineItems.TI_PT_AU_RECIPE,

            FurnaceMachineItems.SLOT_EXTENSION,
            FurnaceMachineItems.ICE_MOLD,
            FurnaceMachineItems.TORCH,

            MachineItems.GRINDER_MACHINE,
            MachineItems.PRINT_MACHINE,
            MachineItems.COFFEE_MACHINE,
            MachineItems.FURNACE_MACHINE,

            ModuleItems.DEFAULT,
            ModuleItems.T_SHAPED,
            ModuleItems.CROSS_SHAPED,
            ModuleItems.MINE,
            ModuleItems.FARM_M,
            ModuleItems.FARM_L,
            ModuleItems.STORAGE_M,
            ModuleItems.STORAGE_L,
            ModuleItems.ENTRY,
            ModuleItems.MODULE_MARKER_ADD,
            ModuleItems.MODULE_MARKER_REMOVE,

            ToolItems.PICKAXE_1,
            ToolItems.PICKAXE_2,
            ToolItems.PICKAXE_3,
            ToolItems.HOE_1,
            ToolItems.HOE_2,
            ToolItems.HOE_3,
            ToolItems.WATERING_CAN_1,
            ToolItems.WATERING_CAN_2,
            ToolItems.CAPSULE_GUN,
            ToolItems.WEAPON_KNIFE,
            ToolItems.WEAPON_LONG_SWORD,
            ToolItems.WEAPON_PIPE,
            ToolItems.SPANNER,

            GameItems.FINAL_HELMET,
            GameItems.FINAL_CHESTPLATE,
            GameItems.FINAL_LEGGINGS,
            GameItems.FINAL_BOOTS,
            GameItems.FOOD_CAPSULE
    );

    private static final ItemStack PREV_BUTTON = IConfigureItem.builder(Material.ARROW)
            .itemName(Component.text("이전", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
            .build();

    private static final ItemStack NEXT_BUTTON = IConfigureItem.builder(Material.ARROW)
            .itemName(Component.text("다음", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
            .build();

    private int page = 0;

    public AdminMenu(Player player) {
        super(player, MenuType.GENERIC_9X6, Component.text("Admin"));
    }

    @Override
    protected void render() {
        renderPage();
    }

    private void renderPage() {
        clearItemSlots();

        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, ITEMS.size());

        for (int i = start; i < end; i++) {
            int slot = i - start;
            ItemStack item = ITEMS.get(i);

            setButton(slot, button(item)
                    .onLeftClick(ctx -> giveItem(item.clone()))
                    .cancelAll()
                    .build()
            );
        }

        renderArrows();
    }

    private void renderArrows() {
        removeButton(PREV_SLOT);
        removeButton(NEXT_SLOT);

        if (page > 0) {
            setButton(PREV_SLOT, button(PREV_BUTTON)
                    .onLeftClick(ctx -> {
                        page--;
                        renderPage();
                    })
                    .cancelAll()
                    .build()
            );
        }

        int totalPages = (int) Math.ceil((double) ITEMS.size() / PAGE_SIZE);
        if (page < totalPages - 1) {
            setButton(NEXT_SLOT, button(NEXT_BUTTON)
                    .onLeftClick(ctx -> {
                        page++;
                        renderPage();
                    })
                    .cancelAll()
                    .build()
            );
        }
    }

    private void clearItemSlots() {
        for (int i = 0; i < PAGE_SIZE; i++) {
            removeButton(i);
        }
    }

    private void giveItem(ItemStack item) {
        Map<Integer, ItemStack> remaining = player.getInventory().addItem(item);
        remaining.values().forEach(drop ->
                player.getWorld().dropItemNaturally(player.getLocation(), drop)
        );
    }
}
