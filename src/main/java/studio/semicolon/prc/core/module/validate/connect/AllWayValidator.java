package studio.semicolon.prc.core.module.validate.connect;

import org.bukkit.Chunk;

final class AllWayValidator implements ConnectionValidator {
    @Override
    public boolean canConnect(Chunk cameraChunk, Chunk indicatorChunk, float indicatorYaw) {
        return true;
    }
}