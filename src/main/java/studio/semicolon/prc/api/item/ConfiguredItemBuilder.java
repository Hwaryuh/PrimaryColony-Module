package studio.semicolon.prc.api.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.world.item.ItemUseAnimation;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import studio.semicolon.prc.Module;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfiguredItemBuilder {
    private final Material material;
    private int amount = 1;
    private Integer customModelData;
    private Component itemName;
    private final List<Component> lore = Lists.newArrayList();
    private final Map<Attribute, AttributeModifier> attributes = Maps.newHashMap();
    private final Set<ItemFlag> itemFlags = Sets.newHashSet();
    private Integer maxStackSize;
    private Integer maxDurability;
    private Double attackDamage;
    private ItemUseAnimation consumableAnimation;
    private Boolean consumableParticles;
    private EquipmentSlot equipmentSlot;
    private NamespacedKey cameraOverlay;
    private final Map<NamespacedKey, PDCEntry<?>> pdcData = Maps.newHashMap();

    public ConfiguredItemBuilder(Material material) {
        this.material = material;
    }

    public ConfiguredItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ConfiguredItemBuilder customModelData(int cmd) {
        this.customModelData = cmd;
        return this;
    }

    public ConfiguredItemBuilder itemName(Component name) {
        this.itemName = name;
        return this;
    }

    public ConfiguredItemBuilder lore(Component... lines) {
        this.lore.addAll(Arrays.asList(lines));
        return this;
    }

    public ConfiguredItemBuilder lore(List<Component> lines) {
        this.lore.addAll(lines);
        return this;
    }

    public ConfiguredItemBuilder addLore(Component line) {
        this.lore.add(line);
        return this;
    }

    public ConfiguredItemBuilder hideAttributes() {
        this.itemFlags.add(ItemFlag.HIDE_ATTRIBUTES);
        AttributeModifier dummy = new AttributeModifier(
                new NamespacedKey(Module.getInstance(), "hide_attribute"),
                0.0,
                AttributeModifier.Operation.ADD_NUMBER
        );
        this.attributes.put(Attribute.ARMOR, dummy);
        return this;
    }

    public <T> ConfiguredItemBuilder pdc(String key, PersistentDataType<?, T> type, T value) {
        return pdc(PDCKeys.of(key), type, value);
    }

    public <T> ConfiguredItemBuilder pdc(NamespacedKey key, PersistentDataType<?, T> type, T value) {
        pdcData.put(key, new PDCEntry<>(type, value));
        return this;
    }

    public ConfiguredItemBuilder maxStackSize(int size) {
        this.maxStackSize = size;
        return this;
    }

    public ConfiguredItemBuilder maxDurability(int durability) {
        this.maxDurability = durability;
        return this;
    }

    public ConfiguredItemBuilder attackDamage(double damage) {
        this.attackDamage = damage;
        return this;
    }

    public ConfiguredItemBuilder consumable(ItemUseAnimation animation, boolean particles) {
        this.consumableAnimation = animation;
        this.consumableParticles = particles;
        return this;
    }

    public ConfiguredItemBuilder equippable(EquipmentSlot slot, NamespacedKey cameraOverlay) {
        this.equipmentSlot = slot;
        this.cameraOverlay = cameraOverlay;
        return this;
    }

    @SuppressWarnings("UnstableApiUsage")
    public ItemStack build() {
        ItemStack item = ItemStack.of(material, amount);

        item.editMeta(meta -> {
            if (customModelData != null) {
                meta.setCustomModelData(customModelData);
            }

            if (itemName != null) {
                meta.itemName(itemName.decoration(TextDecoration.ITALIC, false));
            }

            if (!lore.isEmpty()) {
                List<Component> formattedLore = lore.stream()
                        .map(line -> line.decoration(TextDecoration.ITALIC, false))
                        .collect(Collectors.toList());
                meta.lore(formattedLore);
            }

            attributes.forEach(meta::addAttributeModifier);

            if (!itemFlags.isEmpty()) {
                meta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));
            }

            if (maxStackSize != null) {
                meta.setMaxStackSize(maxStackSize);
            }

            if (equipmentSlot != null) {
                EquippableComponent equippable = meta.getEquippable();
                equippable.setSlot(equipmentSlot);
                if (cameraOverlay != null) {
                    equippable.setCameraOverlay(cameraOverlay);
                }
                meta.setEquippable(equippable);
            }

            if (!pdcData.isEmpty()) {
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                pdcData.forEach((key, entry) -> {
                    @SuppressWarnings("unchecked")
                    PDCEntry<Object> typedEntry = (PDCEntry<Object>) entry;
                    pdc.set(key, typedEntry.type, typedEntry.value);
                });
            }
        });

        if (maxDurability != null) {
            NMSItemUtil.setMaxDurability(item, maxDurability);
        }

        if (attackDamage != null) {
            NMSItemUtil.setAttackDamage(item, attackDamage);
        }

        if (consumableAnimation != null && consumableParticles != null) {
            NMSItemUtil.setConsumable(item, consumableAnimation, consumableParticles);
        }

        return item;
    }

    private record PDCEntry<T>(PersistentDataType<?, T> type, T value) { }
}