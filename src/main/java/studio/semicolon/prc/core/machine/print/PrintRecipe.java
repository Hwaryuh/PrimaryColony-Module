package studio.semicolon.prc.core.machine.print;

import com.google.common.collect.Maps;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.constant.item.ToolItems;
import studio.semicolon.prc.api.constant.item.machine.FurnaceMachineItems;

import java.util.LinkedHashMap;
import java.util.Map;

public enum PrintRecipe {
    PICKAXE_1(
            ToolItems.PICKAXE_1,
            materials(
                    FurnaceMachineItems.FE_INGOT, 10
            )
    ),
    HOE_1(
            ToolItems.HOE_1,
            materials(
                    FurnaceMachineItems.FE_INGOT, 10
            )
    ),
    WATERING_CAN_1(
            ToolItems.WATERING_CAN_1,
            materials(
                    FurnaceMachineItems.AL_INGOT, 10
            )
    ),
    WEAPON_PIPE(
            ToolItems.WEAPON_PIPE,
            materials(
                    FurnaceMachineItems.FE_INGOT, 10
            )
    ),
    PICKAXE_2(
            ToolItems.PICKAXE_2,
            materials(
                    FurnaceMachineItems.AL_INGOT, 20,
                    FurnaceMachineItems.FE_INGOT, 10,
                    FurnaceMachineItems.CU_INGOT, 10,
                    FurnaceMachineItems.LI_INGOT, 5,
                    ToolItems.PICKAXE_1, 1
            )
    ),
    HOE_2(
            ToolItems.HOE_2,
            materials(
                    FurnaceMachineItems.AL_INGOT, 10,
                    FurnaceMachineItems.FE_INGOT, 10,
                    FurnaceMachineItems.CU_INGOT, 10,
                    ToolItems.HOE_1, 1
            )
    ),
    WATERING_CAN_2(
            ToolItems.WATERING_CAN_2,
            materials(
                    FurnaceMachineItems.AL_INGOT, 15,
                    FurnaceMachineItems.FE_INGOT, 10,
                    FurnaceMachineItems.CU_INGOT, 5,
                    ToolItems.WATERING_CAN_1, 1
            )
    ),
    WEAPON_KNIFE(
            ToolItems.WEAPON_KNIFE,
            materials(
                    FurnaceMachineItems.CU_INGOT, 10,
                    FurnaceMachineItems.LI_INGOT, 10,
                    FurnaceMachineItems.PT_INGOT, 10
            )
    ),
    PICKAXE_3(
            ToolItems.PICKAXE_3,
            materials(
                    FurnaceMachineItems.CU_INGOT, 10,
                    FurnaceMachineItems.LI_INGOT, 10,
                    FurnaceMachineItems.PT_INGOT, 10,
                    FurnaceMachineItems.NI_INGOT, 5,
                    FurnaceMachineItems.TI_INGOT, 5,
                    ToolItems.PICKAXE_2, 1
            )
    ),
    HOE_3(
            ToolItems.HOE_3,
            materials(
                    FurnaceMachineItems.CU_INGOT, 10,
                    FurnaceMachineItems.LI_INGOT, 10,
                    FurnaceMachineItems.FE_INGOT, 10,
                    FurnaceMachineItems.NI_INGOT, 5,
                    FurnaceMachineItems.TI_INGOT, 5,
                    ToolItems.HOE_2, 1
            )
    ),
    CAPSULE_GUN(
            ToolItems.CAPSULE_GUN,
            materials(
                    FurnaceMachineItems.CU_INGOT, 20,
                    FurnaceMachineItems.LI_INGOT, 20,
                    FurnaceMachineItems.PT_INGOT, 15
            )
    ),
    WEAPON_LONG_SWORD(
            ToolItems.WEAPON_LONG_SWORD,
            materials(
                    FurnaceMachineItems.AL_INGOT, 20,
                    FurnaceMachineItems.FE_INGOT, 10,
                    FurnaceMachineItems.CU_INGOT, 10
            )
    );

    private final ItemStack result;
    private final Map<ItemStack, Integer> materials;

    PrintRecipe(ItemStack result, Map<ItemStack, Integer> materials) {
        this.result = result;
        this.materials = materials;
    }

    public String getID() {
        return this.name().toLowerCase();
    }

    public ItemStack getResult() {
        return result.clone();
    }

    public Map<ItemStack, Integer> getMaterials() {
        return materials;
    }

    private static LinkedHashMap<ItemStack, Integer> materials(Object... pairs) {
        LinkedHashMap<ItemStack, Integer> map = Maps.newLinkedHashMap();
        for (int i = 0; i < pairs.length; i += 2) {
            map.put((ItemStack) pairs[i], (Integer) pairs[i + 1]);
        }
        return map;
    }

    public static PrintRecipe getByID(String ID) {
        for (PrintRecipe recipe : values()) {
            if (recipe.getID().equals(ID)) {
                return recipe;
            }
        }
        return null;
    }
}