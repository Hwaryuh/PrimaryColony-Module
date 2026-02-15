package studio.semicolon.prc.api.constant.item;

import io.quill.paper.util.bukkit.pdc.PDCKeys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import studio.semicolon.prc.api.item.IConfigureItem;

import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;

public interface ToolItems extends IConfigureItem {
    String PRCCore = "PrcCore"; 
    
    ItemStack PICKAXE_1 = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(1).itemName(Component.text("단단한 곡괭이"))
            .addLore(Component.text("여러 광물을 캘 수 있다.", NamedTextColor.GRAY))
            .hideAttributes()
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_max"), PersistentDataType.INTEGER, 150)
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_cur"), PersistentDataType.INTEGER, 150)
            .build();
    ItemStack PICKAXE_2 = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(2).itemName(Component.text("가벼운 채굴기(드릴)"))
            .addLore(Component.text("모든 광물을 캘 수 있다.", NamedTextColor.GRAY))
            .hideAttributes()
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_max"), PersistentDataType.INTEGER, 450)
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_cur"), PersistentDataType.INTEGER, 450)
            .build();
    ItemStack PICKAXE_3 = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(3).itemName(Component.text("무거운 채굴기(드릴)"))
            .addLore(Component.text("모든 광물을 더 빠르게 캘 수 있다.", NamedTextColor.GRAY))
            .hideAttributes()
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_max"), PersistentDataType.INTEGER, 950)
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_cur"), PersistentDataType.INTEGER, 950)
            .build();


    ItemStack HOE_1 = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(4).itemName(Component.text("단단한 괭이"))
            .addLore(Component.text("땅을 갈 수 있는 괭이, 조금 더 튼튼하다.", NamedTextColor.GRAY))
            .hideAttributes()
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_max"), PersistentDataType.INTEGER, 150)
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_cur"), PersistentDataType.INTEGER, 150)
            .build();
    ItemStack HOE_2 = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(5).itemName(Component.text("가볍고 단단한 괭이"))
            .addLore(Component.text("땅을 갈 수 있는 괭이, 한 번에 더 큰 면적을 갈 수 있다.", NamedTextColor.GRAY))
            .hideAttributes()
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_max"), PersistentDataType.INTEGER, 450)
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_cur"), PersistentDataType.INTEGER, 450)
            .build();
    ItemStack HOE_3 = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(6).itemName(Component.text("자동화 괭이"))
            .addLore(Component.text("땅을 갈 수 있는 괭이, 한 번에 더 큰 면적을 갈 수 있다.", NamedTextColor.GRAY))
            .addLore(Component.text("왼손에 들고 있는 씨앗을 자동으로 심는다.", NamedTextColor.GRAY))
            .hideAttributes()
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_max"), PersistentDataType.INTEGER, 950)
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_cur"), PersistentDataType.INTEGER, 950)
            .build();


    ItemStack WATERING_CAN_1 = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(7).itemName(Component.text("소형 물뿌리개"))
            .addLore(Component.text("경작지나 식물에 물을 줄 수 있다.", NamedTextColor.GRAY))
            .hideAttributes()
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_max"), PersistentDataType.INTEGER, 450)
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_cur"), PersistentDataType.INTEGER, 450)
            .build();
    ItemStack WATERING_CAN_2 = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(8).itemName(Component.text("펌프형 물뿌리개"))
            .addLore(Component.text("경작지나 식물에 더 넓은 범위로 물을 줄 수 있다.", NamedTextColor.GRAY))
            .hideAttributes()
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_max"), PersistentDataType.INTEGER, 450)
            .pdc(PDCKeys.of(PRCCore.toLowerCase(), "custom_durability_cur"), PersistentDataType.INTEGER, 450)
            .build();
    ItemStack CAPSULE_GUN = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(9).itemName(Component.text("캡슐건"))
            .addLore(Component.text("왼손에 든 캡슐을 소모하여 작물에 사용한다.", NamedTextColor.GRAY))
            .addLore(Component.text("한 식물에 여러 캡슐을 사용할 수는 없다.", NamedTextColor.GRAY))
            .hideAttributes().build();


    ItemStack WEAPON_KNIFE = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(10).itemName(Component.text("나이프"))
            .addLore(Component.text("빠르게 적을 베어낼 수 있다.", NamedTextColor.GRAY)).hideAttributes()
            .attackDamage(6.0)
            .maxDurability(250)
            .build();
    ItemStack WEAPON_LONG_SWORD = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(11).itemName(Component.text("장도"))
            .addLore(Component.text("클래식 근접 무기, 무기가 가진 밸런스가 좋다.", NamedTextColor.GRAY)).hideAttributes()
            .attackDamage(7.0)
            .maxDurability(750)
            .build();
    ItemStack WEAPON_PIPE = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(12).itemName(Component.text("파이프"))
            .addLore(Component.text("무겁지만 치명적인 근접무기,", NamedTextColor.GRAY))
            .addLore(Component.text("느리지만 매 공격 치명적인 피해를 입힐 수 있다.", NamedTextColor.GRAY))
            .hideAttributes()
            .attackDamage(4.0)
            .maxDurability(150)
            .build();

    ItemStack SPANNER = IConfigureItem.builder(Material.WOODEN_SHOVEL).customModelData(13).itemName(Component.text("스패너")).build();

    @SuppressWarnings("UnstableApiUsage")
    ItemStack FINAL_BOOTS = IConfigureItem.builder(Material.DIAMOND_BOOTS)
            .addAttribute(Attribute.STEP_HEIGHT, "step_height", 2.0, ADD_NUMBER, EquipmentSlotGroup.FEET)
            .build();
}