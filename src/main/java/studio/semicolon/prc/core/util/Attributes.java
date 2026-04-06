package studio.semicolon.prc.core.util;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import studio.semicolon.prc.PluginNamespace;

import java.util.function.Consumer;

public class Attributes {
    private final AttributeInstance instance;

    public Attributes(AttributeInstance instance) {
        this.instance = instance;
    }

    public double getBase() {
        return instance.getBaseValue();
    }

    public void setBase(double value) {
        instance.setBaseValue(value);
    }

    public void add(String key, double amount, AttributeModifier.Operation operation) {
        remove(key);
        instance.addModifier(new AttributeModifier(PluginNamespace.key(key), amount, operation));
    }

    public void remove(String key) {
        NamespacedKey namespacedKey = PluginNamespace.key(key);
        for (AttributeModifier modifier : instance.getModifiers()) {
            if (modifier.getKey().equals(namespacedKey)) {
                instance.removeModifier(modifier);
            }
        }
    }

    public static void of(LivingEntity entity, Attribute attribute, Consumer<Attributes> block) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) return;
        block.accept(new Attributes(instance));
    }
}
