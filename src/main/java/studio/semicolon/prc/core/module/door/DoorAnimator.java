package studio.semicolon.prc.core.module.door;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.quill.paper.util.bukkit.Logger;
import io.quill.paper.util.bukkit.task.CancellableTask;
import io.quill.paper.util.bukkit.task.Tasks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import studio.semicolon.prc.api.constant.sound.PRCSounds;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class DoorAnimator {
    private static final int ANIMATION_DURATION = 15;
    private static final float ELEVATION_HEIGHT = 4.01f;
    private static final Transformation DEFAULT_TRANSFORM = new Transformation(
            new Vector3f(0, 1, 0),
            new AxisAngle4f(),
            new Vector3f(2, 2, 2),
            new AxisAngle4f()
    );

    private final Map<ItemDisplay, BarrierState> barrierStates = new WeakHashMap<>();
    private final Map<ItemDisplay, Set<CancellableTask>> tasks = new WeakHashMap<>();

    /**
     * 문 열기
     */
    public void elevate(AutomaticDoor door) {
        ItemDisplay display = door.display();
        if (!display.isValid()) return;

        cancelAnimation(display);

        door.setElevated(true);
        moveUp(display);
        removeBarriers(door);

        PRCSounds.MODULE_DOOR_OPEN.stopAndPlay(display.getLocation());
    }

    /**
     * 문 닫기
     */
    public void lower(AutomaticDoor door) {
        ItemDisplay display = door.display();
        if (!display.isValid()) return;

        cancelAnimation(display);

        door.setElevated(false);
        moveDown(display);

        CancellableTask restoreTask = Tasks.later(ANIMATION_DURATION, () -> {
            if (display.isValid()) {
                restoreBarriers(door);
            }
            clearTasks(display);
        });

        trackTask(display, restoreTask);
        PRCSounds.MODULE_DOOR_CLOSE.stopAndPlay(display.getLocation());
    }

    private void cancelAnimation(ItemDisplay display) {
        Set<CancellableTask> set = tasks.remove(display);
        if (set == null || set.isEmpty()) return;

        for (CancellableTask task : set) {
            if (task == null) continue;
            if (task.isCancelled()) continue;

            try {
                task.cancel();
            } catch (Exception e) {
                Logger.warn("Door task cancel failed", e);
            }
        }
    }

    private void trackTask(ItemDisplay display, CancellableTask task) {
        if (task == null) return;
        tasks.computeIfAbsent(display, k -> Sets.newHashSet()).add(task);
    }

    private void clearTasks(ItemDisplay display) {
        tasks.remove(display);
    }

    private void moveUp(ItemDisplay display) {
        Transformation current = display.getTransformation();

        Transformation elevated = new Transformation(
                new Vector3f(current.getTranslation().x, current.getTranslation().y + ELEVATION_HEIGHT, current.getTranslation().z),
                current.getLeftRotation(),
                new Vector3f(2, 0, 2),
                current.getRightRotation()
        );

        display.setTransformation(elevated);
        display.setInterpolationDelay(0);
        display.setInterpolationDuration(ANIMATION_DURATION);
    }

    private void moveDown(ItemDisplay display) {
        display.setTransformation(DEFAULT_TRANSFORM);
        display.setInterpolationDelay(0);
        display.setInterpolationDuration(ANIMATION_DURATION);
    }

    private void removeBarriers(AutomaticDoor door) {
        Location displayLoc = door.location();
        int baseY = displayLoc.getBlockY();

        Map<Integer, List<Location>> barriersByFloor = Maps.newHashMap();

        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = 0; y <= 3; y++) {
                    Location loc = displayLoc.clone().add(x, y, z);
                    Block block = loc.getBlock();

                    if (block.getType() == Material.BARRIER) {
                        int floor = (loc.getBlockY() - baseY) + 1;
                        if (floor >= 1 && floor <= 4) {
                            barriersByFloor
                                    .computeIfAbsent(floor, k -> Lists.newArrayList())
                                    .add(loc);
                        }
                    }
                }
            }
        }

        barrierStates.put(door.display(), new BarrierState(barriersByFloor));

        ItemDisplay display = door.display();
        for (int floor = 1; floor <= 4; floor++) {
            int delay = floor * 3;
            List<Location> locations = barriersByFloor.get(floor);

            if (locations != null && !locations.isEmpty()) {
                CancellableTask removeTask = Tasks.later(delay, () -> {
                    for (Location loc : locations) {
                        if (loc.isChunkLoaded()) {
                            loc.getBlock().setType(Material.AIR);
                        }
                    }
                });

                trackTask(display, removeTask);
            }
        }
    }

    private void restoreBarriers(AutomaticDoor door) {
        ItemDisplay display = door.display();
        if (!display.isValid()) {
            barrierStates.remove(display);
            return;
        }

        BarrierState state = barrierStates.remove(display);
        if (state == null) return;

        Map<Integer, List<Location>> barriersByFloor = state.locations();

        for (int floor = 1; floor <= 4; floor++) {
            List<Location> locations = barriersByFloor.get(floor);
            if (locations == null) continue;

            for (Location loc : locations) {
                if (!loc.isChunkLoaded()) {
                    Logger.warn("청크 언로드됨, 배리어 복구 불가: " + loc);
                    continue;
                }

                Block block = loc.getBlock();
                if (block.getType() != Material.AIR) {
                    Logger.warn("블럭이 AIR가 아님, 복구 스킵: " + loc + " (현재: " + block.getType() + ")");
                    continue;
                }

                block.setType(Material.BARRIER);
            }
        }
    }

    /**
     * 배리어 위치 저장
     */
    private record BarrierState(Map<Integer, List<Location>> locations) { }
}