package studio.semicolon.prc.core.module.door;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import studio.semicolon.prc.core.module.ModuleMetadata;
import studio.semicolon.prc.core.module.Structures;

import java.util.Set;

public class DoorConditionChecker {
    private DoorConditionChecker() { }

    private static final double ACTIVATION_RANGE = 3.0;
    private static final double CONNECTION_RANGE = 5.05;

    public static boolean shouldOpen(AutomaticDoor door, Player player, Set<AutomaticDoor> allDoors) {
        double distance = door.location().distance(player.getLocation());
        if (distance > ACTIVATION_RANGE) return false;

        boolean hasStructure = hasConnectedStructure(door, player);
        if (!hasStructure) return false;

        return hasNearbyDoor(door, allDoors);
    }

    /**
     * 플레이어-문 방향으로 구조물 연결 확인
     */
    private static boolean hasConnectedStructure(AutomaticDoor door, Player player) {
        Chunk playerChunk = player.getChunk();

        ModuleMetadata.Direction direction = Structures.getDirectionBetween(
                player.getLocation(),
                door.location()
        );

        Chunk targetChunk = Structures.getAdjacentChunk(playerChunk, direction);
        boolean targetOccupied = ModuleMetadata.isChunkOccupied(targetChunk);

        if (targetOccupied) return true;

        Chunk reverseChunk = Structures.getAdjacentChunk(playerChunk, direction.opposite());
        return ModuleMetadata.isChunkOccupied(reverseChunk);
    }


    /**
     * 근처에 다른 문이 있는지 확인 (쌍 문 조건)
     */
    private static boolean hasNearbyDoor(AutomaticDoor door, Set<AutomaticDoor> allDoors) {
        int count = 0;

        for (AutomaticDoor other : allDoors) {
            if (other.equals(door)) continue;
            if (!other.isValid()) continue;

            if (other.location().distance(door.location()) <= CONNECTION_RANGE) {
                count++;
            }
        }

        return count > 0;
    }
}