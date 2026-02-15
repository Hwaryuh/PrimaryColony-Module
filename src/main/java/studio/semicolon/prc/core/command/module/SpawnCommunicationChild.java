package studio.semicolon.prc.core.command.module;

import io.quill.paper.command.CommandResult;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import io.quill.paper.util.bukkit.Locations;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class SpawnCommunicationChild {
    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("spawnCommunicationModule").playerOnly()
                .runPlayer(ctx -> {
                    Player player = ctx.sender().player();
                    World world = player.getWorld();

                    Location loc1 = Locations.of(world, 180.5, -34, 168);
                    Location loc2 = Locations.of(world, 184, -34, 164.5);
                    Location loc3 = Locations.of(world, 187.5, -34, 168);
                    Location loc4 = Locations.of(world, 184, -34, 171.5);

                    for (Location location : new Location[]{loc1, loc2, loc3, loc4}) {
                        player.getWorld().spawn(location, Interaction.class, i -> {
                            i.getPersistentDataContainer().set(PDCKeys.of("communication_interaction"), PersistentDataType.BOOLEAN, true);
                            i.setResponsive(true);
                            i.setInteractionWidth(1.25f);
                            i.setInteractionHeight(2);
                        });
                    }
                    return CommandResult.success();
                })
                .build();
    }
}