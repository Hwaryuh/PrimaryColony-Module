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
import studio.semicolon.prc.core.menu.AdminMenu;

public class AdminChild {
    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("admin").playerOnly()
                .runPlayer(ctx -> {
                    Player player = ctx.sender().player();

                    new AdminMenu(player).open();
                    return CommandResult.success();
                })
                .build();
    }
}
