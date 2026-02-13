package studio.semicolon.prc.core.machine.coffee;

import io.quill.paper.item.ItemMatcher;
import io.quill.paper.menu.DragPolicy;
import io.quill.paper.menu.button.ClickContext;
import io.quill.paper.menu.button.DynamicButton;
import io.quill.paper.menu.slot.SlotFilter;
import io.quill.paper.util.bukkit.task.Tasks;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import studio.semicolon.prc.api.constant.text.MachineMessages;
import studio.semicolon.prc.api.constant.item.machine.CoffeeMachineItems;
import studio.semicolon.prc.api.constant.sound.PRCSounds;
import studio.semicolon.prc.api.machine.MachineMenu;
import studio.semicolon.prc.api.machine.MachineState;
import studio.semicolon.prc.api.machine.AbstractMachine;

@SuppressWarnings({"UnstableApiUsage", "UnnecessaryUnicodeEscape"})
public class CoffeeMachineMenu extends MachineMenu {
    private final int[] TAT = { 120, 90, 10 }; // AG, AU, TI

    private final int BREW_BUTTON_SLOT = 12;
    private final int MUG_SLOT = 39;
    private final int BEAN_SLOT = 52;

    public CoffeeMachineMenu(Player player, AbstractMachine machine) {
        super(player, machine, MenuType.GENERIC_9X6);
    }

    @Override
    protected void render() {
        syncState();
        setDragPolicy(DragPolicy.PREVENT_TOP_ONLY);

        if (state.isCompleted()) {
            renderResult();
        } else if (state.isIdle()) {
            setDynamicButton(MUG_SLOT, new DynamicButton() {
                @Override
                public ItemStack createIcon(ClickContext ctx) {
                    ItemStack current = ctx.getSlotItem();
                    if (ItemMatcher.matches(current, CoffeeMachineItems.MUG)) return current;
                    return ItemStack.empty();
                }

                @Override
                public void onClick(ClickContext ctx) { }

                @Override
                public boolean shouldCancel(ClickContext ctx) {
                    if (ctx.isPlace()) {
                        ItemStack cursor = ctx.getCursor();
                        ItemStack slotItem = ctx.getSlotItem();

                        if (!ItemMatcher.matches(cursor, CoffeeMachineItems.MUG)) return true;
                        if (slotItem != null && !slotItem.getType().isAir()) return true;

                        if (cursor.getAmount() > 1) {
                            ItemStack toPlace = cursor.clone();
                            toPlace.setAmount(1);
                            ctx.event().getClickedInventory().setItem(ctx.event().getSlot(), toPlace);
                            cursor.setAmount(cursor.getAmount() - 1);
                            return true;
                        }

                        PRCSounds.MACHINE_INSERT_ITEM.play(player);
                        return false;
                    }

                    if (ctx.isPickup() || ctx.isMove()) {
                        ItemStack slotItem = ctx.getSlotItem();
                        return !ItemMatcher.matches(slotItem, CoffeeMachineItems.MUG);
                    }

                    if (ctx.isHotbarSwap()) {
                        ItemStack hotbarItem = player.getInventory().getItem(ctx.event().getHotbarButton());
                        ItemStack slotItem = ctx.getSlotItem();

                        if (hotbarItem == null || hotbarItem.getType().isAir()) {
                            return !ItemMatcher.matches(slotItem, CoffeeMachineItems.MUG);
                        }

                        if (!ItemMatcher.matches(hotbarItem, CoffeeMachineItems.MUG)) return true;
                        if (slotItem != null && !slotItem.getType().isAir()) return true;

                        ItemStack toPlace = hotbarItem.clone();
                        toPlace.setAmount(1);
                        ctx.event().getClickedInventory().setItem(ctx.event().getSlot(), toPlace);
                        hotbarItem.setAmount(hotbarItem.getAmount() - 1);
                        PRCSounds.MACHINE_INSERT_ITEM.play(player);

                        return true;
                    }

                    return true;
                }
            });
        }

        setSlotFilter(BEAN_SLOT, new SlotFilter() {
            @Override
            public boolean canPlace(ItemStack item, InventoryClickEvent event) {
                return isCoffeeBean(item);
            }

            @Override
            public Integer getMaxPlaceAmount() {
                return 1;
            }

            @Override
            public void onPlaced() {
                PRCSounds.MACHINE_INSERT_ITEM.play(player);
            }
        });
        setButton(BREW_BUTTON_SLOT, button(CoffeeMachineItems.START_BUTTON)
                .onLeftClick(ctx -> {
                    if (state.isProcessing()) {
                        syncState();
                        PRCSounds.GLOBAL_CLICK.play(player);
                    } else if (state.isCompleted()) {
                        alert(MachineMessages.COFFEE_EXIST_RESULT);
                    } else if (state.isIdle()) {
                        tryStartBrewing();
                    }
                })
                .cancelAll()
                .build()
        );
        onPlayerInventory(builder -> builder
                .onShiftClick(ctx -> {
                    if (!state.isIdle()) return;

                    ItemStack clicked = ctx.getSlotItem();
                    if (ItemMatcher.matches(clicked, CoffeeMachineItems.MUG)) {
                        ItemStack mugSlotItem = getInventory().getItem(MUG_SLOT);
                        if (mugSlotItem == null || mugSlotItem.getType().isAir()) {
                            ItemStack toMove = clicked.clone();
                            toMove.setAmount(1);
                            getInventory().setItem(MUG_SLOT, toMove);
                            clicked.setAmount(clicked.getAmount() - 1);
                            PRCSounds.MACHINE_INSERT_ITEM.play(player);
                            return;
                        }
                    }

                    tryShiftClickToSlot(clicked, BEAN_SLOT);
                })
                .cancelOn(ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT)
        );
    }

