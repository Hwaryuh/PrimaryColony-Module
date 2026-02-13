package studio.semicolon.prc.core.event.listener.machine;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.player.PlayerContext;
import io.quill.paper.player.PlayerContexts;
import io.quill.paper.util.bukkit.Selectors;
import io.quill.paper.util.bukkit.Ticks;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import studio.semicolon.prc.api.constant.sound.PRCSounds;
import studio.semicolon.prc.core.event.AdvancementMatcher;
import studio.semicolon.prc.core.util.Titles;

import java.util.Optional;

public class EntryModuleListener implements EventSubscriber<PlayerInteractEntityEvent, EntryModuleListener.Context>, AdvancementMatcher {
    private static final NamespacedKey KEY_JOIN = PDCKeys.of("entry_interaction_join");
    private static final NamespacedKey KEY_QUIT = PDCKeys.of("entry_interaction_quit");

    private static final String COOLDOWN_KEY = "entry_module_cooldown";
    private static final int COOLDOWN = Ticks.sec2Ticks(2.0);

    private static final double Y_OFFSET = -1.0;

    public sealed interface Context extends EventContext permits Context.Join, Context.Quit {
        Interaction target();

        record Join(Interaction target) implements Context, EventContext.Data { }
        record Quit(Interaction target) implements Context, EventContext.Data { }
    }

    @Override
    public String getAdvancementKey() {
        return "planet/normal/frozen";
    }

    @Override
    public Optional<Context> expect(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (!(entity instanceof Interaction interaction)) {
            return Optional.empty();
        }

        PersistentDataContainer pdc = interaction.getPersistentDataContainer();

        if (pdc.has(KEY_JOIN, PersistentDataType.BOOLEAN)) {
            Interaction target = Selectors.nearest(interaction, Interaction.class, 5.5,
                    i -> {
                        PersistentDataContainer targetPdc = i.getPersistentDataContainer();
                        return targetPdc.has(KEY_QUIT, PersistentDataType.BOOLEAN) &&
                                Boolean.TRUE.equals(targetPdc.get(KEY_QUIT, PersistentDataType.BOOLEAN));
                    }
            );

            if (target != null) return Optional.of(new Context.Join(target));
        }

        if (pdc.has(KEY_QUIT, PersistentDataType.BOOLEAN)) {
            Interaction target = Selectors.nearest(interaction, Interaction.class, 5.5,
                    i -> {
                        PersistentDataContainer targetPdc = i.getPersistentDataContainer();
                        return targetPdc.has(KEY_JOIN, PersistentDataType.BOOLEAN) &&
                                Boolean.TRUE.equals(targetPdc.get(KEY_JOIN, PersistentDataType.BOOLEAN));
                    }
            );

            if (target != null) return Optional.of(new Context.Quit(target));
        }

        return Optional.empty();
    }

    @Override
    public EventResult onEvent(PlayerInteractEntityEvent e, Context ctx) {
        Player player = e.getPlayer();
        PlayerContext playerContext = PlayerContexts.ctx(player);

        if (playerContext.cooldown(COOLDOWN_KEY)) return EventResult.STOP;

        switch (ctx) {
            case Context.Join join -> onJoin(player);
            case Context.Quit quit -> onQuit(player);
        }

        Location teleportLoc = getTeleportLocation(ctx.target(), player);
        Titles.fade(player, () -> player.teleport(teleportLoc));

        playerContext.cooldown(COOLDOWN_KEY, COOLDOWN);
        PRCSounds.MODULE_INTERACT_ENTRY.play(player);

        return EventResult.STOP;
    }

    private void onJoin(Player player) {
        if (player.getFreezeTicks() > 0) {
            player.setFreezeTicks(0);
        }
    }

    private void onQuit(Player player) {
        if (!hasAnyArmor(player)) {
//            player.sendMessage(GameMessages.COLD_OUTSIDE);
            grant(player);
            player.setFreezeTicks(221112);
        }
    }

    private boolean hasAnyArmor(Player player) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            if (item != null && item.getType() != Material.AIR) {
                return true;
            }
        }
        return false;
    }

    private Location getTeleportLocation(Interaction target, Player player) {
        Location loc = target.getLocation().clone();
        loc.setYaw(player.getLocation().getYaw());
        loc.setPitch(player.getLocation().getPitch());
        loc.add(0, Y_OFFSET, 0);
        return loc;
    }
}