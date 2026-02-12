package studio.semicolon.prc.core.event.listener.advancement;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import studio.semicolon.prc.core.event.AdvancementMatcher;

import java.util.Optional;

public class GameModeChangeListener implements EventSubscriber<PlayerGameModeChangeEvent, EventContext.Empty>, AdvancementMatcher {
    @Override
    public String key() {
        return "planet/hidden/gamemode";
    }

    @Override
    public Optional<EventContext.Empty> expect(PlayerGameModeChangeEvent e) {
        return e.getNewGameMode() == GameMode.CREATIVE
                ? Optional.of(new EventContext.Empty())
                : Optional.empty();
    }

    @Override
    public EventResult onEvent(PlayerGameModeChangeEvent e, EventContext.Empty empty) {
        grant(e.getPlayer());
        return EventResult.STOP;
    }
}