package studio.semicolon.prc.core.event;

import org.bukkit.entity.Player;
import studio.semicolon.prc.core.util.Advancements;

public interface AdvancementMatcher {
    String getAdvancementKey();

    default void grant(Player player) {
        Advancements.grant(player, getAdvancementKey());
    }
}