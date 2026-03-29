package studio.semicolon.prc.core.command.module;

import io.quill.paper.command.ArgumentKey;
import io.quill.paper.command.CommandResult;
import io.quill.paper.command.argument.ArgType;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import io.quill.paper.player.PlayerContext;
import io.quill.paper.player.PlayerContexts;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import studio.semicolon.prc.core.event.listener.game.BackPackListener;
import studio.semicolon.prc.core.event.listener.game.DocumentListener;
import studio.semicolon.prc.core.event.listener.game.RoPAILeftClickListener;
import studio.semicolon.prc.core.event.listener.game.RoPAIRightClickListener;
import studio.semicolon.prc.core.event.listener.machine.MachineDestroyListener;

public class ResetAdvancementChild {
    private static final ArgumentKey<Player> TARGET = ArgumentKey.of("target", Player.class);

    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("resetAdvancement").playerOnly()
                .argument(TARGET, ArgType.player()).and()
                .runPlayer(ctx -> {
                    Player target = ctx.arg(TARGET);
                    PlayerContext playerContext = PlayerContexts.ctx(target);

                    playerContext.resetCounter(RoPAILeftClickListener.COUNTER_KEY);
                    playerContext.resetCounter(RoPAIRightClickListener.COUNTER_KEY);
                    playerContext.resetCounter(MachineDestroyListener.COUNTER_KEY);
                    playerContext.resetCounter(BackPackListener.COUNTER_KEY);
                    playerContext.resetCounter(DocumentListener.COUNTER_KEY);
                    playerContext.removePersistentFlag(BackPackListener.FLAG_KEY);

                    target.sendActionBar(Component.text("상호작용 카운트 기록이 초기화 되었습니다."));
                    return CommandResult.success();
                })
                .build();
    }
}
