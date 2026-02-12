package studio.semicolon.prc.core.machine.furnace;

import io.quill.paper.item.ItemMatcher;
import io.quill.paper.menu.DragPolicy;
import io.quill.paper.menu.slot.SlotFilter;
import io.quill.paper.util.bukkit.task.Tasks;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import studio.semicolon.prc.api.machine.AbstractMachine;
import studio.semicolon.prc.api.machine.MachineMenu;
import studio.semicolon.prc.api.machine.MachineState;
import studio.semicolon.prc.api.machine.Upgradeable;
import studio.semicolon.prc.core.constant.item.ETCItems;
import studio.semicolon.prc.core.constant.item.machine.FurnaceMachineItems;
import studio.semicolon.prc.core.constant.item.machine.GrinderMachineItems;
import studio.semicolon.prc.core.constant.sound.PRCSounds;
import studio.semicolon.prc.core.constant.text.MachineMessages;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class FurnaceMachineMenu extends MachineMenu {
    private final Upgradeable upgradeable;

    private static final int FUEL_SLOT = 3;
    private static final int POWDER_1_SLOT = 38;
    private static final int POWDER_2_SLOT = 39;
    private static final int POWDER_3_SLOT = 40;
    private static final int RECIPE_SLOT = 48;
    private static final int RESULT_SLOT = 43;
    private static final int START_BUTTON_SLOT = 46;
    private static final int UPGRADE_1_SLOT = 8;
    private static final int UPGRADE_2_SLOT = 17;
    private static final int UPGRADE_3_SLOT = 26;

    public FurnaceMachineMenu(Player player, AbstractMachine machine) {
        super(player, machine, MenuType.GENERIC_9X6);
        this.upgradeable = (Upgradeable) machine;
    }

    @Override
    protected void render() {
        syncState();
        setDragPolicy(DragPolicy.PREVENT_TOP_ONLY);
        onPlayerInventory(builder -> builder.cancelOn(ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT));
    }

    private FurnaceRecipe getIngotRecipe() {
        ItemStack fuel = getInventory().getItem(FUEL_SLOT);
        if (!ItemMatcher.matches(fuel, GrinderMachineItems.MG_POWDER) || fuel.getAmount() < 1) {
            alert(MachineMessages.FURNACE_INSUFFICIENT_FUEL);
            return null;
        }

        ItemStack recipeItem = getInventory().getItem(RECIPE_SLOT);
        FurnaceRecipe recipe = FurnaceRecipe.fromRecipeItem(recipeItem);
        if (recipe == null) {
            alert(MachineMessages.FURNACE_INSUFFICIENT_RECIPE);
            return null;
        }

        List<ItemStack> requiredPowders = recipe.getRequiredPowders();
        int[] slots = { POWDER_1_SLOT, POWDER_2_SLOT, POWDER_3_SLOT };
        for (int i = 0; i < requiredPowders.size(); i++) {
            ItemStack powder = getInventory().getItem(slots[i]);
            if (!ItemMatcher.matches(powder, requiredPowders.get(i)) || powder.getAmount() < 4) {
                alert(MachineMessages.FURNACE_INSUFFICIENT_INGREDIENTS);
                return null;
            }
        }

        return recipe;
    }

    private void setUpgradeSlot(int slot, int requiredLevel, ItemStack upgradeItem) {
        int currentLevel = upgradeable.getUpgradeLevel();

        if (currentLevel >= requiredLevel) {
            setButton(slot, button(upgradeItem).cancelAll().build());
        } else if (currentLevel == requiredLevel - 1) {
            setSlotFilter(slot, new SlotFilter() {
                @Override
                public boolean canPlace(ItemStack item, InventoryClickEvent e) {
                    return ItemMatcher.matches(item, upgradeItem);
                }

                @Override
                public boolean canPickup(ItemStack item, InventoryClickEvent e) { return false; }

                @Override
                public void onPlaced() {
                    upgradeable.setUpgradeLevel(requiredLevel);

                    if (requiredLevel == 1) {
                        removeButton(POWDER_2_SLOT);
                        setUpgradeSlot(UPGRADE_2_SLOT, 2, FurnaceMachineItems.ICE_MOLD);
                        Tasks.run(() -> {
                            setPlaceholderSlot(POWDER_2_SLOT, FurnaceMachineItems.GUIDE_POWDER, GrinderMachineItems::isPowder, 4);
                            registerInputSlot(POWDER_2_SLOT);
                        });
                    } else if (requiredLevel == 2) {
                        removeButton(POWDER_3_SLOT);
                        setUpgradeSlot(UPGRADE_3_SLOT, 3, FurnaceMachineItems.TORCH);
                        Tasks.run(() -> {
                            setPlaceholderSlot(POWDER_3_SLOT, FurnaceMachineItems.GUIDE_POWDER, GrinderMachineItems::isPowder, 4);
                            registerInputSlot(POWDER_3_SLOT);
                        });
                    }

                    PRCSounds.MACHINE_UPGRADE.play(player);
                }
            });
        }
    }

    @Override
    protected boolean validateIngredients() {
        return getIngotRecipe() != null;
    }

    @Override
    protected void updateUI() {
        setUpgradeSlot(UPGRADE_1_SLOT, 1, FurnaceMachineItems.SLOT_EXTENSION);
        setUpgradeSlot(UPGRADE_2_SLOT, 2, FurnaceMachineItems.ICE_MOLD);
        setUpgradeSlot(UPGRADE_3_SLOT, 3, FurnaceMachineItems.TORCH);

        if (state.isIdle()) {
            setPlaceholderSlot(FUEL_SLOT, FurnaceMachineItems.GUIDE_FUEL, i -> ItemMatcher.matches(i, GrinderMachineItems.MG_POWDER), 1);
            registerInputSlot(FUEL_SLOT);

            setPlaceholderSlot(POWDER_1_SLOT, FurnaceMachineItems.GUIDE_POWDER, GrinderMachineItems::isPowder, 4);
            registerInputSlot(POWDER_1_SLOT);

            if (upgradeable.getUpgradeLevel() >= 1) {
                setPlaceholderSlot(POWDER_2_SLOT, FurnaceMachineItems.GUIDE_POWDER, GrinderMachineItems::isPowder, 4);
                registerInputSlot(POWDER_2_SLOT);
            }

            if (upgradeable.getUpgradeLevel() >= 2) {
                setPlaceholderSlot(POWDER_3_SLOT, FurnaceMachineItems.GUIDE_POWDER, GrinderMachineItems::isPowder, 4);
                registerInputSlot(POWDER_3_SLOT);
            }

            setPlaceholderSlot(RECIPE_SLOT, FurnaceMachineItems.GUIDE_RECIPE, FurnaceMachineItems::isIngotRecipe, 1);
            registerInputSlot(RECIPE_SLOT);

            routeShiftClickToFirst(
                    i -> ItemMatcher.matches(i, GrinderMachineItems.MG_POWDER),
                    FUEL_SLOT, POWDER_1_SLOT, POWDER_2_SLOT, POWDER_3_SLOT
            );
            routeShiftClickToFirst(GrinderMachineItems::isPowder, POWDER_1_SLOT, POWDER_2_SLOT, POWDER_3_SLOT);
            routeShiftClickToFirst(FurnaceMachineItems::isIngotRecipe, RECIPE_SLOT);
            routeShiftClickToFirst(i -> ItemMatcher.matches(i, FurnaceMachineItems.SLOT_EXTENSION), UPGRADE_1_SLOT);
            routeShiftClickToFirst(i -> ItemMatcher.matches(i, FurnaceMachineItems.ICE_MOLD), UPGRADE_2_SLOT);
            routeShiftClickToFirst(i -> ItemMatcher.matches(i, FurnaceMachineItems.TORCH), UPGRADE_3_SLOT);

            setButton(START_BUTTON_SLOT, button(FurnaceMachineItems.START_BUTTON)
                    .onLeftClick(ctx -> {
                        if (!ctx.isEmptyCursor()) return;
                        FurnaceRecipe recipe = getIngotRecipe();
                        if (recipe == null) return;
                        startCrafting(recipe.getDuration(upgradeable.getUpgradeLevel() >= 3), recipe.name());
                    })
                    .cancelAll()
                    .build()
            );
        } else  {
            setSlotFilter(FUEL_SLOT, item -> false);
            setSlotFilter(POWDER_1_SLOT, item -> false);
            setSlotFilter(POWDER_2_SLOT, item -> false);
            setSlotFilter(POWDER_3_SLOT, item -> false);
            setSlotFilter(RECIPE_SLOT, item -> false);

            if (state.isProcessing()) {
                setTitle(machine.getTitleForState(MachineState.State.PROCESSING));
                removeButton(START_BUTTON_SLOT);
                setButton(RESULT_SLOT, button(getTimerItem(state.getRemainingSeconds()))
                        .onLeftClick(ctx -> {
                            if (!ctx.isEmptyCursor()) return;
                            syncState();
                            PRCSounds.GLOBAL_CLICK.play(player);
                        })
                        .cancelAll()
                        .build()
                );
            } else if (state.isCompleted()) {
                setTitle(machine.getTitleForState(MachineState.State.IDLE));
                renderResult();
            }
        }

        if (!state.isProcessing() && upgradeable.getUpgradeLevel() < 1) {
            setButton(POWDER_2_SLOT, button(ETCItems.BLOCKED_SLOT_ITEM).cancelAll().build());
            setButton(POWDER_3_SLOT, button(ETCItems.BLOCKED_SLOT_ITEM).cancelAll().build());
        } else if (!state.isProcessing() && upgradeable.getUpgradeLevel() < 2) {
            setButton(POWDER_3_SLOT, button(ETCItems.BLOCKED_SLOT_ITEM).cancelAll().build());
        }
    }

    @Override
    protected void consumeIngredients() {
        FurnaceRecipe recipe = getIngotRecipe();
        if (recipe == null) return;

        ItemStack fuel = getInventory().getItem(FUEL_SLOT);
        if (fuel != null) setSlotEmpty(FUEL_SLOT);

        setSlotEmpty(POWDER_1_SLOT, POWDER_2_SLOT, POWDER_3_SLOT);

        ItemStack recipeItem = getInventory().getItem(RECIPE_SLOT);
        setSlotEmpty(RECIPE_SLOT);
        returnItem(recipeItem);
    }

    @Override
    protected int getResultSlot() {
        return RESULT_SLOT;
    }

    @Override
    protected ItemStack createResult() {
        String recipeID = state.getProcessResult();
        if (recipeID == null) return ItemStack.empty();

        try {
            FurnaceRecipe recipe = FurnaceRecipe.valueOf(recipeID);
            return recipe.getResult();
        } catch (IllegalArgumentException e) {
            return ItemStack.empty();
        }
    }

    @Override
    protected void onTakeResult() { }

    @Override
    protected void onClose(InventoryCloseEvent e) {
        super.onClose(e);
        if (state.isIdle()) {
            returnAllInputSlots();
        }
    }
}