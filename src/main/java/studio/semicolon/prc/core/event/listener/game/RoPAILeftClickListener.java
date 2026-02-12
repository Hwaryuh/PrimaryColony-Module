package studio.semicolon.prc.core.event.listener.game;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Optional;

public class RoPAILeftClickListener implements EventSubscriber<EntityDamageByEntityEvent, EventContext.Empty> {
    public static String SCOREBOARD_TAG = "home.drone.talk.interaction";

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
        player.sendMessage("드론 좌클릭");

        return EventResult.STOP;
    }
}