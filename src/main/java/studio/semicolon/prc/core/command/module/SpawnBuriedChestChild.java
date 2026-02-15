package studio.semicolon.prc.core.command.module;

import io.quill.paper.command.ArgumentKey;
import io.quill.paper.command.CommandResult;
import io.quill.paper.command.argument.ArgType;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import studio.semicolon.prc.core.event.listener.game.BuriedChestListener;

public class SpawnBuriedChestChild {
    private static final ArgumentKey<Integer> INDEX = ArgumentKey.intKey("index");

    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("spawnBuriedChest").playerOnly()
                .argument(INDEX, ArgType.integer(1, 4)).and()
                .runPlayer(ctx -> {
                    Player player = ctx.sender().player();
                    Location loc = player.getLocation();
                    NamespacedKey key;

                    switch (ctx.arg(INDEX)) {
                        case 1 -> {
                            loc.set(485.75, -18.0, 862.75);
                            key = BuriedChestListener.BURIED_HELMET;
                        }
                        case 2 -> {
                            loc.set(128.75, 91.0, 816.75);
                            key = BuriedChestListener.BURIED_CHESTPLATE;
                        }
                        case 3 -> {
                            loc.set(651.0, 1.0, 257.0);
                            key = BuriedChestListener.BURIED_LEGGINGS;
                        }
                        case 4 -> {
                            loc.set(233.25, 143.0, 495.0);
                            key = BuriedChestListener.BURIED_BOOTS;
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + ctx.arg(INDEX));
                    }

                    loc.getWorld().spawn(loc, Interaction.class, i -> {
                        i.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
                        i.setResponsive(true);
                    });
                    return CommandResult.success();
                })
                .build();
    }
}