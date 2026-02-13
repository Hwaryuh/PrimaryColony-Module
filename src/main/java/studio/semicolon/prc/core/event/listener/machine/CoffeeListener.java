package studio.semicolon.prc.core.event.listener.machine;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.item.ItemMatcher;
import io.quill.paper.util.bukkit.Potions;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffectType;
import studio.semicolon.prc.api.constant.item.machine.CoffeeMachineItems;
import studio.semicolon.prc.core.event.AdvancementMatcher;

import java.util.Optional;

public class CoffeeListener implements EventSubscriber<PlayerItemConsumeEvent, EventContext.Empty>, AdvancementMatcher {
    @Override
    public String getAdvancementKey() {
        return "module/normal/employee";
    }

    @Override
    public Optional<EventContext.Empty> expect(PlayerItemConsumeEvent e) {
        return ItemMatcher.matches(e.getItem(), CoffeeMachineItems.COFFEE)
                ? Optional.of(new EventContext.Empty())
                : Optional.empty();
    }

    @Override
    public EventResult onEvent(PlayerItemConsumeEvent e, EventContext.Empty ctx) {
        var player = e.getPlayer();

        if (player.hasPotionEffect(PotionEffectType.HASTE)) {
            grant(player);
        }

        Potions.of(PotionEffectType.SPEED)
                .minutes(1)
                .level(2)
                .ambient(false)
                .particles(false)
                .icon(false)
                .applyTo(player);

        Potions.of(PotionEffectType.HASTE)
                .minutes(1)
                .level(2)
                .ambient(false)
                .particles(true)
                .icon(true)
                .applyTo(player);

        return EventResult.STOP;
    }
}