package studio.semicolon.prc.core.event.listener.game;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import studio.semicolon.prc.core.event.InteractionMatcher;

import java.util.Optional;

public class RoPAIRightClickListener implements EventSubscriber<PlayerInteractEntityEvent, EventContext.Empty> {
    public static String SCOREBOARD_TAG = "home.drone.talk.interaction";

    @Override
    public Optional<EventContext.Empty> expect(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return Optional.empty();

        return (interaction.getScoreboardTags().contains(SCOREBOARD_TAG))
                ? Optional.of(new EventContext.Empty())
                : Optional.empty();
    }

    @Override
    public EventResult onEvent(PlayerInteractEntityEvent e, EventContext.Empty empty) {
        var player = e.getPlayer();
        player.sendMessage("드론 우클릭");

        return EventResult.STOP;
    }
}