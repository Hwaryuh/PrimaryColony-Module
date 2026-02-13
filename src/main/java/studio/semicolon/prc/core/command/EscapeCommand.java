package studio.semicolon.prc.core.command;

import io.quill.paper.command.CommandResult;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import io.quill.paper.player.PlayerContext;
import io.quill.paper.player.PlayerContexts;
import io.quill.paper.util.bukkit.Potions;
import io.quill.paper.util.bukkit.Ticks;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import studio.semicolon.prc.core.util.Advancements;

public class EscapeCommand {
    private final static String COOLDOWN_KEY = "escape_cooldown";
    private final static int EFFECT_DURATION = Ticks.sec2Ticks(5.0);

    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("탈출")
                .playerOnly()
                .runPlayer(ctx -> {
                    Player player = ctx.sender().player();
                    PlayerContext playerContexts = PlayerContexts.ctx(player);

                    if (!playerContexts.cooldown(COOLDOWN_KEY)) {
                        Potions.of(PotionEffectType.LEVITATION)
                                .duration(EFFECT_DURATION)
                                .level(1)
                                .ambient(false)
                                .particles(true)
                                .icon(false)
                                .applyTo(player);

                        Advancements.grant(player, "planet/hidden/escape");
                        playerContexts.cooldown(COOLDOWN_KEY, EFFECT_DURATION);
                    }

                    return CommandResult.success();
                })
                .build();
    }
}