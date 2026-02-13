package studio.semicolon.prc.core.command.module;

import io.quill.paper.command.CommandResult;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import studio.semicolon.prc.core.event.listener.game.BackPackListener;

public class SpawnBackPackChild {
    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("spawnBackPack").playerOnly()
                .runPlayer(ctx -> {
                    Player player = ctx.sender().player();

                    Location loc = player.getLocation();
                    loc.setX(177.5);
                    loc.setY(-33);
                    loc.setZ(176.5);

                    player.getWorld().spawn(loc, Interaction.class, i -> {
                        i.getPersistentDataContainer().set(BackPackListener.BACKPACK_KEY, PersistentDataType.BOOLEAN, true);
                        i.getPersistentDataContainer().set(BackPackListener.BACKPACK_SPECIAL, PersistentDataType.BOOLEAN, true);
                        i.setResponsive(true);
                    });
                    return CommandResult.success();
                })
                .build();
    }
}