package studio.semicolon.prc.core.event.listener.game;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.item.ItemMatcher;
import io.quill.paper.player.PlayerContext;
import io.quill.paper.player.PlayerContexts;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
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
import studio.semicolon.prc.api.constant.item.game.GameItems;
import studio.semicolon.prc.api.constant.item.game.ToolItems;
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

    private static final NamespacedKey OPENED_KEY = PDCKeys.of("buried_chest_opened");

    public static final Set<NamespacedKey> CHEST_KEYS = ImmutableSet.of(
            BURIED_HELMET, BURIED_CHESTPLATE, BURIED_LEGGINGS, BURIED_BOOTS
    );

    private static final Map<NamespacedKey, ItemStack> CHEST_REWARDS = ImmutableMap.of(
            BURIED_HELMET, GameItems.FINAL_HELMET,
            BURIED_CHESTPLATE, GameItems.FINAL_CHESTPLATE,
            BURIED_LEGGINGS, GameItems.FINAL_LEGGINGS,
            BURIED_BOOTS, GameItems.FINAL_BOOTS
    );

    private static final int CHEST_CLOSED_CMD = 42;
    private static final int CHEST_OPENED_CMD = 43;

    public sealed interface Context extends EventContext permits Context.FirstOpen, Context.AlreadyOpened, Context.Deny {
        record FirstOpen(
                NamespacedKey chestKey,
                ItemStack reward,
                Interaction interaction
        ) implements Context, EventContext.Data {
        }

        record AlreadyOpened(NamespacedKey chestKey, ItemStack reward) implements Context, EventContext.Data {
        }

        record Deny() implements Context, EventContext.Error {
            @Override
            public Component text() {
                return GameMessages.BURIED_CHEST_NEED_TOOL;
            }
        }
    }

    @Override
    public Optional<Context> expect(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return Optional.empty();

        PersistentDataContainer interactionPdc = interaction.getPersistentDataContainer();
        NamespacedKey matchedKey = findMatchingKey(interactionPdc);
        if (matchedKey == null) return Optional.empty();

        Player player = e.getPlayer();

        if (!ItemMatcher.matches(player.getInventory().getItemInMainHand(), ToolItems.PICKAXE_3)) {
            return Optional.of(new Context.Deny());
        }

        PlayerContext playerContext = PlayerContexts.ctx(player);
        if (playerContext.persistentFlag(matchedKey.getKey())) {
            return Optional.empty();
        }

        ItemStack reward = CHEST_REWARDS.get(matchedKey);
        boolean isFirstOpen = !interactionPdc.has(OPENED_KEY, PersistentDataType.BOOLEAN);

        return Optional.of(isFirstOpen
                ? new Context.FirstOpen(matchedKey, reward, interaction)
                : new Context.AlreadyOpened(matchedKey, reward));
    }

    @Override
    public EventResult onEvent(PlayerInteractEntityEvent e, Context ctx) {
        Player player = e.getPlayer();
        Location location = e.getRightClicked().getLocation();

        if (ctx instanceof Context.FirstOpen firstOpen) {
            Interaction interaction = firstOpen.interaction();
            interaction.getPersistentDataContainer().set(OPENED_KEY, PersistentDataType.BOOLEAN, true);
            updateChestDisplay(location);
            PRCSounds.BURIED_CHEST_OPEN.play(location);
            player.getWorld().spawnParticle(Particle.LARGE_SMOKE, location, 10);
        }

        NamespacedKey chestKey = switch (ctx) {
            case Context.FirstOpen f -> f.chestKey();
            case Context.AlreadyOpened f -> f.chestKey();
            default -> throw new IllegalStateException();
        };
        ItemStack reward = switch (ctx) {
            case Context.FirstOpen f -> f.reward();
            case Context.AlreadyOpened f -> f.reward();
            default -> throw new IllegalStateException();
        };

        player.getWorld().dropItemNaturally(location.clone().add(0, 0.5, 0), reward.clone());
        player.sendMessage(GameMessages.BURIED_CHEST_FOUND_ITEM);
        PlayerContexts.ctx(player).persistentFlag(chestKey.getKey(), true);

        return EventResult.STOP;
    }

    @Override
    public EventResult onError(PlayerInteractEntityEvent e, EventContext.Error error) {
        Player player = e.getPlayer();
        player.sendMessage(error.text());
        PRCSounds.DOCUMENTS_CLICK.play(player);
        return EventResult.STOP;
    }

    private NamespacedKey findMatchingKey(PersistentDataContainer pdc) {
        for (NamespacedKey key : CHEST_KEYS) {
            if (pdc.has(key, PersistentDataType.BOOLEAN)) return key;
        }
        return null;
    }

    private void updateChestDisplay(Location loc) {
        loc.getWorld().getNearbyEntities(loc, 1.5, 1.5, 1.5).forEach(entity -> {
            if (!(entity instanceof ItemDisplay itemDisplay)) return;
            ItemStack displayItem = itemDisplay.getItemStack();
            if (!displayItem.hasItemMeta()) return;
            ItemMeta meta = displayItem.getItemMeta();
            if (!meta.hasCustomModelData() || meta.getCustomModelData() != CHEST_CLOSED_CMD) return;
            meta.setCustomModelData(CHEST_OPENED_CMD);
            displayItem.setItemMeta(meta);
            itemDisplay.setItemStack(displayItem);
        });
    }
}
