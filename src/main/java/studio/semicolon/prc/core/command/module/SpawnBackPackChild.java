package studio.semicolon.prc.core.command.module;

import io.quill.paper.command.CommandResult;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import io.quill.paper.player.PlayerContext;
import io.quill.paper.player.PlayerContexts;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import studio.semicolon.prc.core.event.listener.game.BackPackListener;
import studio.semicolon.prc.core.event.listener.game.RoPAILeftClickListener;
import studio.semicolon.prc.core.event.listener.game.RoPAIRightClickListener;
import studio.semicolon.prc.core.event.listener.machine.MachineDestroyListener;

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
                        i.getPersistentDataContainer().set(BackPackListener.BACKPACK_FIRST, PersistentDataType.BOOLEAN, true);
                        i.setResponsive(true);
                    });
                    return CommandResult.success();
                })
                .build();
    }
}