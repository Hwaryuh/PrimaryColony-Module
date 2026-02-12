package studio.semicolon.prc.core.event.listener.game;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.player.PlayerContext;
import io.quill.paper.player.PlayerContexts;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import studio.semicolon.prc.core.event.AdvancementMatcher;

import java.util.Optional;

public class RoPAIRightClickListener implements EventSubscriber<PlayerInteractEntityEvent, EventContext.Empty>, AdvancementMatcher {
    public static String SCOREBOARD_TAG = "home.drone.talk.interaction";
    public static String COUNTER_KEY = "ropai_right_click";

    @Override
    public String getAdvancementKey() {
        return "module/normal/happy_robot";
    }

    @Override
    public Optional<EventContext.Empty> expect(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return Optional.empty();

        return (interaction.getScoreboardTags().contains(SCOREBOARD_TAG))
                ? Optional.of(new EventContext.Empty())
                : Optional.empty();
    }

    @Override
    public EventResult onEvent(PlayerInteractEntityEvent e, EventContext.Empty ctx) {
        Player player = e.getPlayer();
        PlayerContext playerContext = PlayerContexts.ctx(player);

        player.swingMainHand();
        int count = playerContext.increment(COUNTER_KEY);
        if (count == 10) {
            grant(player);
        }

        return EventResult.STOP;
    }
}