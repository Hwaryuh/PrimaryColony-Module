package studio.semicolon.prc.core.event.listener.game;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.util.bukkit.Selectors;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import studio.semicolon.prc.api.constant.item.game.GameItems;
import studio.semicolon.prc.api.constant.sound.PRCSounds;
import studio.semicolon.prc.core.event.PDCMatcher;

import java.util.Optional;

public class StatueListener implements EventSubscriber<PlayerInteractEntityEvent, StatueListener.Context>, PDCMatcher {
    public static final NamespacedKey STATUE_KEY = PDCKeys.of("statue_interaction");

    public sealed interface Context extends EventContext permits Context.Data {
        record Data(Interaction interaction, int index) implements StatueListener.Context, EventContext.Data {
        }
    }

    @Override
    public NamespacedKey getIdentityKey() {
        return STATUE_KEY;
    }

    @Override
    public boolean matches(PersistentDataContainer pdc) {
        return pdc.has(STATUE_KEY, PersistentDataType.INTEGER);
    }

    @Override
    public Optional<StatueListener.Context> expect(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return Optional.empty();

        if (!matches(interaction.getPersistentDataContainer())) return Optional.empty();

        int index = interaction.getPersistentDataContainer().get(STATUE_KEY, PersistentDataType.INTEGER);
        return Optional.of(new Context.Data(interaction, index));
    }

    @Override
    public EventResult onEvent(PlayerInteractEntityEvent e, StatueListener.Context ctx) {
        Context.Data data = (Context.Data) ctx;
        Location location = data.interaction().getLocation();

        location.getWorld().spawnParticle(
                Particle.POOF,
                location,
                20,
                0.3, 0.3, 0.3,
                0.02
        );

        PRCSounds.SEARCH_STATUE.play(location);
        location.getWorld().dropItemNaturally(location.clone().add(0, 1, 0), GameItems.nametagOf(data.index()));
        Selectors.findAll(location, ItemDisplay.class, 1.0, d -> {
                    ItemStack item = d.getItemStack();
                    return  item.getType() == Material.PLAYER_HEAD;
                })
                .forEach(display -> display.setViewRange(display.getViewRange() == 0F ? 1.0F : 0F));

        data.interaction().remove();
        return EventResult.STOP;
    }
}
