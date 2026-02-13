package studio.semicolon.prc.core.util;

import io.quill.paper.util.bukkit.task.Tasks;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class Players {
    public static void stopMovement(Player player, int duration) {
        if (player == null) return;

        AttributeInstance movementAttr = player.getAttribute(Attribute.MOVEMENT_SPEED);
        AttributeInstance jumpAttr = player.getAttribute(Attribute.JUMP_STRENGTH);

        if (movementAttr == null || jumpAttr == null) return;

        double originalMovement = movementAttr.getBaseValue();
        double originalJump = jumpAttr.getBaseValue();

        movementAttr.setBaseValue(0.0);
        jumpAttr.setBaseValue(0.0);

        Tasks.later(duration, () -> {
            if (!player.isOnline()) return;

            AttributeInstance restoredMovement = player.getAttribute(Attribute.MOVEMENT_SPEED);
            AttributeInstance restoredJump = player.getAttribute(Attribute.JUMP_STRENGTH);

            if (restoredMovement != null) {
                restoredMovement.setBaseValue(originalMovement);
            }
            if (restoredJump != null) {
                restoredJump.setBaseValue(originalJump);
            }
        });
    }
}