package studio.semicolon.prc.core.game;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import studio.semicolon.prc.core.util.Attributes;

public class GravityService {
    private static final String MODIFIER_KEY = "primary_colony_gravity";
    private static final double DEFAULT_GRAVITY = 0.08;
    private static final double TARGET_GRAVITY = 0.048;

    private GravityService() {
    }

    public static void set(Player player) {
        Attributes.of(player, Attribute.GRAVITY, scope ->
                scope.add(MODIFIER_KEY, TARGET_GRAVITY - DEFAULT_GRAVITY, AttributeModifier.Operation.ADD_NUMBER));
    }

    public static void remove(Player player) {
        Attributes.of(player, Attribute.GRAVITY, scope ->
                scope.remove(MODIFIER_KEY));
    }
}
