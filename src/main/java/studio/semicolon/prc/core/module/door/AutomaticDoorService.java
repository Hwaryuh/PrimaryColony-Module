package studio.semicolon.prc.core.module.door;

import com.google.common.collect.Sets;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.Set;

public class AutomaticDoorService {
    private static final AutomaticDoorService INSTANCE = new AutomaticDoorService();

    private final DoorAnimator animator = new DoorAnimator();

    private AutomaticDoorService() { }

    public static AutomaticDoorService getInstance() {
        return INSTANCE;
    }

    /**
     * 플레이어 주변 문 상태 업데이트
     */
    public void updateDoors(Player player) {
        GameMode gameMode = player.getGameMode();
        if (gameMode == GameMode.SPECTATOR) return;

        Set<AutomaticDoor> nearbyDoors = findDoorsNearPlayer(player);

        for (AutomaticDoor door : nearbyDoors) {
            if (!door.isValid()) continue;

            boolean shouldBeOpen = shouldDoorBeOpen(door, nearbyDoors);
            boolean isCurrentlyOpen = door.isElevated();

            if (shouldBeOpen && !isCurrentlyOpen) {
                animator.elevate(door);
            } else if (!shouldBeOpen && isCurrentlyOpen) {
                animator.lower(door);
            }
        }
    }

    /**
     * 플레이어 주변 자동문 찾기 (3x3 청크)
     */
    private Set<AutomaticDoor> findDoorsNearPlayer(Player player) {
        Chunk playerChunk = player.getChunk();
        World world = player.getWorld();

        int baseX = playerChunk.getX();
        int baseZ = playerChunk.getZ();

        Set<AutomaticDoor> nearbyDoors = Sets.newHashSet();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                int cx = baseX + dx;
                int cz = baseZ + dz;

                if (!world.isChunkLoaded(cx, cz)) continue;
                Chunk chunk = world.getChunkAt(cx, cz);

                for (Entity entity : chunk.getEntities()) {
                    if (!(entity instanceof ItemDisplay display)) continue;

                    AutomaticDoor door = new AutomaticDoor(display);
                    if (door.isValid()) {
                        nearbyDoors.add(door);
                    }
                }
            }
        }

        return nearbyDoors;
    }

    /**
     * 문이 열려있어야 하는지 판단
     */
    private boolean shouldDoorBeOpen(AutomaticDoor door, Set<AutomaticDoor> nearbyDoors) {
        for (Player p : door.location().getWorld().getPlayers()) {
            if (p.getGameMode() == GameMode.SPECTATOR) continue;
            if (DoorConditionChecker.shouldOpen(door, p, nearbyDoors)) {
                return true;
            }
        }
        return false;
    }
}