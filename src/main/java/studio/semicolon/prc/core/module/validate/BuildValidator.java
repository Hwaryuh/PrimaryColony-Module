package studio.semicolon.prc.core.module.validate;

import org.bukkit.Chunk;
import studio.semicolon.prc.api.module.Yaws;
import studio.semicolon.prc.api.module.indicator.IndicatorState;
import studio.semicolon.prc.core.module.ModuleMetadata;

import java.util.Map;

public class BuildValidator {
    private BuildValidator() { }

    /**
     * 인디케이터가 점유할 모든 청크가 비어있는지 확인
     */
    public static boolean isOccupied(IndicatorState state) {
        for (Chunk chunk : state.getOccupiedChunks()) {
            if (ModuleMetadata.isChunkOccupied(chunk)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canConnect(Chunk cameraChunk, IndicatorState state, Map<ModuleMetadata.Direction, Boolean> cameraConnections) {
        Chunk indicatorChunk = state.chunk();
        int distance = Math.abs(cameraChunk.getX() - indicatorChunk.getX()) + Math.abs(cameraChunk.getZ() - indicatorChunk.getZ());

        if (distance != 1) return false;

        ModuleMetadata.Direction toIndicator = getDirectionBetween(cameraChunk, indicatorChunk);

        if (!cameraConnections.getOrDefault(toIndicator, false)) return false;

        ConnectRule rule = state.moduleType().getConnectRule();

        return switch (rule) {
            case FRONT_ONLY -> isFrontConnection(cameraChunk, indicatorChunk, state.yaw());
            case TWO_WAY -> isTwoWayConnection(cameraChunk, indicatorChunk, state.yaw());
            case THREE_WAY -> isThreeWayConnection(cameraChunk, indicatorChunk, state.yaw());
            case ALL_WAY -> true;
        };
    }

    /**
     * 두 청크 사이의 방향 계산
     */
    private static ModuleMetadata.Direction getDirectionBetween(Chunk from, Chunk to) {
        int dx = to.getX() - from.getX();
        int dz = to.getZ() - from.getZ();

        if (dx == 1) return ModuleMetadata.Direction.EAST;
        if (dx == -1) return ModuleMetadata.Direction.WEST;
        if (dz == 1) return ModuleMetadata.Direction.SOUTH;
        if (dz == -1) return ModuleMetadata.Direction.NORTH;

        throw new IllegalArgumentException("Chunks are not adjacent");
    }

    private static boolean isFrontConnection(Chunk camera, Chunk indicator, float yaw) {
        int dx = camera.getX() - indicator.getX();
        int dz = camera.getZ() - indicator.getZ();
        int quantized = Yaws.quantize(yaw);

        return switch (quantized) {
            case 0 -> dz == 1;
            case 90 -> dx == -1;
            case 180 -> dz == -1;
            case 270 -> dx == 1;
            default -> false;
        };
    }

    private static boolean isTwoWayConnection(Chunk camera, Chunk indicator, float yaw) {
        int dx = camera.getX() - indicator.getX();
        int dz = camera.getZ() - indicator.getZ();
        int quantized = Yaws.quantize(yaw);

        return switch (quantized) {
            case 0 -> dz == 1 || dz == -1; // 앞면 남쪽 → 남/북 허용
            case 90 -> dx == -1 || dx == 1; // 앞면 서쪽 → 서/동 허용
            case 180 -> dz == -1 || dz == 1; // 앞면 북쪽 → 북/남 허용
            case 270 -> dx == 1 || dx == -1; // 앞면 동쪽 → 동/서 허용
            default -> false;
        };
    }

    private static boolean isThreeWayConnection(Chunk camera, Chunk indicator, float yaw) {
        int dx = camera.getX() - indicator.getX();
        int dz = camera.getZ() - indicator.getZ();
        int quantized = Yaws.quantize(yaw);

        boolean blockedDirection = switch (quantized) {
            case 0 -> (dx == -1); // yaw 0 (앞면 남쪽) → 서쪽 막힘
            case 90 -> (dz == -1); // yaw 90 (앞면 서쪽) → 북쪽 막힘
            case 180 -> (dx == 1); // yaw 180 (앞면 북쪽) → 동쪽 막힘
            case 270 -> (dz == 1); // yaw 270 (앞면 동쪽) → 남쪽 막힘
            default -> false;
        };

        return !blockedDirection;
    }
}