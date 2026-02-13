package studio.semicolon.prc.core.module;

import com.google.common.collect.Sets;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import studio.semicolon.prc.api.module.Yaws;
import studio.semicolon.prc.api.module.indicator.IndicatorState;
import studio.semicolon.prc.core.module.validate.connect.ConnectRule;

import java.util.List;
import java.util.Set;

public class ModuleMetadata {
    private ModuleMetadata() { }

    static final NamespacedKey MODULE_DATA = PDCKeys.of("module_data");
    private static final NamespacedKey MODULE_OCCUPIED = PDCKeys.of("module_occupied");
    private static final NamespacedKey CONNECT_NORTH = PDCKeys.of("module_connect_north");
    private static final NamespacedKey CONNECT_SOUTH = PDCKeys.of("module_connect_south");
    private static final NamespacedKey CONNECT_EAST = PDCKeys.of("module_connect_east");
    private static final NamespacedKey CONNECT_WEST = PDCKeys.of("module_connect_west");

    public static void saveMetadata(IndicatorState state) {
        List<Chunk> chunks = state.getOccupiedChunks();

        for (Chunk chunk : chunks) {
            removeMetadata(chunk);
            setOccupied(chunk);
        }

        saveConnections(state);
    }

    public static void removeMetadata(Chunk chunk) {
        chunk.getPersistentDataContainer().remove(ModuleMetadata.MODULE_DATA);
    }

    public static boolean isChunkOccupied(Chunk chunk) {
        PersistentDataContainer pdc = chunk.getPersistentDataContainer();
        PersistentDataContainer container = pdc.get(MODULE_DATA, PersistentDataType.TAG_CONTAINER);

        if (container == null) return false;

        Boolean occupied = container.get(MODULE_OCCUPIED, PersistentDataType.BOOLEAN);
        return Boolean.TRUE.equals(occupied);
    }

    public static void setOccupied(Chunk chunk) {
        PersistentDataContainer pdc = chunk.getPersistentDataContainer();
        PersistentDataContainer container = pdc.get(MODULE_DATA, PersistentDataType.TAG_CONTAINER);

        if (container == null) {
            container = pdc.getAdapterContext().newPersistentDataContainer();
        }

        container.set(MODULE_OCCUPIED, PersistentDataType.BOOLEAN, true);
        pdc.set(MODULE_DATA, PersistentDataType.TAG_CONTAINER, container);
    }

    /**
     * 모듈 설치 시 각 청크에 연결 가능 방향 저장
     */
    public static void saveConnections(IndicatorState state) {
        List<Chunk> chunks = state.getOccupiedChunks();
        ModuleType type = state.moduleType();
        int chunkCount = type.getChunkCount();

        if (chunkCount == 1) {
            saveSingleChunkConnections(chunks.getFirst(), type.getConnectRule(), state.yaw());
        } else {
            saveMultiChunkConnections(chunks, state.getRotation());
        }
    }

    private static void saveSingleChunkConnections(Chunk chunk, ConnectRule rule, float yaw) {
        Set<Direction> allowedDirections = getConnectionDirections(rule, yaw);

        for (Direction direction : Direction.values()) {
            setDirection(chunk, direction, allowedDirections.contains(direction));
        }
    }

    private static void saveMultiChunkConnections(List<Chunk> chunks, StructureRotation rotation) {
        // 첫 청크: 연장의 반대 방향만 연결 가능
        Direction outwardDirection = getOppositeOfExtension(rotation);
        setDirection(chunks.getFirst(), outwardDirection, true);

        // 마지막 청크: 연장 방향만 연결 가능
        Direction extensionDirection = getExtensionDirection(rotation);
        setDirection(chunks.getLast(), extensionDirection, true);

        // 중간 청크: 아무것도 저장 안 함 (모든 방향 false, 연결 불가)
    }

