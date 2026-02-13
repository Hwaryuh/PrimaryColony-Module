package studio.semicolon.prc.core.event.listener.game;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import studio.semicolon.prc.api.constant.text.UtilMessages;
import studio.semicolon.prc.core.event.AdvancementMatcher;
import studio.semicolon.prc.core.event.InteractionMatcher;

import java.util.Optional;

public class DocumentListener implements EventSubscriber<PlayerInteractEntityEvent, EventContext.Empty>, InteractionMatcher, AdvancementMatcher {
    @Override
    public NamespacedKey getIdentityKey() {
        return PDCKeys.of("documents_interaction");
    }

    @Override
    public String getAdvancementKey() {
        return "module/normal/i_hate_studying";
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
        e.getPlayer().sendMessage(UtilMessages.WIKI_LINK);
        return EventResult.STOP;
    }
}