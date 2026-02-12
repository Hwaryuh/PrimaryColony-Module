package studio.semicolon.prc.core.event.listener.advancement;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import studio.semicolon.prc.core.event.AdvancementMatcher;

import java.util.Optional;

public class PlantTouchListener implements EventSubscriber<PlayerInteractEvent, EventContext.Empty>, AdvancementMatcher {
    @Override
    public String key() {
        return "planet/normal/touch";
    }

    @Override
    public Optional<EventContext.Empty> expect(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();

        if (block == null) return Optional.empty();
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK) || !(e.getHand() == EquipmentSlot.HAND)) return Optional.empty();
        return block.getType() == Material.RED_STAINED_GLASS
                ? Optional.of(new EventContext.Empty())
                : Optional.empty();
    }

    @Override
    public EventResult onEvent(PlayerInteractEvent e, EventContext.Empty empty) {
        var player = e.getPlayer();

        grant(player);
        player.swingMainHand();
        return EventResult.STOP;
    }
}