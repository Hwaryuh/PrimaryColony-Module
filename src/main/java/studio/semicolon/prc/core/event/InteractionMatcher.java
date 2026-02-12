package studio.semicolon.prc.core.event;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public interface InteractionMatcher {
    NamespacedKey key();

    default boolean matches(PersistentDataContainer pdc) {
        return pdc.has(key(), PersistentDataType.BOOLEAN) && Boolean.TRUE.equals(pdc.get(key(), PersistentDataType.BOOLEAN));
    }
}