package studio.semicolon.prc.core.command;

import io.quill.paper.command.ArgumentKey;
import io.quill.paper.command.CommandResult;
import io.quill.paper.command.argument.ArgType;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TestLabel {
    private static final ArgumentKey<Player> TARGET = ArgumentKey.of("target", Player.class);
    private static final ArgumentKey<Integer> AMOUNT = ArgumentKey.intKey("amount");
    private static final ArgumentKey<Location> LOCATION = ArgumentKey.of("location", Location.class);

    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("justtest")
                .child(c -> c
                        .name("pay").playerOnly()
                        .argument(TARGET, ArgType.player()).and()
                        .argument(AMOUNT, ArgType.integer(1, 1_000_000)).and()
                        .runPlayer(ctx -> {
                            Player player = ctx.sender().player();
                            Player target = ctx.arg(TARGET);
                            if (player.equals(target)) return CommandResult.failure("자기 자신에게는 송금할 수 없습니다.");

                            int amount = ctx.arg(AMOUNT);
                            player.sendMessage(Component.text(target.getName() + "에게 $" + amount + "를 송금했습니다."));
                            target.sendMessage(Component.text(player.getName() + "에게서 $" + amount + "를 받았습니다."));

                            return CommandResult.success();
                        })
                        .build()
                )
                .child(c -> c
                        .name("tp").playerOnly()
                        .argument(LOCATION, ArgType.location()).and()
                        .runPlayer(ctx -> {
                            Player player = ctx.sender().player();
                            Location location = ctx.arg(LOCATION);

                            player.teleport(location);
                            return CommandResult.success();
                        })
                        .build()
                )
                .build();
    }
}