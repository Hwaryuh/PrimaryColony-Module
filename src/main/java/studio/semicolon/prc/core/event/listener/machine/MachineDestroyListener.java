package studio.semicolon.prc.core.event.listener.machine;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.item.ItemMatcher;
import io.quill.paper.player.PlayerContext;
import io.quill.paper.player.PlayerContexts;
import kr.eme.prcMission.enums.MissionVersion;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.machine.MachineState;
import studio.semicolon.prc.api.constant.text.MachineMessages;
import studio.semicolon.prc.api.constant.item.ToolItems;
import studio.semicolon.prc.api.machine.AbstractMachine;
import studio.semicolon.prc.api.machine.MachineManager;
import studio.semicolon.prc.api.constant.sound.PRCSounds;
import studio.semicolon.prc.core.event.AdvancementMatcher;
import studio.semicolon.prc.core.util.Missions;

import java.util.Optional;

public class MachineDestroyListener implements EventSubscriber<PlayerInteractEvent, MachineDestroyListener.Context>, AdvancementMatcher {
    public static String COUNTER_KEY = "use_spanner";

    public sealed interface Context extends EventContext permits Context.Allow, Context.Deny {
        record Allow(GameMode mode, AbstractMachine machine, Location location) implements Context, EventContext.Data { }

        sealed interface Deny extends Context, EventContext.Error permits Deny.Locked, Deny.Processing, Deny.HasResult {
            record Locked() implements Deny {
                @Override public Component text() { return MachineMessages.DESTROY_LOCKED; }
            }
            record Processing() implements Deny {
                @Override public Component text() { return MachineMessages.DESTROY_PROCESSING; }
            }
            record HasResult() implements Deny {
                @Override public Component text() { return MachineMessages.DESTROY_EXIST_RESULT; }
            }
        }
    }

    @Override
    public String getAdvancementKey() {
        return "module/normal/dismantle";
    }

    @Override
    public Optional<Context> expect(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) return Optional.empty();
        if (e.getHand() != EquipmentSlot.HAND) return Optional.empty();

        Block clicked = e.getClickedBlock();
        if (clicked == null || clicked.getType() != Material.BARRIER) {
            return Optional.empty();
        }

        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        GameMode mode = player.getGameMode();

        if (!ItemMatcher.matches(item, ToolItems.SPANNER) && mode != GameMode.CREATIVE) {
            return Optional.empty();
        }

        Location location = clicked.getLocation();
        MachineManager manager = MachineManager.getInstance();
        AbstractMachine machine = manager.getMachine(location);

        if (machine == null) return Optional.empty();
        if (manager.isLocked(location)) return Optional.of(new Context.Deny.Locked());

        if (!machine.canDestroy()) {
            MachineState state = machine.loadState();
            if (state.isProcessing()) return Optional.of(new Context.Deny.Processing());
            if (state.isCompleted()) return Optional.of(new Context.Deny.HasResult());
        }

        return Optional.of(new Context.Allow(mode, machine, location));
    }

    @Override
    public EventResult onEvent(PlayerInteractEvent e, Context ctx) {
        Context.Allow allow = (Context.Allow) ctx;
        Player player = e.getPlayer();
        PlayerContext playerContext = PlayerContexts.ctx(player);

        if (allow.mode() == GameMode.SURVIVAL || allow.mode() == GameMode.ADVENTURE) {
            for (ItemStack drop : allow.machine().getDrops()) {
                allow.location().getWorld().dropItemNaturally(allow.location(), drop);
            }
            Missions.progressV1(player, "DEVICE_INTERACTION", "wrench_pickup", 1);
        }

        allow.machine().destroy();
        MachineManager.getInstance().unregister(allow.location());
        player.swingMainHand();

        int count = playerContext.increment(COUNTER_KEY);
        if (count == 10) {
            grant(player);
        }

        return EventResult.TERMINATE;
    }

    @Override
    public EventResult onError(PlayerInteractEvent e, EventContext.Error error) {
        Player player = e.getPlayer();
        PRCSounds.GLOBAL_ERROR.play(player);
        player.sendMessage(error.text());
        return EventResult.TERMINATE;
    }
}