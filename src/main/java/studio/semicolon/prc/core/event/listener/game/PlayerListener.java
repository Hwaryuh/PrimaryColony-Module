package studio.semicolon.prc.core.event.listener.game;

import io.quill.paper.item.ItemMatcher;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.constant.item.module.ModuleItems;
import studio.semicolon.prc.api.constant.text.GameMessages;
import studio.semicolon.prc.core.game.RandomSlotSelector;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        var player = event.getPlayer();
        if (player.getGameMode().isInvulnerable()) return;

        RandomSlotSelector selector = RandomSlotSelector.builder()
                .excludeSlots(36, 37, 38, 39)
                .excludeIf(item -> item.getType() == Material.DIAMOND_HELMET)
                .excludeIf(item -> item.getType() == Material.DIAMOND_CHESTPLATE)
                .excludeIf(item -> item.getType() == Material.DIAMOND_LEGGINGS)
                .excludeIf(item -> item.getType() == Material.DIAMOND_BOOTS)
                .excludeIf(item -> ItemMatcher.matchesMaterial(item, ModuleItems.MODULE_MARKER_ADD))
                .excludeIf(item -> ItemMatcher.matchesMaterial(item, ModuleItems.MODULE_MARKER_REMOVE))
                .excludeIf(ItemMatcher.matcherOf(Material.ORANGE_DYE, 5))
                .excludeIf(ItemMatcher.matcherOf(Material.ORANGE_DYE, 6))
                .excludeIf(ItemMatcher.matcherOf(Material.ORANGE_DYE, 7))
                .excludeIf(ItemMatcher.matcherOf(Material.ORANGE_DYE, 10))
                .build();

        var inventory = player.getInventory();
        selector.select(inventory).ifPresent(slot -> {
            ItemStack lost = inventory.getItem(slot);
            if (lost != null) {
                player.sendMessage(GameMessages.getLostItemMessage(lost));
                Sound sound = Sound.sound()
                        .type(Key.key("minecraft:item.armor.equip_leather"))
                        .volume(1.0F)
                        .pitch(0.75F)
                        .build();
                player.playSound(sound);
            }
            inventory.setItem(slot, ItemStack.empty());
        });
    }
}
