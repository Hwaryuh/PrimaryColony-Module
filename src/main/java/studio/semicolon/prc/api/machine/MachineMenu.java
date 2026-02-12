package studio.semicolon.prc.api.machine;

import io.quill.paper.menu.InventoryMenu;
import io.quill.paper.util.bukkit.task.Tasks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jetbrains.annotations.NotNull;
import studio.semicolon.prc.core.constant.item.ETCItems;
import studio.semicolon.prc.core.constant.sound.PRCSounds;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public abstract class MachineMenu extends InventoryMenu {
    protected final AbstractMachine machine;
    protected MachineState state;

    protected <V extends org.bukkit.inventory.InventoryView, B extends InventoryViewBuilder<@NotNull V>> MachineMenu(
            Player player,
            AbstractMachine machine,
            MenuType.Typed<@NotNull V, @NotNull B> menuType) {
        super(player, menuType, machine.getTitleForState(machine.loadState().getState()));
        this.machine = machine;
        this.state = machine.loadState();
    }

    // State

    protected void syncState() {
        state = machine.loadState();
        Tasks.run(this::updateUI);
    }

    protected abstract void updateUI();

    protected final void startCrafting(int durationSeconds, String resultItemID) {
        if (!validateIngredients()) {
            onValidationFailed();
            return;
        }

        consumeIngredients();
        PRCSounds.GLOBAL_CLICK.play(player);

        state.startProcessing(durationSeconds, resultItemID);
        machine.saveState(state);
        updateUI();
        machine.getCraftingStartSound().play(machine.getLocation());
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        MachineManager.getInstance().unlock(machine.getLocation());
    }

    protected void returnItem(ItemStack item) {
        if (item == null || item.getType().isAir()) return;

        HashMap<Integer, ItemStack> remaining = player.getInventory().addItem(item);
        if (!remaining.isEmpty()) {
            for (ItemStack drop : remaining.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
        }
    }

    protected boolean validateIngredients() {
        return true;
    }

    protected void onValidationFailed() { }

    protected void consumeIngredients() { }

    protected abstract int getResultSlot();

    protected abstract ItemStack createResult();

    protected void renderResult() {
        if (!state.isCompleted()) return;

        ItemStack result = createResult();

        setButton(getResultSlot(), button(result)
                .onAnyClick(ctx -> {
                    if (ctx.isPickup() || ctx.isMove()) {
                        state.setState(MachineState.State.IDLE);
                        machine.saveState(state);

                        Tasks.run(() -> {
                            onTakeResult();
                            updateUI();
                        });
                    }
                })
                .cancelIf(ctx -> !ctx.isPickup() && !ctx.isMove())
                .build()
        );
    }

    protected void onTakeResult() { }

    protected void alert(Component message) {
        player.sendMessage(message);
        PRCSounds.GLOBAL_ERROR.play(player);
    }

    protected ItemStack getTimerItem(int remainingSeconds) {
        ItemStack timer = ETCItems.MACHINE_GUIDE_ITEM.clone();
        timer.editMeta(meta -> {
            meta.itemName(Component.text("남은 시간: " + remainingSeconds + "초", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(Component.text("클릭하여 갱신", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        });
        return timer;
    }

    protected ItemStack createLockedSlot() {
        return ETCItems.BLOCKED_SLOT_ITEM;
    }
}