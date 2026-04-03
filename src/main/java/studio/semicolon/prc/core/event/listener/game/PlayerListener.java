package studio.semicolon.prc.core.event.listener.game;

import io.quill.paper.item.ItemMatcher;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.constant.item.module.ModuleItems;
import studio.semicolon.prc.core.game.RandomSlotSelector;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getPlayer();
        if (player.getGameMode().isInvulnerable()) return;

        RandomSlotSelector selector = RandomSlotSelector.builder()
                .excludeSlots(36, 37, 38, 39)
                .excludeIf(item -> item.getType() == Material.DIAMOND_HELMET)
                .excludeIf(item -> item.getType() == Material.DIAMOND_CHESTPLATE)
                .excludeIf(item -> item.getType() == Material.DIAMOND_LEGGINGS)
                .excludeIf(item -> item.getType() == Material.DIAMOND_BOOTS)
                .excludeIf(item -> ItemMatcher.matches(item, ModuleItems.MODULE_MARKER_ADD))
                .excludeIf(item -> ItemMatcher.matches(item, ModuleItems.MODULE_MARKER_REMOVE))
                .build();

        var inventory = player.getInventory();
        selector.select(inventory).ifPresent(slot -> inventory.setItem(slot, ItemStack.empty()));
    }
}
