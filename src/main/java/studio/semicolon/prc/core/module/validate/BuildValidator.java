package studio.semicolon.prc.core.module.validate;

import org.bukkit.Chunk;
import studio.semicolon.prc.core.module.indicator.IndicatorState;
import studio.semicolon.prc.core.module.ModuleMetadata;
import studio.semicolon.prc.core.module.validate.connect.ConnectRule;

import java.util.Map;

public class BuildValidator {
    private BuildValidator() { }

    /**
     * 인디케이터가 점유할 모든 청크가 비어있는지 확인
     */
    public static boolean isOccupied(IndicatorState state) {
        for (Chunk chunk : state.occupiedChunks().asList()) {  // 수정
            if (ModuleMetadata.isChunkOccupied(chunk)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 카메라 청크에서 인디케이터 청크로 연결 가능한지 검증
     *
     * @param cameraChunk 카메라가 위치한 청크
     * @param state 인디케이터 상태
     * @param cameraConnections 카메라 청크의 방향별 연결 가능 여부
     * @return 연결 가능 여부
     */
    public static boolean canConnect(Chunk cameraChunk, IndicatorState state, Map<ModuleMetadata.Direction, Boolean> cameraConnections) {
        Chunk indicatorChunk = state.chunk();

        if (!isAdjacent(cameraChunk, indicatorChunk)) {
            return false;
        }

        ModuleMetadata.Direction directionToIndicator = getDirectionBetween(cameraChunk, indicatorChunk);
        if (!cameraConnections.getOrDefault(directionToIndicator, false)) {
            return false;
        }

        ConnectRule rule = state.moduleType().getConnectRule();
        return rule.validate(cameraChunk, indicatorChunk, state.yaw());
    }

    private static boolean isAdjacent(Chunk chunk1, Chunk chunk2) {
        int distance = Math.abs(chunk1.getX() - chunk2.getX()) + Math.abs(chunk1.getZ() - chunk2.getZ());
        return distance == 1;
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
}