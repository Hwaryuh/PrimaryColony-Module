package studio.semicolon.prc.core.event.listener.game;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import studio.semicolon.prc.core.event.InteractionMatcher;

import java.util.Optional;

public class BackPackListener implements EventSubscriber<PlayerInteractEntityEvent, EventContext.Empty>, InteractionMatcher {
    @Override
    public NamespacedKey getIdentityKey() {
        return PDCKeys.of("backpack_interaction");
    }

    @Override
    public Optional<EventContext.Empty> expect(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return Optional.empty();

        return matches(interaction.getPersistentDataContainer())
                ? Optional.of(new EventContext.Empty())
                : Optional.empty();
    }

    @Override
    public EventResult onEvent(PlayerInteractEntityEvent e, EventContext.Empty ctx) {
        e.getPlayer().sendMessage("가방");

        return EventResult.STOP;
    }
}