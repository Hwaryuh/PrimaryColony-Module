package studio.semicolon.prc.core.command.module;

import io.quill.paper.command.CommandResult;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import studio.semicolon.prc.core.module.ModuleMetadataDebug;

public class ResetChunkChild {
    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("resetChunk").playerOnly()
                .runPlayer(ctx -> {
                    Player player = ctx.sender().player();
                    Chunk chunk = player.getChunk();
                    ModuleMetadataDebug.clearAll(chunk);
                    return CommandResult.success();
                })
                .build();
    }
}