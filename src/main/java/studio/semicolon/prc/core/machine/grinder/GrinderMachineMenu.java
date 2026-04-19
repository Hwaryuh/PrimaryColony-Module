package studio.semicolon.prc.core.machine.grinder;

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
import studio.semicolon.prc.api.constant.item.game.ETCItems;
import studio.semicolon.prc.api.constant.item.machine.GrinderMachineItems;
import studio.semicolon.prc.api.constant.sound.PRCSounds;
import studio.semicolon.prc.api.constant.text.MachineMessages;
import studio.semicolon.prc.api.machine.UpgradeableMachine;
import studio.semicolon.prc.core.util.Missions;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class GrinderMachineMenu extends MachineMenu {
    private final UpgradeableMachine upgradeable;

    private static final int ORE_1_SLOT = 3;
    private static final int ORE_2_SLOT = 5;
    private static final int RESULT_1_SLOT = 48;
    private static final int RESULT_2_SLOT = 50;
    private static final int[] LEVER_BUTTON_SLOTS = { 15, 16, 24, 25 };
    private static final int UPGRADE_1_SLOT = 27;
    private static final int UPGRADE_2_SLOT = 36;
    private static final int UPGRADE_3_SLOT = 45;

    public GrinderMachineMenu(Player player, AbstractMachine machine) {
        super(player, machine, MenuType.GENERIC_9X6);
        this.upgradeable = (UpgradeableMachine) machine;
    }

    @Override
    protected void render() {
        syncState();
        setDragPolicy(DragPolicy.PREVENT_TOP_ONLY);
        onPlayerInventory(builder -> builder.cancelOn(ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT));
    }

    @Override
    protected void updateUI() {
        setUpgradeSlot(UPGRADE_1_SLOT, 1, GrinderMachineItems.SLOT_EXTENSION);
        setUpgradeSlot(UPGRADE_2_SLOT, 2, GrinderMachineItems.GEAR);
        setUpgradeSlot(UPGRADE_3_SLOT, 3, GrinderMachineItems.DRILL);

        if (state.isIdle()) {
            setPlaceholderSlot(ORE_1_SLOT, GrinderMachineItems.GUIDE_ORE, GrinderMachineItems::isOre, getMaxOreAmount());
            registerInputSlot(ORE_1_SLOT);

            if (upgradeable.getUpgradeLevel() >= 1) {
                setPlaceholderSlot(ORE_2_SLOT, GrinderMachineItems.GUIDE_ORE, GrinderMachineItems::isOre, getMaxOreAmount());
                registerInputSlot(ORE_2_SLOT);
            }

            routeShiftClickToFirst(GrinderMachineItems::isOre,
                    () -> ORE_1_SLOT,
                    () -> upgradeable.getUpgradeLevel() >= 1 ? ORE_2_SLOT : -1
            );
            routeShiftClickToFirst(i -> ItemMatcher.matches(i, GrinderMachineItems.SLOT_EXTENSION), UPGRADE_1_SLOT);
            routeShiftClickToFirst(i -> ItemMatcher.matches(i, GrinderMachineItems.GEAR), UPGRADE_2_SLOT);
            routeShiftClickToFirst(i -> ItemMatcher.matches(i, GrinderMachineItems.DRILL), UPGRADE_3_SLOT);

            for (int slot : LEVER_BUTTON_SLOTS) {
                setButton(slot, button(GrinderMachineItems.LEVER_BUTTON)
                        .onLeftClick(ctx -> {
                            if (!ctx.isEmptyCursor()) return;
                            GrindingData data = getGrindingData();
                            if (data == null) return;
                            startCrafting(data.duration(), data.processResult());
                        })
                        .cancelAll()
                        .build()
                );
            }
        } else {
            setSlotFilter(ORE_1_SLOT, item -> false);
            setSlotFilter(ORE_2_SLOT, item -> false);

            if (state.isProcessing()) {
                setTitle(machine.getTitleForState(MachineState.State.PROCESSING));
                removeButtons(LEVER_BUTTON_SLOTS);

                String processResult = state.getProcessResult();
                String[] recipeIDs = processResult.split(",");

                setButton(RESULT_1_SLOT, button(getTimerItem(state.getRemainingSeconds()))
                        .onLeftClick(ctx -> {
                            if (!ctx.isEmptyCursor()) return;
                            syncState();
                            PRCSounds.GLOBAL_CLICK.play(player);
                        })
                        .cancelAll()
                        .build()
                );

                if (recipeIDs.length > 1) {
                    setButton(RESULT_2_SLOT, button(getTimerItem(state.getRemainingSeconds()))
                            .onLeftClick(ctx -> {
                                if (!ctx.isEmptyCursor()) return;
                                syncState();
                                PRCSounds.GLOBAL_CLICK.play(player);
                            })
                            .cancelAll()
                            .build()
                    );
                }
            } else if (state.isCompleted()) {
                setTitle(machine.getTitleForState(MachineState.State.IDLE));
                renderResult();
            }
        }

        if (upgradeable.getUpgradeLevel() < 1) {
            setButton(ORE_2_SLOT, button(ETCItems.BLOCKED_SLOT_ITEM).cancelAll().build());
        }
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
                        removeButton(ORE_2_SLOT);
                        setUpgradeSlot(UPGRADE_2_SLOT, 2, GrinderMachineItems.GEAR);
                        Tasks.run(() -> {
                            refreshOreSlots();
                            Missions.progressV2(player, "MODULE_UPGRADE", "crusher_process", 1);
                        });
                    } else if (requiredLevel == 2) {
                        setUpgradeSlot(UPGRADE_3_SLOT, 3, GrinderMachineItems.DRILL);
                        Tasks.run(() -> {
                            refreshOreSlots();
                            Missions.progressV2(player, "MODULE_UPGRADE", "crusher_process", 1);
                        });
                    } else if (requiredLevel == 3) {
                        Tasks.run(() -> refreshOreSlots());
                    }

                    PRCSounds.MACHINE_UPGRADE.play(player);
                }
            });
        }
    }

    private GrindingData getGrindingData() {
        ItemStack ore1 = getInventory().getItem(ORE_1_SLOT);

        if (ore1 == null || ore1.getType().isAir() || ItemMatcher.matches(ore1, GrinderMachineItems.GUIDE_ORE)) {
            alert(MachineMessages.GRINDER_INSUFFICIENT_ORE);
            return null;
        }

        GrinderRecipe recipe1 = GrinderRecipe.fromOre(ore1);
        if (recipe1 == null) {
            alert(MachineMessages.GRINDER_INVALID_ORE);
            return null;
        }

        if (!recipe1.canProcess(upgradeable.getUpgradeLevel())) {
            alert(MachineMessages.GRINDER_CANNOT_PROCESS);
            return null;
        }

        int duration = recipe1.getDuration(upgradeable.getUpgradeLevel()) * ore1.getAmount();
        String processResult = recipe1.name() + ":" + ore1.getAmount();

        ItemStack ore2 = getInventory().getItem(ORE_2_SLOT);

        if (ore2 != null && !ore2.getType().isAir() && !ItemMatcher.matches(ore2, ETCItems.BLOCKED_SLOT_ITEM) && !ItemMatcher.matches(ore2, GrinderMachineItems.GUIDE_ORE)) {
            GrinderRecipe recipe2 = GrinderRecipe.fromOre(ore2);
            if (recipe2 == null) {
                alert(MachineMessages.GRINDER_INVALID_ORE);
                return null;
            }

            if (!recipe2.canProcess(upgradeable.getUpgradeLevel())) {
                alert(MachineMessages.GRINDER_CANNOT_PROCESS);
                return null;
            }

            int duration2 = recipe2.getDuration(upgradeable.getUpgradeLevel()) * ore2.getAmount();
            duration = Math.max(duration, duration2);
            processResult += "," + recipe2.name() + ":" + ore2.getAmount();
        }

        return new GrindingData(duration, processResult);
    }

    @Override
    protected boolean validateIngredients() {
        return getGrindingData() != null;
    }

    @Override
    protected void onValidationFailed() {
        alert(MachineMessages.GRINDER_INSUFFICIENT_ORE);
    }

    @Override
    protected void consumeIngredients() {
        setSlotEmpty(ORE_1_SLOT, ORE_2_SLOT);
    }

    @Override
    protected int getResultSlot() {
        return RESULT_1_SLOT;
    }

    @Override
    protected ItemStack createResult() {
        return ItemStack.empty();
    }

    @Override
    protected void renderResult() {
        if (!state.isCompleted()) return;

        List<GrinderRecipe.GrindingResult> results = GrinderRecipe.parseResults(state.getProcessResult());

        if (!results.isEmpty()) {
            GrinderRecipe.GrindingResult r1 = results.getFirst();
            setResultButton(RESULT_1_SLOT, r1.recipe().getPowder().asQuantity(r1.powderAmount()), r1.recipe());
        }

        if (results.size() >= 2) {
            GrinderRecipe.GrindingResult r2 = results.get(1);
            setResultButton(RESULT_2_SLOT, r2.recipe().getPowder().asQuantity(r2.powderAmount()), r2.recipe());
        }
    }

    private void onTakePowder() {
        Tasks.run(() -> {
            ItemStack slot1 = getInventory().getItem(RESULT_1_SLOT);
            ItemStack slot2 = getInventory().getItem(RESULT_2_SLOT);

            boolean isSlot1Empty = slot1 == null || slot1.getType().isAir();
            boolean isSlot2Empty = slot2 == null || slot2.getType().isAir();

            if (isSlot1Empty && isSlot2Empty) {
                state.setState(MachineState.State.IDLE);
                machine.saveState(state);
                updateUI();
                return;
            }

            List<GrinderRecipe.GrindingResult> results = GrinderRecipe.parseResults(state.getProcessResult());
            StringBuilder remaining = new StringBuilder();

            if (!isSlot1Empty && !results.isEmpty()) {
                remaining.append(results.getFirst().recipe().name()).append(":").append(results.getFirst().oreAmount());
            }

            if (results.size() >= 2) {
                if (!remaining.isEmpty()) remaining.append(",");
                if (!isSlot2Empty) {
                    remaining.append(results.get(1).recipe().name()).append(":").append(results.get(1).oreAmount());
                }
            }

            state.setProcessResult(remaining.toString());
            machine.saveState(state);
        });
    }

    private void setResultButton(int slot, ItemStack result, GrinderRecipe recipe) {
        setButton(slot, button(result)
                .onAnyClick(ctx -> {
                    if (ctx.isPickup() || ctx.isMove()) {
                        processMission(recipe);
                        onTakePowder();
                    }
                })
                .cancelIf(ctx -> !ctx.isPickup() && !ctx.isMove())
                .build()
        );
    }

    @Override
    protected void onClose(InventoryCloseEvent e) {
        super.onClose(e);
        if (state.isIdle()) {
            returnAllInputSlots();
        }
    }

    private void refreshOreSlots() {
        setPlaceholderSlot(ORE_1_SLOT, GrinderMachineItems.GUIDE_ORE, GrinderMachineItems::isOre, getMaxOreAmount());
        registerInputSlot(ORE_1_SLOT);
        setPlaceholderSlot(ORE_2_SLOT, GrinderMachineItems.GUIDE_ORE, GrinderMachineItems::isOre, getMaxOreAmount());
        registerInputSlot(ORE_2_SLOT);
    }

    private int getMaxOreAmount() {
        return switch (upgradeable.getUpgradeLevel()) {
            case 0 -> 2;
            case 1 -> 4;
            case 2 -> 8;
            default -> 16;
        };
    }

    private void processMission(GrinderRecipe recipe) {
        switch (recipe) {
            case FE -> Missions.progressV1(player, "DEVICE_INTERACTION", "crusher_process", 1);
        }
    }

    private record GrindingData(int duration, String processResult) { }
}
