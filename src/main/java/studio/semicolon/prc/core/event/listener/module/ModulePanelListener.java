package studio.semicolon.prc.core.event.listener.module;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.util.bukkit.Potions;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffectType;
import studio.semicolon.prc.core.event.InteractionMatcher;
import studio.semicolon.prc.core.module.menu.BuildPanelMenu;
import studio.semicolon.prc.core.util.Advancements;

import java.util.Optional;

public class ModulePanelListener implements EventSubscriber<PlayerInteractEntityEvent, ModulePanelListener.Context>, InteractionMatcher {
    public record Context(double panelY) implements EventContext, EventContext.Data { }

    @Override
    public NamespacedKey key() {
        return PDCKeys.of("panel_interaction");
    }

    @Override
    public Optional<ModulePanelListener.Context> expect(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return Optional.empty();

        return matches(interaction.getPersistentDataContainer())
                ? Optional.of(new Context(entity.getY()))
                : Optional.empty();
    }

    @Override
    public EventResult onEvent(PlayerInteractEntityEvent e, ModulePanelListener.Context ctx) {
        new BuildPanelMenu(e.getPlayer(), ctx.panelY).open();

        return EventResult.STOP;
    }
}