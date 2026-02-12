package studio.semicolon.prc.core.command.module;

import io.quill.paper.command.CommandResult;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import org.bukkit.entity.Player;
import studio.semicolon.prc.core.module.menu.BuildPanelMenu;

public class BuildChild {
    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("build").playerOnly()
                .runPlayer(ctx -> {
                    Player player = ctx.sender().player();
                    new BuildPanelMenu(player, player.getY() + 1).open();
                    return CommandResult.success();
                })
                .build();
    }
}