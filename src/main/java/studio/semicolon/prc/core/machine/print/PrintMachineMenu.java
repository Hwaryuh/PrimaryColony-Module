package studio.semicolon.prc.core.machine.print;

import io.quill.paper.item.ItemMatcher;
import io.quill.paper.menu.DragPolicy;
import io.quill.paper.util.bukkit.Logger;
import kr.eme.prcMission.enums.MissionVersion;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import studio.semicolon.prc.api.machine.AbstractMachine;
import studio.semicolon.prc.api.machine.MachineMenu;
import studio.semicolon.prc.api.constant.item.machine.PrintMachineItems;
import studio.semicolon.prc.api.constant.sound.PRCSounds;
import studio.semicolon.prc.api.constant.text.MachineMessages;
import studio.semicolon.prc.api.constant.text.MenuTitles;
import studio.semicolon.prc.core.util.Missions;

import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class PrintMachineMenu extends MachineMenu {
    private enum Page { SELECTION, CONFIRM }

    private final int TAT = 10;

    private final int[] PRINT_ITEM_SLOTS = { 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39 };
    private final int SELECTED_ITEM_SLOT = 19;
    private final int[] YES_BUTTON_SLOTS = { 27, 28 };
    private final int[] NO_BUTTON_SLOTS = { 29, 30 };
    private final int RESULT_SLOT = 33;

    private Page currentPage = Page.SELECTION;
    private PrintRecipe selectedRecipe = null;

    public PrintMachineMenu(Player player, AbstractMachine machine) {
        super(player, machine, MenuType.GENERIC_9X6);
    }

    @Override
    protected void render() {
        syncState();
        setDragPolicy(DragPolicy.PREVENT_TOP_ONLY);
        onPlayerInventory(builder -> builder.cancelOn(ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT));
    }

    @Override
    protected void updateUI() {
        if (state.isProcessing()) {
            currentPage = Page.SELECTION;
            selectedRecipe = null;
        }

        this.clearSlots();

        if (state.isProcessing()) {
            setTitle(MenuTitles.PRINT_MACHINE_PROCESSING);
            setButton(RESULT_SLOT, button(getTimerItem(state.getRemainingSeconds()))
                    .onLeftClick(ctx -> {
                        syncState();
                        PRCSounds.GLOBAL_CLICK.play(player);
                    })
                    .cancelAll()
                    .build()
            );
        } else if (state.isCompleted()) {
            setTitle(MenuTitles.PRINT_MACHINE_PROCESSING);
            renderResult();
        } else {
            switch (currentPage) {
                case SELECTION -> renderSelectionPage();
                case CONFIRM -> renderConfirmPage();
            }
        }
    }

    private void clearSlots() {
        setSlotEmpty(PRINT_ITEM_SLOTS);
        setSlotEmpty(RESULT_SLOT);
    }

    private void renderSelectionPage() {
        setTitle(MenuTitles.PRINT_MACHINE_SELECTION);

        PrintRecipe[] recipes = PrintRecipe.values();
        for (int i = 0; i < PRINT_ITEM_SLOTS.length && i < recipes.length; i++) {
            PrintRecipe recipe = recipes[i];
            int slot = PRINT_ITEM_SLOTS[i];

            setButton(slot, button(recipe.getResult())
                    .onLeftClick(ctx -> {
                        if (ctx.isEmptyCursor()) {
                            this.selectedRecipe = recipe;
                            this.currentPage = Page.CONFIRM;
                            updateUI();
                            PRCSounds.GLOBAL_CLICK.play(player);
                        }
                    })
                    .cancelAll()
                    .build()
            );
        }
    }

    private void renderConfirmPage() {
        setTitle(MenuTitles.PRINT_MACHINE_CONFIRM);
        removeButtons(PRINT_ITEM_SLOTS);

        ItemStack selectedItem = PrintMachineItems.withMaterials(selectedRecipe.getResult(), selectedRecipe.getMaterials());
        setButton(SELECTED_ITEM_SLOT, button(selectedItem).cancelAll().build());

        for (int slot : YES_BUTTON_SLOTS) {
            setButton(slot, button(PrintMachineItems.YES_BUTTON)
                    .onLeftClick(ctx -> {
                        if (ctx.isEmptyCursor()) {
                            tryStartCrafting();
                        }
                    })
                    .cancelAll()
                    .build()
            );
        }

        for (int slot : NO_BUTTON_SLOTS) {
            setButton(slot, button(PrintMachineItems.NO_BUTTON)
                    .onLeftClick(ctx -> {
                        if (ctx.isEmptyCursor()) {
                            this.selectedRecipe = null;
                            this.currentPage = Page.SELECTION;
                            this.clearSlots();
                            render();
                            PRCSounds.GLOBAL_CLICK.play(player);
                        }
                    })
                    .cancelAll()
                    .build()
            );
        }
    }

    private void tryStartCrafting() {
        if (selectedRecipe == null) return;
        startCrafting(TAT, selectedRecipe.getID());
    }

    @Override
    protected int getResultSlot() {
        return RESULT_SLOT;
    }

    @Override
    protected boolean validateIngredients() {
        if (selectedRecipe == null) return false;

        Map<ItemStack, Integer> required = selectedRecipe.getMaterials();

        for (Map.Entry<ItemStack, Integer> entry : required.entrySet()) {
            ItemStack requiredItem = entry.getKey();
            int requiredAmount = entry.getValue();
            int count = 0;

            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null) continue;
                if (ItemMatcher.matches(item, requiredItem)) {
                    count += item.getAmount();
                }
            }

            if (count < requiredAmount) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void onValidationFailed() {
        alert(MachineMessages.PRINT_MACHINE_INVALID_INGREDIENTS);
    }

    @Override
    protected void consumeIngredients() {
        if (selectedRecipe == null) return;

        Map<ItemStack, Integer> required = selectedRecipe.getMaterials();

        for (Map.Entry<ItemStack, Integer> entry : required.entrySet()) {
            ItemStack requiredItem = entry.getKey();
            int remaining = entry.getValue();

            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (item == null) continue;

                if (ItemMatcher.matches(item, requiredItem)) {
                    int stackAmount = item.getAmount();

                    if (stackAmount <= remaining) {
                        player.getInventory().setItem(i, ItemStack.of(Material.AIR));
                        remaining -= stackAmount;
                    } else {
                        item.setAmount(stackAmount - remaining);
                        remaining = 0;
                    }

                    if (remaining <= 0) break;
                }
            }
        }
    }

    @Override
    protected ItemStack createResult() {
        if (selectedRecipe != null) {
            return selectedRecipe.getResult();
        }

        String recipeID = state.getProcessResult();
        PrintRecipe recipe = PrintRecipe.getByID(recipeID);

        if (recipe != null) {
            return recipe.getResult();
        } else {
            return ItemStack.empty();
        }
    }

    @Override
    protected void onTakeResult() {
        String recipeID = state.getProcessResult();
        if (recipeID == null) return;

        PrintRecipe recipe = PrintRecipe.getByID(recipeID);
        if (recipe == null) {
            Logger.warn("Invalid recipe ID: " + recipeID);
            return;
        }

        progressMission(recipe);
        this.clearSlots();
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        super.onClose(event);
    }

    private void progressMission(PrintRecipe recipe) {
        switch (recipe) {
            case HOE_1 -> {
                Missions.progressV1(player, "CRAFTING", "crafting", 1);
            }
            case WATERING_CAN_1 -> {
                Missions.progressV1(player, "CRAFTING", "crafting", 2);
            }
            case PICKAXE_2 -> {
                Missions.progressV1(player, "UPGRADE", "upgrade", 1);
            }
            case HOE_2 -> {
                Missions.progressV1(player, "UPGRADE", "upgrade", 2);
            }
            case WATERING_CAN_2 -> {
                Missions.progressV1(player, "UPGRADE", "upgrade", 3);
            }
            case CAPSULE_GUN -> {
                Missions.progressV2(player, "CRAFTING", "crafting", 1);
            }
            case WEAPON_LONG_SWORD, PICKAXE_3 -> {
                Missions.progressV2(player, "CRAFTING", "printer_module", 1);
            }
        }
    }
}