    /**
     * ConnectRule과 yaw에 따른 연결 가능 방향 계산
     */
    private static Set<Direction> getConnectionDirections(ConnectRule rule, float yaw) {
        int snapped = Yaws.snapToCardinal(yaw);

        return switch (rule) {
            case FRONT_ONLY -> {
                Direction front = getFrontDirection(snapped);
                yield Sets.newHashSet(front);
            }
            case TWO_WAY -> {
                Direction front = getFrontDirection(snapped);
                Direction back = front.opposite();
                yield Sets.newHashSet(front, back);
            }
            case THREE_WAY -> {
                Direction blocked = getBlockedDirection(snapped);
                Set<Direction> connections = Sets.newHashSet();
                if (Direction.NORTH != blocked) connections.add(Direction.NORTH);
                if (Direction.SOUTH != blocked) connections.add(Direction.SOUTH);
                if (Direction.EAST != blocked) connections.add(Direction.EAST);
                if (Direction.WEST != blocked) connections.add(Direction.WEST);
                yield connections;
            }
            case ALL_WAY -> Sets.newHashSet(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
        };
    }

    /**
     * yaw에 따른 앞면 방향
     */
    private static Direction getFrontDirection(int yaw) {
        return switch (yaw) {
            case 0 -> Direction.SOUTH;
            case 90 -> Direction.WEST;
            case 180 -> Direction.NORTH;
            case 270 -> Direction.EAST;
            default -> throw new IllegalArgumentException("Invalid yaw: " + yaw);
        };
    }

    /**
     * THREE_WAY 모듈에서 차단되는 방향
     * yaw 0 (앞면 남쪽) → 서쪽 막힘
     * yaw 90 (앞면 서쪽) → 북쪽 막힘
     * yaw 180 (앞면 북쪽) → 동쪽 막힘
     * yaw 270 (앞면 동쪽) → 남쪽 막힘
     */
    private static Direction getBlockedDirection(int yaw) {
        return switch (yaw) {
            case 0 -> Direction.WEST;
            case 90 -> Direction.NORTH;
            case 180 -> Direction.EAST;
            case 270 -> Direction.SOUTH;
            default -> throw new IllegalArgumentException("Invalid yaw: " + yaw);
        };
    }

    /**
     * 구조물이 연장되는 방향
     */
    static Direction getExtensionDirection(StructureRotation rotation) {
        return switch (rotation) {
            case NONE -> Direction.SOUTH;              // offset [0, 1]
            case CLOCKWISE_90 -> Direction.WEST;       // offset [-1, 0]
            case CLOCKWISE_180 -> Direction.NORTH;     // offset [0, -1]
            case COUNTERCLOCKWISE_90 -> Direction.EAST; // offset [1, 0]
        };
    }

    /**
     * 연장의 반대 방향 (첫 청크가 연결 가능한 방향)
     */
    static Direction getOppositeOfExtension(StructureRotation rotation) {
        return getExtensionDirection(rotation).opposite();
    }

    /**
     * 청크에 방향별 연결 가능 여부 저장
     */
    static void setDirection(Chunk chunk, Direction direction, boolean canConnect) {
        PersistentDataContainer pdc = chunk.getPersistentDataContainer();
        PersistentDataContainer container = pdc.get(MODULE_DATA, PersistentDataType.TAG_CONTAINER);

        if (container == null) {
            container = pdc.getAdapterContext().newPersistentDataContainer();
        }

        NamespacedKey key = getKeyForDirection(direction);
        if (canConnect) {
            container.set(key, PersistentDataType.BOOLEAN, true);
        }
        pdc.set(MODULE_DATA, PersistentDataType.TAG_CONTAINER, container);
    }

    /**
     * 카메라 청크에서 특정 방향으로 연결 가능한지 확인
     */
    public static boolean canConnectToDirection(Chunk chunk, Direction direction) {
        PersistentDataContainer pdc = chunk.getPersistentDataContainer();
        PersistentDataContainer container = pdc.get(MODULE_DATA, PersistentDataType.TAG_CONTAINER);

        if (container == null) return false;

        NamespacedKey key = getKeyForDirection(direction);
        Boolean value = container.get(key, PersistentDataType.BOOLEAN);
        return Boolean.TRUE.equals(value);
    }

    static NamespacedKey getKeyForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> CONNECT_NORTH;
            case SOUTH -> CONNECT_SOUTH;
            case EAST -> CONNECT_EAST;
            case WEST -> CONNECT_WEST;
        };
    }

    public enum Direction {
        NORTH, SOUTH, EAST, WEST;

        public Direction opposite() {
            return switch (this) {
                case NORTH -> SOUTH;
                case SOUTH -> NORTH;
                case EAST -> WEST;
                case WEST -> EAST;
            };
        }
    }
}