    @Override
    protected void updateUI() {
        if (state.isProcessing()) {
            setTitle(machine.getTitleForState(MachineState.State.PROCESSING));
            setButton(BREW_BUTTON_SLOT, button(getTimerItem(state.getRemainingSeconds()))
                    .onLeftClick(ctx -> {
                        syncState();
                        PRCSounds.GLOBAL_CLICK.play(player);
                    })
                    .cancelAll()
                    .build()
            );
        } else if (state.isCompleted()) {
            setTitle(machine.getTitleForState(MachineState.State.IDLE));
            setButton(BREW_BUTTON_SLOT, button(CoffeeMachineItems.START_BUTTON)
                    .onLeftClick(ctx -> {
                        alert(MachineMessages.COFFEE_EXIST_RESULT);
                    })
                    .cancelAll()
                    .build()
            );
            renderResult();
        } else {
            setTitle(machine.getTitleForState(state.getState()));
            setButton(BREW_BUTTON_SLOT, button(CoffeeMachineItems.START_BUTTON)
                    .onLeftClick(ctx -> tryStartBrewing())
                    .cancelAll()
                    .build()
            );
        }
    }

    private void clearSlots() {
        Tasks.run(() -> setSlotEmpty(MUG_SLOT, BEAN_SLOT));
    }

    private void tryStartBrewing() {
        this.clearSlots();
        startCrafting(getBrewTime(getInventory().getItem(BEAN_SLOT)), null);
    }

    @Override
    protected int getResultSlot() {
        return MUG_SLOT;
    }

    @Override
    protected boolean validateIngredients() {
        ItemStack mug = getInventory().getItem(MUG_SLOT);
        ItemStack bean = getInventory().getItem(BEAN_SLOT);
        return ItemMatcher.matches(mug, CoffeeMachineItems.MUG) && isCoffeeBean(bean);
    }

    @Override
    protected void onValidationFailed() {
        alert(MachineMessages.COFFEE_INSUFFICIENT_INGREDIENTS);
    }

    @Override
    protected void onTakeResult() {
        this.clearSlots();
    }

    private boolean isCoffeeBean(ItemStack item) {
        return ItemMatcher.matches(item, CoffeeMachineItems.COFFEE_BEAN_AG) || ItemMatcher.matches(item, CoffeeMachineItems.COFFEE_BEAN_AU) || ItemMatcher.matches(item, CoffeeMachineItems.COFFEE_BEAN_TI);
    }

    private int getBrewTime(ItemStack bean) {
        if (ItemMatcher.matches(bean, CoffeeMachineItems.COFFEE_BEAN_AG)) return TAT[0];
        if (ItemMatcher.matches(bean, CoffeeMachineItems.COFFEE_BEAN_AU)) return TAT[1];
        if (ItemMatcher.matches(bean, CoffeeMachineItems.COFFEE_BEAN_TI)) return TAT[2];

        return 0;
    }

    @Override
    protected ItemStack createResult() {
        return CoffeeMachineItems.COFFEE.clone();
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        super.onClose(event);

        if (!state.isIdle()) return;

        returnItem(getInventory().getItem(MUG_SLOT));
        returnItem(getInventory().getItem(BEAN_SLOT));
    }
}