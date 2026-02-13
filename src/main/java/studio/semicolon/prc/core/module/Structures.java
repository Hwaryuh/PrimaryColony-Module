package studio.semicolon.prc.core.module;

import io.quill.paper.util.bukkit.Locations;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.structure.StructureRotation;
import studio.semicolon.prc.api.module.Yaws;

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

    public static int[] getOffsetFromRotation(StructureRotation rotation) {
        return switch (rotation) {
            case NONE -> new int[]{0, 1};
            case CLOCKWISE_90 -> new int[]{-1, 0};
            case CLOCKWISE_180 -> new int[]{0, -1};
            case COUNTERCLOCKWISE_90 -> new int[]{1, 0};
        };
    }

    /**
     * 두 위치 사이의 방향 계산 (Location 기반)
     */
    public static ModuleMetadata.Direction getDirectionBetween(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();

        double angle = Math.atan2(dz, dx);
        double degrees = Math.toDegrees(angle);

        if (degrees < 0) {
            degrees += 360;
        }

        if (degrees >= 315 || degrees < 45) {
            return ModuleMetadata.Direction.EAST;
        } else if (degrees >= 45 && degrees < 135) {
            return ModuleMetadata.Direction.SOUTH;
        } else if (degrees >= 135 && degrees < 225) {
            return ModuleMetadata.Direction.WEST;
        } else {
            return ModuleMetadata.Direction.NORTH;
        }
    }

    /**
     * 두 청크 사이의 방향 계산 (인접 청크 전용)
     */
    public static ModuleMetadata.Direction getDirectionBetween(Chunk from, Chunk to) {
        int dx = to.getX() - from.getX();
        int dz = to.getZ() - from.getZ();

        if (dx == 1 && dz == 0) return ModuleMetadata.Direction.EAST;
        if (dx == -1 && dz == 0) return ModuleMetadata.Direction.WEST;
        if (dx == 0 && dz == 1) return ModuleMetadata.Direction.SOUTH;
        if (dx == 0 && dz == -1) return ModuleMetadata.Direction.NORTH;

        throw new IllegalArgumentException("Chunks are not adjacent: from(" + from.getX() + "," + from.getZ() + ") to(" + to.getX() + "," + to.getZ() + ")");
    }

    /**
     * 방향에 따라 인접 청크 가져오기
     */
    public static Chunk getAdjacentChunk(Chunk chunk, ModuleMetadata.Direction direction) {
        int[] offset = getOffsetFromDirection(direction);
        return chunk.getWorld().getChunkAt(
                chunk.getX() + offset[0],
                chunk.getZ() + offset[1]
        );
    }

    /**
     * Direction → 청크 오프셋 변환
     */
    public static int[] getOffsetFromDirection(ModuleMetadata.Direction direction) {
        return switch (direction) {
            case SOUTH -> new int[]{0, 1};
            case NORTH -> new int[]{0, -1};
            case EAST -> new int[]{1, 0};
            case WEST -> new int[]{-1, 0};
        };
    }
}