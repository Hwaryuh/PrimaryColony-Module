package studio.semicolon.prc.core.module.validate.connect;

import org.bukkit.Chunk;
import studio.semicolon.prc.api.module.Yaws;

final class TwoWayValidator implements ConnectionValidator {
    @Override
    public boolean canConnect(Chunk cameraChunk, Chunk indicatorChunk, float indicatorYaw) {
        int dx = cameraChunk.getX() - indicatorChunk.getX();
        int dz = cameraChunk.getZ() - indicatorChunk.getZ();
        int quantized = Yaws.quantize(indicatorYaw);

        return switch (quantized) {
            case 0 -> dz == 1 || dz == -1;   // 앞면 남쪽 → 남/북 허용
            case 90 -> dx == -1 || dx == 1;  // 앞면 서쪽 → 서/동 허용
            case 180 -> dz == -1 || dz == 1; // 앞면 북쪽 → 북/남 허용
            case 270 -> dx == 1 || dx == -1; // 앞면 동쪽 → 동/서 허용
            default -> false;
        };
    }
}