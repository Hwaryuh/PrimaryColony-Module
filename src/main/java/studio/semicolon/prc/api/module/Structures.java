package studio.semicolon.prc.api.module;

import com.google.common.collect.Lists;
import io.quill.paper.util.bukkit.Locations;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.structure.StructureRotation;

import java.util.List;

public class Structures {
    private Structures() { }

    private static final int CHUNK_SIZE = 16;
    private static final double CORNER_OFFSET = 0.5;

    /**
     * 인디케이터 yaw를 StructureRotation으로 변환
     * yaw 0 → 앞면 남쪽 → CLOCKWISE_180
     * yaw 90 → 앞면 서쪽 → COUNTERCLOCKWISE_90
     * yaw 180 → 앞면 북쪽 → NONE (구조물 저장 방향)
     * yaw 270 → 앞면 동쪽 → CLOCKWISE_90
     */
    public static StructureRotation getRotationFromYaw(float yaw) {
        int snapped = Yaws.snapToCardinal(yaw);

        return switch (snapped) {
            case 0 -> StructureRotation.CLOCKWISE_180;
            case 90 -> StructureRotation.COUNTERCLOCKWISE_90;
            case 180 -> StructureRotation.NONE;
            case 270 -> StructureRotation.CLOCKWISE_90;
            default -> throw new IllegalStateException("Unexpected yaw: " + yaw);
        };
    }

    /**
     * Rotation에 따른 청크의 기준 꼭짓점 계산
     */
    public static Location getCornerLocation(Chunk chunk, StructureRotation rotation, int y) {
        int x = chunk.getX() * CHUNK_SIZE;
        int z = chunk.getZ() * CHUNK_SIZE;

        return switch (rotation) {
            case NONE -> Locations.of(chunk.getWorld(), x + CORNER_OFFSET, y, z + CORNER_OFFSET);
            case CLOCKWISE_90 -> Locations.of(chunk.getWorld(), x + CHUNK_SIZE - CORNER_OFFSET, y, z + CORNER_OFFSET);
            case CLOCKWISE_180 -> Locations.of(chunk.getWorld(), x + CHUNK_SIZE - CORNER_OFFSET, y, z + CHUNK_SIZE - CORNER_OFFSET);
            case COUNTERCLOCKWISE_90 -> Locations.of(chunk.getWorld(), x + CORNER_OFFSET, y, z + CHUNK_SIZE - CORNER_OFFSET);
        };
    }

    public static Location getCenterLocation(Chunk chunk, double y) {
        int x = chunk.getX() * CHUNK_SIZE + CHUNK_SIZE / 2;
        int z = chunk.getZ() * CHUNK_SIZE + CHUNK_SIZE / 2;

        return Locations.of(chunk.getWorld(), x, y, z);
    }

    /**
     * 구조물이 점유할 청크 목록 반환 (rotation 고려)
     */
    public static List<Chunk> getOccupiedChunks(Chunk baseChunk, int chunksToOccupy, StructureRotation rotation) {
        List<Chunk> chunks = Lists.newArrayList(baseChunk);
        if (chunksToOccupy == 1) return chunks;

        int[] offset = getOffsetFromRotation(rotation);

        for (int i = 1; i < chunksToOccupy; i++) {
            chunks.add(baseChunk.getWorld().getChunkAt(
                    baseChunk.getX() + (offset[0] * i),
                    baseChunk.getZ() + (offset[1] * i)
            ));
        }

        return chunks;
    }

    public static int[] getOffsetFromRotation(StructureRotation rotation) {
        return switch (rotation) {
            case NONE -> new int[]{0, 1};
            case CLOCKWISE_90 -> new int[]{-1, 0};
            case CLOCKWISE_180 -> new int[]{0, -1};
            case COUNTERCLOCKWISE_90 -> new int[]{1, 0};
        };
    }
}