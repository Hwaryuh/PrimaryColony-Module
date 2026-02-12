package studio.semicolon.prc.core.command;

import com.google.common.collect.Sets;
import io.quill.paper.command.ArgumentKey;
import io.quill.paper.command.CommandResult;
import io.quill.paper.command.SenderContext;
import io.quill.paper.command.argument.ArgType;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import studio.semicolon.prc.core.module.ModuleMetadata;
import studio.semicolon.prc.core.module.ModuleMetadataDebug;
import studio.semicolon.prc.core.module.menu.BuildPanelMenu;

import java.util.Set;

public class ModuleCommand {
    private static final ArgumentKey<Boolean> EAST = ArgumentKey.boolKey("east");
    private static final ArgumentKey<Boolean> WEST = ArgumentKey.boolKey("west");
    private static final ArgumentKey<Boolean> SOUTH = ArgumentKey.boolKey("south");
    private static final ArgumentKey<Boolean> NORTH = ArgumentKey.boolKey("north");

    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("module")//.aliases("md")
                .child(c -> c
                        .name("build").playerOnly()
                        .run(ctx -> {
                            Player player = ((SenderContext.PlayerOnly) ctx.sender()).player();
                            new BuildPanelMenu(player, player.getY() + 1).open();
                            return CommandResult.success();
                        })
                        .build()
                )
                .child(c -> c
                        .name("metadata").playerOnly()
                        .argument(EAST, ArgType.bool()).and()
                        .argument(WEST, ArgType.bool()).and()
                        .argument(SOUTH, ArgType.bool()).and()
                        .argument(NORTH, ArgType.bool()).and()
                        .run(ctx -> {
                            Player player = ((SenderContext.PlayerOnly) ctx.sender()).player();
                            Chunk chunk = player.getChunk();

                            boolean east = ctx.arg(EAST);
                            boolean west = ctx.arg(WEST);
                            boolean south = ctx.arg(SOUTH);
                            boolean north = ctx.arg(NORTH);

                            Set<ModuleMetadata.Direction> directions = Sets.newHashSet();
                            if (east) directions.add(ModuleMetadata.Direction.EAST);
                            if (west) directions.add(ModuleMetadata.Direction.WEST);
                            if (south) directions.add(ModuleMetadata.Direction.SOUTH);
                            if (north) directions.add(ModuleMetadata.Direction.NORTH);

                            ModuleMetadataDebug.setOccupied(chunk);
                            ModuleMetadataDebug.setConnections(chunk, directions);
                            player.sendMessage("Set: " + ModuleMetadataDebug.getInfo(chunk));

                            return CommandResult.success();
                        })
                        .build()
                )
                .child(c -> c
                        .name("info").playerOnly()
                        .run(ctx -> {
                            Player player = ((SenderContext.PlayerOnly) ctx.sender()).player();
                            Chunk chunk = player.getChunk();
                            ModuleMetadataDebug.DebugInfo info = ModuleMetadataDebug.getInfo(chunk);
                            player.sendMessage(info.toString());
                            return CommandResult.success();
                        })
                        .build()
                )
                .child(c -> c
                        .name("resetChunk").playerOnly()
                        .run(ctx -> {
                            Player player = ((SenderContext.PlayerOnly) ctx.sender()).player();
                            Chunk chunk = player.getChunk();
                            ModuleMetadataDebug.clearAll(chunk);
                            return CommandResult.success();
                        })
                        .build()
                )
                .build();
    }
}