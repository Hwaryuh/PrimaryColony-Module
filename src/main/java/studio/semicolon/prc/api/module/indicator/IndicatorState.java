package studio.semicolon.prc.api.module.indicator;

import org.bukkit.Chunk;
import org.bukkit.block.structure.StructureRotation;
import studio.semicolon.prc.api.module.Structures;
import studio.semicolon.prc.api.module.Yaws;
import studio.semicolon.prc.core.module.ModuleType;

import java.util.List;

public record IndicatorState(Chunk chunk, float yaw, ModuleType moduleType) {
    public IndicatorState(Chunk chunk, float yaw, ModuleType moduleType) {
        this.chunk = chunk;
        this.yaw = Yaws.normalize(yaw);
        this.moduleType = moduleType;
    }

    public StructureRotation getRotation() {
        return Structures.getRotationFromYaw(yaw);
    }

    /**
     * 이 인디케이터가 점유할 청크 목록
     */
    public List<Chunk> getOccupiedChunks() {
        return Structures.getOccupiedChunks(chunk, moduleType.getChunkCount(), getRotation());
    }

    /**
     * 회전된 새 상태 반환 (불변)
     */
    public IndicatorState rotate() {
        float newYaw = (yaw + 90) % 360;
        return new IndicatorState(chunk, newYaw, moduleType);
    }

    /**
     * 이동된 새 상태 반환 (불변)
     */
    public IndicatorState moveTo(Chunk newChunk) {
        return new IndicatorState(newChunk, yaw, moduleType);
    }
}