package studio.semicolon.prc.core.command.module;

import io.quill.paper.command.CommandResult;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import io.quill.paper.player.PlayerContext;
import io.quill.paper.player.PlayerContexts;
import org.bukkit.entity.Player;
import studio.semicolon.prc.core.event.listener.game.RoPAILeftClickListener;
import studio.semicolon.prc.core.event.listener.game.RoPAIRightClickListener;
import studio.semicolon.prc.core.event.listener.machine.MachineDestroyListener;

public class ResetAdvancementChild {
    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("resetAdvancement").playerOnly()
                .runPlayer(ctx -> {
                    Player player = ctx.sender().player();
                    PlayerContext playerContext = PlayerContexts.ctx(player);

                    playerContext.resetCounter(RoPAILeftClickListener.COUNTER_KEY);
                    playerContext.resetCounter(RoPAIRightClickListener.COUNTER_KEY);
                    playerContext.resetCounter(MachineDestroyListener.COUNTER_KEY);
                    return CommandResult.success();
                })
                .build();
    }
}