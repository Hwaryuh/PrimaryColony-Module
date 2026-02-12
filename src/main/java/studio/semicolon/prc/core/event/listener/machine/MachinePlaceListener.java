package studio.semicolon.prc.core.event.listener.machine;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.item.ItemMatcher;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import studio.semicolon.prc.api.machine.AbstractMachine;
import studio.semicolon.prc.api.machine.MachineManager;
import studio.semicolon.prc.core.constant.item.machine.MachineItems;
import studio.semicolon.prc.core.constant.sound.PRCSounds;
import studio.semicolon.prc.core.constant.text.MachineMessages;
import studio.semicolon.prc.core.machine.coffee.CoffeeMachine;
import studio.semicolon.prc.core.machine.furnace.FurnaceMachine;
import studio.semicolon.prc.core.machine.grinder.GrinderMachine;
import studio.semicolon.prc.core.machine.print.PrintMachine;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public class MachinePlaceListener implements EventSubscriber<PlayerInteractEvent, MachinePlaceListener.Context> {
    private static final Set<Material> INTERACTABLE_BLOCKS = EnumSet.of(
            Material.CHEST, Material.BARREL, Material.TRAPPED_CHEST,
            Material.FURNACE, Material.BLAST_FURNACE, Material.SMOKER,
            Material.CRAFTING_TABLE, Material.ANVIL, Material.ENCHANTING_TABLE,
            Material.BREWING_STAND, Material.BEACON, Material.CARTOGRAPHY_TABLE,
            Material.ENDER_CHEST, Material.GRINDSTONE, Material.HOPPER,
            Material.LECTERN, Material.LOOM, Material.SMITHING_TABLE,
            Material.STONECUTTER
    );

    public sealed interface Context extends EventContext permits Context.Allow, Context.Deny {
        record Allow(boolean creative, ItemStack item, Location targetLoc, BlockFace face, Block clicked)
                implements Context, EventContext.Data { }

        sealed interface Deny extends Context, EventContext.Error permits Deny.NotOnStorage, Deny.NotEmpty, Deny.AlreadyExists {
            record NotOnStorage() implements Deny {
                @Override public Component text() {
                    return MachineMessages.PLACE_NOT_ON_STORAGE;
                }
            }
            record NotEmpty() implements Deny {
                @Override public Component text() { return MachineMessages.PLACE_INVALID; }
            }
            record AlreadyExists() implements Deny {
                @Override public Component text() { return MachineMessages.PLACE_EXISTS; }
            }
        }
    }

    @Override
    public Optional<Context> expect(PlayerInteractEvent e) {
        var clicked = e.getClickedBlock();

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return Optional.empty();
        if (e.getHand() != EquipmentSlot.HAND) return Optional.empty();
        if (e.getItem() == null) return Optional.empty();
        if (clicked == null) return Optional.empty();
        if (!MachineItems.isMachineItem(e.getItem())) return Optional.empty();

        var targetLoc = clicked.getRelative(e.getBlockFace()).getLocation();

        if (e.getBlockFace() != BlockFace.UP) return Optional.empty();
        if (INTERACTABLE_BLOCKS.contains(clicked.getType())) return Optional.empty();

        if (clicked.getType() != Material.WHITE_CONCRETE) {
            return Optional.of(new Context.Deny.NotOnStorage());
        }
        if (!clicked.getRelative(e.getBlockFace()).getType().isAir()) {
            return Optional.of(new Context.Deny.NotEmpty());
        }
        if (MachineManager.getInstance().hasMachine(targetLoc)) {
            return Optional.of(new Context.Deny.AlreadyExists());
        }

        boolean creative = e.getPlayer().getGameMode() == GameMode.CREATIVE;
        return Optional.of(new Context.Allow(creative, e.getItem(), targetLoc, e.getBlockFace(), clicked));
    }

    @Override
    public EventResult onEvent(PlayerInteractEvent e, Context ctx) {
        Context.Allow allow = (Context.Allow) ctx;
        Player player = e.getPlayer();

        AbstractMachine machine = createMachine(allow.item(), allow.targetLoc());
        if (machine == null) return EventResult.TERMINATE;
        if (!machine.place(player)) return EventResult.TERMINATE;

        if (!allow.creative()) {
            ItemStack item = allow.item();
            item.setAmount(item.getAmount() - 1);
        }

        return EventResult.TERMINATE;
    }

    @Override
    public EventResult onError(PlayerInteractEvent e, EventContext.Error error) {
        Player player = e.getPlayer();
        player.sendActionBar(error.text());
        PRCSounds.GLOBAL_ERROR.play(player);
        return EventResult.TERMINATE;
    }

    private AbstractMachine createMachine(ItemStack item, Location location) {
        if (ItemMatcher.matches(item, MachineItems.COFFEE_MACHINE)) {
            return new CoffeeMachine(location);
        }
        if (ItemMatcher.matches(item, MachineItems.PRINT_MACHINE)) {
            return new PrintMachine(location);
        }
        if (ItemMatcher.matches(item, MachineItems.FURNACE_MACHINE)) {
            return new FurnaceMachine(location);
        }
        if (ItemMatcher.matches(item, MachineItems.GRINDER_MACHINE)) {
            return new GrinderMachine(location);
        }
        return null;
    }
}