package studio.semicolon.prc.core.event.listener.advancement;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import studio.semicolon.prc.core.event.AdvancementMatcher;

import java.util.Optional;

public class PlayerFallListener implements EventSubscriber<EntityDamageEvent, PlayerFallListener.Context>, AdvancementMatcher {
    public record Context(Player player) implements EventContext, EventContext.Data { }

    @Override
    public String key() {
        return "planet/hidden/nine_point_eight";
    }

    @Override
    public Optional<Context> expect(EntityDamageEvent e) {
        return (e.getEntity() instanceof Player player && e.getCause() == EntityDamageEvent.DamageCause.FALL)
                ? Optional.of(new Context(player))
                : Optional.empty();
    }

    @Override
    public EventResult onEvent(EntityDamageEvent e, PlayerFallListener.Context ctx) {
        Player player = ctx.player;
        if (player.getFallDistance() > 10.0) {
            grant(player);
        }
        return EventResult.STOP;
    }
}