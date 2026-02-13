package studio.semicolon.prc.core.module.validate.connect;

import org.bukkit.Chunk;
import studio.semicolon.prc.api.module.Yaws;

final class ThreeWayValidator implements ConnectionValidator {
    @Override
    public boolean canConnect(Chunk cameraChunk, Chunk indicatorChunk, float indicatorYaw) {
        int dx = cameraChunk.getX() - indicatorChunk.getX();
        int dz = cameraChunk.getZ() - indicatorChunk.getZ();
        int quantized = Yaws.quantize(indicatorYaw);

        boolean isBlockedDirection = switch (quantized) {
            case 0 -> (dx == -1);   // yaw 0 (앞면 남쪽) → 서쪽 차단
            case 90 -> (dz == -1);  // yaw 90 (앞면 서쪽) → 북쪽 차단
            case 180 -> (dx == 1);  // yaw 180 (앞면 북쪽) → 동쪽 차단
            case 270 -> (dz == 1);  // yaw 270 (앞면 동쪽) → 남쪽 차단
            default -> false;
        };

        return !isBlockedDirection;
    }
}