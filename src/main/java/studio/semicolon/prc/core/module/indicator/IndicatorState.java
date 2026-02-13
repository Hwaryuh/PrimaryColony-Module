package studio.semicolon.prc.core.module.indicator;

import org.bukkit.Chunk;
import org.bukkit.block.structure.StructureRotation;
import studio.semicolon.prc.core.module.Structures;
import studio.semicolon.prc.api.module.Yaws;
import studio.semicolon.prc.core.module.ModuleType;
import studio.semicolon.prc.core.module.build.OccupiedChunks;

public record IndicatorState(
        Chunk chunk,
        float yaw,
        ModuleType moduleType,
        OccupiedChunks occupiedChunks  // 추가
) {
    /**
     * 생성자: occupiedChunks를 즉시 계산
     */
    public IndicatorState(Chunk chunk, float yaw, ModuleType moduleType) {
        this(
                chunk,
                Yaws.normalize(yaw),
                moduleType,
                OccupiedChunks.from(chunk, moduleType, Structures.getRotationFromYaw(Yaws.normalize(yaw)))
        );
    }

    /**
     * Canonical constructor: validation
     */
    public IndicatorState {
        if (chunk == null) throw new IllegalArgumentException("chunk cannot be null");
        if (moduleType == null) throw new IllegalArgumentException("moduleType cannot be null");
        if (occupiedChunks == null) throw new IllegalArgumentException("occupiedChunks cannot be null");
    }

    public StructureRotation getRotation() {
        return Structures.getRotationFromYaw(yaw);
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