package studio.semicolon.prc.core.event.listener.game;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.item.ItemMatcher;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import studio.semicolon.prc.api.constant.item.ToolItems;
import studio.semicolon.prc.api.constant.sound.PRCSounds;
import studio.semicolon.prc.api.constant.text.GameMessages;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BuriedChestListener implements EventSubscriber<PlayerInteractEntityEvent, BuriedChestListener.Context> {
    public static final NamespacedKey BURIED_HELMET = PDCKeys.of("buried_chest_1");
    public static final NamespacedKey BURIED_CHESTPLATE = PDCKeys.of("buried_chest_2");
    public static final NamespacedKey BURIED_LEGGINGS = PDCKeys.of("buried_chest_3");
    public static final NamespacedKey BURIED_BOOTS = PDCKeys.of("buried_chest_4");

    private static final Set<NamespacedKey> CHEST_KEYS = ImmutableSet.of(
            BURIED_HELMET,
            BURIED_CHESTPLATE,
            BURIED_LEGGINGS,
            BURIED_BOOTS
    );

    private static final Map<NamespacedKey, ItemStack> CHEST_REWARDS = ImmutableMap.of(
            BURIED_HELMET, ItemStack.of(Material.DIAMOND_HELMET),
            BURIED_CHESTPLATE, ItemStack.of(Material.DIAMOND_CHESTPLATE),
            BURIED_LEGGINGS, ItemStack.of(Material.DIAMOND_LEGGINGS),
            BURIED_BOOTS, ToolItems.FINAL_BOOTS
    );

    private static final int CHEST_CLOSED_CMD = 42;
    private static final int CHEST_OPENED_CMD = 43;

    public sealed interface Context extends EventContext permits Context.Allow, Context.Deny {
        record Allow(ItemStack reward, Interaction interaction) implements Context, EventContext.Data { }
        record Deny() implements Context, EventContext.Error {
            @Override public Component text() { return GameMessages.BURIED_CHEST_NEED_TOOL; }
        }
    }

    @Override
    public Optional<Context> expect(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return Optional.empty();

        PersistentDataContainer pdc = interaction.getPersistentDataContainer();

        for (NamespacedKey key : CHEST_KEYS) {
            if (matchesKey(pdc, key)) {
                if (!ItemMatcher.matches(e.getPlayer().getInventory().getItemInMainHand(), ToolItems.PICKAXE_3)) {
                    return Optional.of(new Context.Deny());
                }

                return Optional.of(new Context.Allow(CHEST_REWARDS.get(key), interaction));
            }
        }
        return Optional.empty();
    }

    @Override
    public EventResult onEvent(PlayerInteractEntityEvent e, Context ctx) {
        Context.Allow allow = (Context.Allow) ctx;
        Player player = e.getPlayer();
        Location location = allow.interaction().getLocation();

        updateChestDisplay(location);

        PRCSounds.BURIED_CHEST_OPEN.play(location);
        player.getWorld().dropItemNaturally(location.clone().add(0, 0.5, 0), allow.reward());
        player.getWorld().spawnParticle(Particle.LARGE_SMOKE, location, 10);
        player.sendMessage(GameMessages.BURIED_CHEST_FOUND_ITEM);

        allow.interaction().remove();
        return EventResult.STOP;
    }

    @Override
    public EventResult onError(PlayerInteractEntityEvent e, EventContext.Error error) {
        e.getPlayer().sendMessage(error.text());
        return EventResult.STOP;
    }

    private void updateChestDisplay(Location loc) {
        loc.getWorld().getNearbyEntities(loc, 1.5, 1.5, 1.5).forEach(entity -> {
            if (entity instanceof ItemDisplay itemDisplay) {
                ItemStack displayItem = itemDisplay.getItemStack();
                if (displayItem.hasItemMeta()) {
                    ItemMeta meta = displayItem.getItemMeta();
                    if (meta.hasCustomModelData() && meta.getCustomModelData() == CHEST_CLOSED_CMD) {
                        meta.setCustomModelData(CHEST_OPENED_CMD);
                        displayItem.setItemMeta(meta);
                        itemDisplay.setItemStack(displayItem);
                    }
                }
            }
        });
    }

    private boolean matchesKey(PersistentDataContainer pdc, NamespacedKey key) {
        return pdc.has(key, PersistentDataType.BOOLEAN)
                && Boolean.TRUE.equals(pdc.get(key, PersistentDataType.BOOLEAN));
    }
}