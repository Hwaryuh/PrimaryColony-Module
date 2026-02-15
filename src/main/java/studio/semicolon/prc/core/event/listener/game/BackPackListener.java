package studio.semicolon.prc.core.event.listener.game;

import com.google.common.collect.Lists;
import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.player.PlayerContext;
import io.quill.paper.player.PlayerContexts;
import io.quill.paper.util.bukkit.Titles;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import io.quill.paper.util.bukkit.task.TaskChain;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import studio.semicolon.prc.api.constant.item.ToolItems;
import studio.semicolon.prc.api.constant.item.module.ModuleItems;
import studio.semicolon.prc.api.constant.text.GameMessages;
import studio.semicolon.prc.core.event.AdvancementMatcher;
import studio.semicolon.prc.core.event.InteractionMatcher;
import studio.semicolon.prc.core.util.Players;

import java.util.List;
import java.util.Optional;

public class BackPackListener implements EventSubscriber<PlayerInteractEntityEvent, BackPackListener.Context>, InteractionMatcher, AdvancementMatcher {
    public static final NamespacedKey BACKPACK_KEY = PDCKeys.of("backpack_interaction");
    public static final NamespacedKey BACKPACK_SPECIAL = PDCKeys.of("backpack_special");
    public static final String FLAG_KEY = "backpack_item_received";
    public static final String COUNTER_KEY = "backpack_click";

    public sealed interface Context extends EventContext permits Context.First, Context.Received {
        record First(Player player, PlayerContext playerContext, Interaction interaction, boolean isSpecial) implements Context, EventContext.Data { }
        record Received(Player player, PlayerContext playerContext) implements Context, EventContext.Data { }
    }

    @Override
    public NamespacedKey getIdentityKey() {
        return BACKPACK_KEY;
    }

    @Override
    public String getAdvancementKey() {
        return "module/normal/one_per_person";
    }

    @Override
    public Optional<BackPackListener.Context> expect(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return Optional.empty();
        if (!matches(interaction.getPersistentDataContainer())) return Optional.empty();

        Player player = e.getPlayer();
        PlayerContext playerContext = PlayerContexts.ctx(player);

        if (playerContext.persistentFlag(FLAG_KEY)) {
            return Optional.of(new Context.Received(player, playerContext));
        } else {
            boolean isSpecial = interaction.getPersistentDataContainer().getOrDefault(BACKPACK_SPECIAL, PersistentDataType.BOOLEAN, false);
            return Optional.of(new Context.First(player, playerContext, interaction, isSpecial));
        }
    }

    @Override
    public EventResult onEvent(PlayerInteractEntityEvent e, BackPackListener.Context ctx) {
        if (ctx instanceof Context.First(Player player, PlayerContext playerContext, Interaction interaction, boolean isSpecial)) {
            playerContext.persistentFlag(FLAG_KEY, true);
            Players.stopMovement(player, 60);

            if (isSpecial) {
                interaction.getPersistentDataContainer().set(BACKPACK_SPECIAL, PersistentDataType.BOOLEAN, false);
            }

            new TaskChain() {
                @Override
                protected void define() {
                    run(() -> {
                        Titles.show(player, GameMessages.getBackPackSearchingMessage(1), Component.empty(), 1.0, 1.5, 0);
                        player.playSound(player, Sound.ITEM_BUNDLE_DROP_CONTENTS, 1.0f, 0.7f);
                    });
                    delay(20);
                    run(() -> {
                        Titles.show(player, GameMessages.getBackPackSearchingMessage(2), Component.empty(), 0, 1.5, 0);
                        player.playSound(player, Sound.ITEM_BUNDLE_REMOVE_ONE, 1.0f, 0.65f);
                    });
                    delay(20);
                    run(() -> {
                        Titles.show(player, GameMessages.getBackPackSearchingMessage(3), Component.empty(), 0, 1.0, 1.0);
                        player.playSound(player, Sound.ITEM_BUNDLE_REMOVE_ONE, 1.0f, 0.8f);
                    });
                    delay(20);
                    run(() -> {
                        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);

                        List<ItemStack> items = Lists.newArrayList(List.of(
                                ToolItems.PICKAXE_1,
                                ToolItems.WEAPON_PIPE,
                                ItemStack.of(Material.CHAINMAIL_HELMET),
                                ItemStack.of(Material.CHAINMAIL_CHESTPLATE),
                                ItemStack.of(Material.CHAINMAIL_LEGGINGS),
                                ItemStack.of(Material.CHAINMAIL_BOOTS)
                        ));

                        if (isSpecial) {
                            items.add(ModuleItems.MINE);
                        }

                        player.getInventory().addItem(items.toArray(new ItemStack[0]));
                    });
                }
            }.start();
        } else if (ctx instanceof Context.Received(Player player, PlayerContext playerContext)) {
            int count = playerContext.increment(COUNTER_KEY);
            if (count == 10) {
                grant(player);
            }
        }

        return EventResult.STOP;
    }
}