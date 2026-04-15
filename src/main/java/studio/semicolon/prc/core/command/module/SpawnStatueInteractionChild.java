package studio.semicolon.prc.core.command.module;

import io.quill.paper.command.ArgumentKey;
import io.quill.paper.command.CommandResult;
import io.quill.paper.command.argument.ArgType;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import studio.semicolon.prc.core.event.listener.game.StatueListener;

public class SpawnStatueInteractionChild {
    private static final ArgumentKey<Integer> INDEX = ArgumentKey.intKey("index");

    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("spawnStatue").playerOnly()
                .argument(INDEX, ArgType.integer(1, 13)).and()
                .runPlayer(ctx -> {
                    Player player = ctx.sender().player();
                    int index = ctx.arg(INDEX);

                    player.getWorld().spawn(player.getLocation(), Interaction.class, i -> {
                        i.getPersistentDataContainer().set(StatueListener.STATUE_KEY, PersistentDataType.INTEGER, index);
                        i.setInteractionWidth(1.0F);
                        i.setInteractionHeight(1.0F);
                        i.setResponsive(true);
                    });
                    return CommandResult.success();
                })
                .build();
    }
}
