package studio.semicolon.prc.api.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.Module;

import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public class NMSItemUtil {
    public static void setMaxDurability(ItemStack item, int durability) {
        modifyNMS(item, nms -> nms.set(DataComponents.MAX_DAMAGE, durability));
    }

    public static void setAttackDamage(ItemStack item, double amount) {
        item.editMeta(meta -> {
            meta.removeAttributeModifier(Attribute.ATTACK_DAMAGE);

            AttributeModifier damageModifier = new AttributeModifier(
                    new NamespacedKey(Module.getInstance(), "attack_damage"),
                    amount,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.HAND
            );

            meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier);
        });
    }

    public static void setConsumable(ItemStack item, ItemUseAnimation animation, boolean particle) {
        modifyNMS(item, nmsStack -> {
            Consumable.Builder builder = Consumable.builder().animation(animation).hasConsumeParticles(particle);

            switch (animation) {
                case EAT -> builder.sound(SoundEvents.GENERIC_EAT);
                case DRINK -> builder.sound(SoundEvents.GENERIC_DRINK);
            }

            nmsStack.set(DataComponents.CONSUMABLE, builder.build());
        });
    }

    private static void modifyNMS(ItemStack item, Consumer<net.minecraft.world.item.ItemStack> modifier) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        modifier.accept(nmsStack);
        item.setItemMeta(CraftItemStack.asBukkitCopy(nmsStack).getItemMeta());
    }
}