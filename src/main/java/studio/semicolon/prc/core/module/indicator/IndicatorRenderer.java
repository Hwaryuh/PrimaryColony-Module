package studio.semicolon.prc.core.module.indicator;

import com.google.common.base.Preconditions;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;
import studio.semicolon.prc.api.module.Structures;
import studio.semicolon.prc.core.module.ModuleType;

public class IndicatorRenderer {
    private IndicatorRenderer() { }

    private static final Color NORMAL_COLOR = Color.fromRGB(255, 255, 255);
    private static final Color BLOCKED_COLOR = Color.fromRGB(255, 60, 60);
    private static final Color WARNING_COLOR = Color.fromRGB(245, 160, 70);
    private static final int INDICATOR_SCALE = 16;

    public enum RenderState {
        NORMAL, WARNING, BLOCKED
    }

    /**
     * 인디케이터 업데이트 (위치 + 회전 + 렌더링)
     *
     * @param display 인디케이터 디스플레이
     * @param state 인디케이터 상태
     * @param indicatorHeight 인디케이터 Y 좌표
     */
    public static void render(ItemDisplay display, IndicatorState state, double indicatorHeight, RenderState renderState) {
        Preconditions.checkNotNull(display, "display");
        Preconditions.checkNotNull(state, "state");
        Preconditions.checkNotNull(renderState, "renderState");

        Location centerLocation = Structures.getCenterLocation(state.chunk(), indicatorHeight);
        centerLocation.setYaw(state.yaw());
        centerLocation.setPitch(0.0F);
        display.teleport(centerLocation);

        ItemStack item = ofIndicator(state.moduleType(), renderState);
        display.setItemStack(item);

        int scale = state.moduleType().getChunkCount() == 3 ? INDICATOR_SCALE * 2 : INDICATOR_SCALE;
        display.setTransformation(getScaledTransform(display, scale));
    }

    private static ItemStack ofIndicator(ModuleType type, RenderState renderState) {
        ItemStack item = ItemStack.of(Material.LEATHER_HORSE_ARMOR);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        Color color = switch (renderState) {
            case NORMAL -> NORMAL_COLOR;
            case WARNING -> WARNING_COLOR;
            case BLOCKED -> BLOCKED_COLOR;
        };

        meta.setColor(color);
        meta.setCustomModelData(type.getIndicatorModelData());

        item.setItemMeta(meta);
        return item;
    }

    private static Transformation getScaledTransform(ItemDisplay itemDisplay, int scale) {
        return new Transformation(
                itemDisplay.getTransformation().getTranslation(),
                itemDisplay.getTransformation().getLeftRotation(),
                new Vector3f(scale, scale, scale),
                itemDisplay.getTransformation().getRightRotation()
        );
    }
}