package studio.semicolon.prc.api.module.indicator;

import org.bukkit.Chunk;
import org.bukkit.World;
import studio.semicolon.prc.api.module.RelativeDirection;
import studio.semicolon.prc.api.module.Yaws;

public class IndicatorMovement {
    private IndicatorMovement() { }

    /**
     * 목표 청크 계산
     */
    public static Chunk getTargetChunk(Chunk currentChunk, RelativeDirection direction, float cameraYaw) {
        World world = currentChunk.getWorld();
        float targetYaw = (cameraYaw + direction.getYawOffset()) % 360;

        int[] offset = getChunkOffsetFromYaw(targetYaw);
        return world.getChunkAt(
                currentChunk.getX() + offset[0],
                currentChunk.getZ() + offset[1]
        );
    }

    /**
     * Yaw → 청크 오프셋
     */
    private static int[] getChunkOffsetFromYaw(float yaw) {
        return switch (Yaws.snapToCardinal(yaw)) {
            case 0 -> new int[]{0, 1};    // 남
            case 90 -> new int[]{-1, 0};  // 서
            case 180 -> new int[]{0, -1}; // 북
            case 270 -> new int[]{1, 0};  // 동
            default -> throw new IllegalStateException("Unexpected snapped yaw: " + yaw);
        };
    }
}