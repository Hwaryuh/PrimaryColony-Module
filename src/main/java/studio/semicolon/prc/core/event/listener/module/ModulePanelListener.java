package studio.semicolon.prc.core.event.listener.module;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import studio.semicolon.prc.core.event.PDCMatcher;
import studio.semicolon.prc.core.module.menu.BuildPanelMenu;

import java.util.Optional;

public class ModulePanelListener implements EventSubscriber<PlayerInteractEntityEvent, ModulePanelListener.Context>, PDCMatcher {
    public record Context(double panelY) implements EventContext, EventContext.Data {
    }

    @Override
    public NamespacedKey getIdentityKey() {
        return PDCKeys.of("panel_interaction");
    }

    @Override
    public Optional<ModulePanelListener.Context> expect(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        GameMode gameMode = player.getGameMode();
        if (gameMode != GameMode.ADVENTURE && gameMode != GameMode.SURVIVAL) return Optional.empty();
        Entity entity = e.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return Optional.empty();

        return matches(interaction.getPersistentDataContainer())
                ? Optional.of(new Context(entity.getY()))
                : Optional.empty();
    }

    @Override
    public EventResult onEvent(PlayerInteractEntityEvent e, Context ctx) {
        new BuildPanelMenu(e.getPlayer(), ctx.panelY).open();

        return EventResult.STOP;
    }
}
