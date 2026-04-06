package studio.semicolon.prc.api.module;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import studio.semicolon.prc.core.game.GravityService;
import studio.semicolon.prc.core.module.ModuleMetadata;

public final class ModuleAPI {
    private ModuleAPI() {
    }

    public static boolean isInsideModule(Player player) {
        return isChunkOccupied(player.getChunk());
    }

    public static boolean isChunkOccupied(Chunk chunk) {
        return ModuleMetadata.isChunkOccupied(chunk);
    }

    public static void setGravity(Player player) {
        GravityService.set(player);
    }

    public static void restoreGravity(Player player) {
        GravityService.remove(player);
    }
}
