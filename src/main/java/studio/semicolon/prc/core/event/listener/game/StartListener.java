package studio.semicolon.prc.core.event.listener.game;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.player.PlayerContext;
import io.quill.paper.player.PlayerContexts;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import studio.semicolon.prc.core.event.AdvancementMatcher;

import java.util.Optional;

public class StartListener implements EventSubscriber<EntityDamageByEntityEvent, EventContext.Empty> {
    public static String SCOREBOARD_TAG = "start.interaction";

    @Override
    public Optional<EventContext.Empty> expect(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();
        if (!(entity instanceof Interaction interaction)) return Optional.empty();

        return (interaction.getScoreboardTags().contains(SCOREBOARD_TAG))
                ? Optional.of(new EventContext.Empty())
                : Optional.empty();
    }

    @Override
    public EventResult onEvent(EntityDamageByEntityEvent e, EventContext.Empty ctx) {
        if (!(e.getDamager() instanceof Player player)) return EventResult.PASS;
        player.setOp(true);

        return EventResult.STOP;
    }
}
