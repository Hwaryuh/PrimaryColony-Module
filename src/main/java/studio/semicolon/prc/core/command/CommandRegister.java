package studio.semicolon.prc.core.command;

import com.google.common.collect.Sets;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.quill.paper.Bootable;
import io.quill.paper.command.ArgumentKey;
import io.quill.paper.command.CommandRegistry;
import io.quill.paper.command.CommandResult;
import io.quill.paper.command.SenderContext;
import io.quill.paper.command.argument.ArgType;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import studio.semicolon.prc.core.module.ModuleMetadata;
import studio.semicolon.prc.core.module.ModuleMetadataDebug;
import studio.semicolon.prc.core.module.menu.BuildPanelMenu;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class CommandRegister implements Bootable {
    private static final ArgumentKey<Player> TARGET = ArgumentKey.of("target", Player.class);
    private static final ArgumentKey<Integer> AMOUNT = ArgumentKey.intKey("amount");
    private static final ArgumentKey<Location> LOCATION = ArgumentKey.of("location", Location.class);

    private static final ArgumentKey<Boolean> EAST = ArgumentKey.boolKey("east");
    private static final ArgumentKey<Boolean> WEST = ArgumentKey.boolKey("west");
    private static final ArgumentKey<Boolean> SOUTH = ArgumentKey.boolKey("south");
    private static final ArgumentKey<Boolean> NORTH = ArgumentKey.boolKey("north");

    @Override
    public void start(JavaPlugin plugin) {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            CommandRegistry registry = new CommandRegistry(event.registrar());
            registry.register(getTestCommand());
        });
    }

    private QuillCommand getTestCommand() {
        return QuillCommandBuilder.create()
                .name("module")//.aliases("prcmodule")

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

//                .child(c -> c
//                        .name("pay").playerOnly()
//                        .argument(TARGET, ArgType.player()).and()
//                        .argument(AMOUNT, ArgType.integer(1, 1_000_000)).and()
//                        .run(ctx -> {
//                            Player sender = ((SenderContext.PlayerOnly) ctx.sender()).player();
//                            Player target = ctx.arg(TARGET);
//                            if (sender.equals(target)) return CommandResult.failure("자기 자신에게는 송금할 수 없습니다.");
//
//                            int amount = ctx.arg(AMOUNT);
//                            sender.sendMessage(Component.text(target.getName() + "에게 $" + amount + "를 송금했습니다."));
//                            target.sendMessage(Component.text(sender.getName() + "에게서 $" + amount + "를 받았습니다."));
//
//                            return CommandResult.success();
//                        })
//                        .build()
//                )
//
//                .child(c -> c
//                        .name("tp").playerOnly()
//                        .argument(LOCATION, ArgType.location()).and()
//                        .run(ctx -> {
//                            Player player = ((SenderContext.PlayerOnly) ctx.sender()).player();
//                            Location location = ctx.arg(LOCATION);
//
//                            player.teleport(location);
//                            return CommandResult.success();
//                        })
//                        .build()
//                )

                .build();
    }

    @Override
    public void end(JavaPlugin plugin) { }
}