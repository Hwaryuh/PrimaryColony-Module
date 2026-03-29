package studio.semicolon.prc.core.event.listener.module;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.item.ItemMatcher;
import io.quill.paper.util.bukkit.Selectors;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.constant.item.module.ModuleItems;
import studio.semicolon.prc.api.constant.text.ModuleMessages;
import studio.semicolon.prc.core.event.PDCMatcher;
import studio.semicolon.prc.core.module.menu.ModuleMarkerMenu;

import java.util.Optional;

public class ModuleMarkerListener implements EventSubscriber<PlayerInteractEvent, ModuleMarkerListener.Context>, PDCMatcher {
    public static final NamespacedKey MODULE_MARKER_KEY = PDCKeys.of("module_marker");

    public sealed interface Context extends EventContext permits Context.Add, Context.Remove {
        record Add(Player player, Location location) implements Context, EventContext.Data { }
        record Remove(Player player, Location location) implements Context, EventContext.Data { }
    }

    @Override
    public NamespacedKey getIdentityKey() {
        return MODULE_MARKER_KEY;
    }

    @Override
    public Optional<Context> expect(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK) || !(e.getHand() == EquipmentSlot.HAND)) return Optional.empty();
        Block block = e.getClickedBlock();
        if (block == null) return Optional.empty();

        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if (ItemMatcher.matchesMaterial(item, ModuleItems.MODULE_MARKER_ADD)) {
            return Optional.of(new Context.Add(player, block.getLocation()));
        }
        if (ItemMatcher.matchesMaterial(item, ModuleItems.MODULE_MARKER_REMOVE)) {
            return Optional.of(new Context.Remove(player, block.getLocation()));
        }
        return Optional.empty();
    }

    @Override
    public EventResult onEvent(PlayerInteractEvent e, Context ctx) {
        switch (ctx) {
            case Context.Add(Player player, Location location) -> {
                new ModuleMarkerMenu(player, location).open();
            }
            case Context.Remove(Player player, Location location) -> onRemove(player, location);
        }
        return EventResult.STOP;
    }

    private void onRemove(Player player, Location location) {
        location.add(0.0, 1.0, 0.0);
        ItemDisplay marker = Selectors.nearest(location, ItemDisplay.class, 1.0, i -> matches(i.getPersistentDataContainer()));
        marker.remove();
        player.sendActionBar(ModuleMessages.REMOVED_MODULE_MARKER);
    }
}
