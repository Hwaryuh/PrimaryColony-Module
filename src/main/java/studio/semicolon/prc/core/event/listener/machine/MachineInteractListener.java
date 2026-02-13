package studio.semicolon.prc.core.event.listener.machine;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.item.ItemMatcher;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import studio.semicolon.prc.api.machine.AbstractMachine;
import studio.semicolon.prc.api.machine.MachineManager;
import studio.semicolon.prc.api.constant.item.ToolItems;

import java.util.Optional;

public class MachineInteractListener implements EventSubscriber<PlayerInteractEvent, MachineInteractListener.Context> {
    public record Context(AbstractMachine machine) implements EventContext, EventContext.Data { }

    @Override
    public Optional<Context> expect(PlayerInteractEvent e) {
        if (e.getPlayer().isSneaking()) return Optional.empty();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return Optional.empty();
        if (e.getHand() != EquipmentSlot.HAND) return Optional.empty();

        Block clicked = e.getClickedBlock();
        if (clicked == null || clicked.getType() != Material.BARRIER) return Optional.empty();

        var item = e.getPlayer().getInventory().getItemInMainHand();
        if (ItemMatcher.matches(item, ToolItems.SPANNER)) return Optional.empty();

        AbstractMachine machine = MachineManager.getInstance().getMachine(clicked.getLocation());
        if (machine == null) return Optional.empty();

        return Optional.of(new Context(machine));
    }

    @Override
    public EventResult onEvent(PlayerInteractEvent e, Context ctx) {
        ctx.machine().openMenu(e.getPlayer());
        e.getPlayer().swingMainHand();
        return EventResult.TERMINATE;
    }
}