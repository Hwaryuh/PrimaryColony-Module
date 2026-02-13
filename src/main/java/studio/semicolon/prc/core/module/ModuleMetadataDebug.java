package studio.semicolon.prc.core.module;

import org.bukkit.Chunk;

import java.util.EnumSet;
import java.util.Set;

public class ModuleMetadataDebug {
    private ModuleMetadataDebug() { }

    /**
     * 청크를 점유 상태로 표시
     */
    public static void setOccupied(Chunk chunk) {
        ModuleMetadata.setOccupied(chunk);
    }

    /**
     * 청크에 특정 방향으로 연결 가능하도록 설정
     *
     * @param chunk 대상 청크
     * @param directions 연결 가능 방향들
     */
    public static void setConnections(Chunk chunk, Set<ModuleMetadata.Direction> directions) {
        for (ModuleMetadata.Direction direction : directions) {
            ModuleMetadata.setDirection(chunk, direction, true);
        }
    }

    /**
     * 청크의 모든 모듈 메타데이터 삭제 (점유 + 모든 방향)
     */
    public static void clearAll(Chunk chunk) {
        ModuleMetadata.removeMetadata(chunk);
    }

    /**
     * 청크의 현재 메타데이터 상태 조회
     */
    public static DebugInfo getInfo(Chunk chunk) {
        boolean occupied = ModuleMetadata.isChunkOccupied(chunk);

        Set<ModuleMetadata.Direction> connections = EnumSet.noneOf(ModuleMetadata.Direction.class);
        for (ModuleMetadata.Direction dir : ModuleMetadata.Direction.values()) {
            if (ModuleMetadata.canConnectToDirection(chunk, dir)) {
                connections.add(dir);
            }
        }

        return new DebugInfo(occupied, connections);
    }

    public record DebugInfo(boolean occupied, Set<ModuleMetadata.Direction> connections) { }
}