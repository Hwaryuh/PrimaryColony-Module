package studio.semicolon.prc.core.event.listener.game;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import studio.semicolon.prc.api.constant.sound.PRCSounds;

import java.util.Optional;

public class AdvancementDoneListener implements EventSubscriber<PlayerAdvancementDoneEvent, EventContext.Empty> {
    @Override
    public Optional<EventContext.Empty> expect(PlayerAdvancementDoneEvent e) {
        return Optional.of(new EventContext.Empty());
    }

    @Override
    public EventResult onEvent(PlayerAdvancementDoneEvent e, EventContext.Empty ctx) {
        Player player = e.getPlayer();
        PRCSounds.GRANT_ADVANCEMENTS.play(player);

        return EventResult.STOP;
    }
}
