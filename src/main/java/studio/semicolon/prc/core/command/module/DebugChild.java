package studio.semicolon.prc.core.command.module;

import com.google.common.collect.Sets;
import io.quill.paper.command.ArgumentKey;
import io.quill.paper.command.CommandResult;
import io.quill.paper.command.ExecutionContext;
import io.quill.paper.command.SenderContext;
import io.quill.paper.command.argument.ArgType;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import studio.semicolon.prc.core.module.ModuleMetadata;
import studio.semicolon.prc.core.module.ModuleMetadataDebug;

import java.util.Set;

public class DebugChild {
    private static final ArgumentKey<Boolean> EAST = ArgumentKey.boolKey("east");
    private static final ArgumentKey<Boolean> WEST = ArgumentKey.boolKey("west");
    private static final ArgumentKey<Boolean> SOUTH = ArgumentKey.boolKey("south");
    private static final ArgumentKey<Boolean> NORTH = ArgumentKey.boolKey("north");

    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("debug")
                .child(c -> c
                        .name("info").playerOnly()
                        .runPlayer(ctx -> {
                            Player player = ctx.sender().player();
                            player.sendMessage(ModuleMetadataDebug.getInfo(player.getChunk()).toString());
                            return CommandResult.success();
                        })
                        .build()
                )
                .child(c -> c
                        .name("setMetaData").playerOnly()
                        .argument(EAST, ArgType.bool()).and()
                        .argument(WEST, ArgType.bool()).and()
                        .argument(SOUTH, ArgType.bool()).and()
                        .argument(NORTH, ArgType.bool()).and()
                        .runPlayer(ctx -> {
                            Player player = ctx.sender().player();
                            Chunk chunk = player.getChunk();

                            Set<ModuleMetadata.Direction> directions = extractDirections(ctx);

                            ModuleMetadataDebug.setOccupied(chunk);
                            ModuleMetadataDebug.setConnections(chunk, directions);
                            player.sendMessage("Set: " + ModuleMetadataDebug.getInfo(chunk));

                            return CommandResult.success();
                        })
                        .build()
                )
                .build();
    }

    private static Set<ModuleMetadata.Direction> extractDirections(ExecutionContext<SenderContext.PlayerOnly> ctx) {
        Set<ModuleMetadata.Direction> directions = Sets.newHashSet();

        if (ctx.arg(EAST)) directions.add(ModuleMetadata.Direction.EAST);
        if (ctx.arg(WEST)) directions.add(ModuleMetadata.Direction.WEST);
        if (ctx.arg(SOUTH)) directions.add(ModuleMetadata.Direction.SOUTH);
        if (ctx.arg(NORTH)) directions.add(ModuleMetadata.Direction.NORTH);
        return directions;
    }
}