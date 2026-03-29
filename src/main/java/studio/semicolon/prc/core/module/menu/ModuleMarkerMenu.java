package studio.semicolon.prc.core.module.menu;

import io.quill.paper.menu.DragPolicy;
import io.quill.paper.menu.InventoryMenu;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.persistence.PersistentDataType;
import org.joml.Matrix4f;
import studio.semicolon.prc.api.constant.sound.PRCSounds;
import studio.semicolon.prc.api.constant.text.MenuTitles;
import studio.semicolon.prc.core.event.listener.module.ModuleMarkerListener;
import studio.semicolon.prc.core.module.ModuleType;

@SuppressWarnings("UnstableApiUsage")
public class ModuleMarkerMenu extends InventoryMenu {
    private final Location clickedLocation;
    public ModuleMarkerMenu(Player player, Location clickedLocation) {
        super(player, MenuType.GENERIC_9X3, MenuTitles.BUILD_PANEL);
        this.clickedLocation = clickedLocation;
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
                            addMarker(module);
                            close();
                        }
                    })
                    .cancelAll()
                    .build()
            );
        }
    }
    
    private void addMarker(ModuleType module) {
        clickedLocation.setYaw(player.getYaw());
        clickedLocation.setPitch(0.0f);
        clickedLocation.add(0.5f, 1.5f, 0.5f);
        ItemDisplay itemDisplay = player.getWorld().spawn(clickedLocation, ItemDisplay.class, i -> {
            i.setItemStack(module.getRequiredItem());
            i.setBillboard(Display.Billboard.VERTICAL);
            i.setBrightness(new Display.Brightness(15, 15));
            Matrix4f mat = new Matrix4f();
            i.setTransformationMatrix(mat.rotateY((float) Math.toRadians(180)));
        });
        itemDisplay.getPersistentDataContainer().set(ModuleMarkerListener.MODULE_MARKER_KEY, PersistentDataType.BOOLEAN, true);
        PRCSounds.MACHINE_INSERT_ITEM.play(player);
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
