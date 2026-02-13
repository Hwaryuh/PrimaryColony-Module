package studio.semicolon.prc.core.module.build;

import io.quill.paper.util.bukkit.task.Tasks;
import org.bukkit.Location;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import studio.semicolon.prc.api.constant.sound.PRCSounds;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class BuildAnimation {
    private BuildAnimation() { }

    private static final float[] INTEGRITY_STAGES = { 0.1f, 0.5f, 1.0f };
    private static final float[] PITCH_STAGES = { 0.4f, 1.0f, 1.8f };
    private static final long INTERVAL = 25L;

    public static void start(Structure structure, Location location, StructureRotation rotation) {
        Random random = new Random();
        AtomicInteger stage = new AtomicInteger(0);

        Tasks.repeat(INTERVAL, INTERVAL, INTEGRITY_STAGES.length, () -> {
            int currentStage = stage.getAndIncrement();
            boolean includeEntities = (currentStage == INTEGRITY_STAGES.length - 1);

            structure.place(location, includeEntities, rotation, Mirror.NONE, 0, INTEGRITY_STAGES[currentStage], random);
            PRCSounds.MODULE_BUILD_PROGRESS.withPitch(PITCH_STAGES[currentStage]).play(location);
        });
    }
}