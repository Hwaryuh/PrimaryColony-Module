package studio.semicolon.prc.core.module.validate.connect;

import org.bukkit.Chunk;
import studio.semicolon.prc.api.module.Yaws;

final class FrontOnlyValidator implements ConnectionValidator {
    @Override
    public boolean canConnect(Chunk cameraChunk, Chunk indicatorChunk, float indicatorYaw) {
        int dx = cameraChunk.getX() - indicatorChunk.getX();
        int dz = cameraChunk.getZ() - indicatorChunk.getZ();
        int quantized = Yaws.quantize(indicatorYaw);

        return switch (quantized) {
            case 0 -> dz == 1;    // 앞면 남쪽 → 카메라가 남쪽에 있어야 함
            case 90 -> dx == -1;  // 앞면 서쪽 → 카메라가 서쪽에 있어야 함
            case 180 -> dz == -1; // 앞면 북쪽 → 카메라가 북쪽에 있어야 함
            case 270 -> dx == 1;  // 앞면 동쪽 → 카메라가 동쪽에 있어야 함
            default -> false;
        };
    }
}