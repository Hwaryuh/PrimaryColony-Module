package studio.semicolon.prc.core.module.menu;

import io.quill.paper.menu.DragPolicy;
import io.quill.paper.menu.InventoryMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import studio.semicolon.prc.core.constant.sound.PRCSounds;
import studio.semicolon.prc.core.constant.text.MenuTitles;
import studio.semicolon.prc.core.constant.text.ModuleMessages;
import studio.semicolon.prc.core.module.build.BuildSessionManager;
import studio.semicolon.prc.core.module.ModuleType;
import studio.semicolon.prc.core.util.Titles;

@SuppressWarnings("UnstableApiUsage")
public class BuildPanelMenu extends InventoryMenu {
    private final double panelY;

    public BuildPanelMenu(Player player, double panelY) {
        super(player, MenuType.GENERIC_9X3, MenuTitles.BUILD_PANEL);
        this.panelY = panelY;
    }

    @Override
    protected void render() {
        setDragPolicy(DragPolicy.PREVENT_ALL);
        onPlayerInventory(builder -> builder.cancelOn(ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT));

        for (int i = 9; i <= 17; i++) {
            int slot = i;
            ModuleType type = getModuleType(slot);
            ItemStack buttonItem = type.getRequiredItem();

            setButton(slot, button(buttonItem)
                    .onLeftClick(ctx -> {
                        if (ctx.isEmptyCursor()) {
                            var module = getModuleType(slot);

                            if (BuildSessionManager.getInstance().isUsing()) {
                                player.sendMessage(ModuleMessages.SESSION_USING);
                                PRCSounds.GLOBAL_ERROR.play(player);
                                close();
                                return;
                            }

                            close();
                            PRCSounds.GLOBAL_CLICK.play(player);

                            Titles.fade(player, () -> BuildSessionManager.getInstance().enter(player, module, panelY));
                        }
                    })
                    .cancelAll()
                    .build()
            );
        }
    }

    private ModuleType getModuleType(int slot) {
        var index = slot - 9;

        return switch (index) {
            case 0 -> ModuleType.DEFAULT;
            case 1 -> ModuleType.T_SHAPED;
            case 2 -> ModuleType.CROSS_SHAPED;
            case 3 -> ModuleType.MINE;
            case 4 -> ModuleType.FARM_M;
            case 5 -> ModuleType.FARM_L;
            case 6 -> ModuleType.STORAGE_M;
            case 7 -> ModuleType.STORAGE_L;
            case 8 -> ModuleType.ENTRY;
            default -> throw new IllegalStateException("Unexpected value: " + slot);
        };
    }
}