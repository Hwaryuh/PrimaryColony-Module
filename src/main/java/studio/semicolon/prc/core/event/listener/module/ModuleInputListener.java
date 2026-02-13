package studio.semicolon.prc.core.event.listener.module;

import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.util.bukkit.Logger;
import io.quill.paper.util.bukkit.Titles;
import org.bukkit.Chunk;
import org.bukkit.Input;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInputEvent;
import studio.semicolon.prc.core.module.validate.BuildValidationResult;
import studio.semicolon.prc.core.module.indicator.IndicatorMovement;
import studio.semicolon.prc.core.module.indicator.IndicatorState;
import studio.semicolon.prc.api.module.RelativeDirection;
import studio.semicolon.prc.core.constant.sound.PRCSounds;
import studio.semicolon.prc.core.constant.text.ModuleMessages;
import studio.semicolon.prc.core.module.build.BuildResult;
import studio.semicolon.prc.core.module.build.BuildSessionManager;
import studio.semicolon.prc.core.module.build.BuildSession;
import studio.semicolon.prc.core.module.validate.BuildValidator;
import studio.semicolon.prc.core.module.indicator.IndicatorRenderer;
import studio.semicolon.prc.core.module.build.ModuleBuilder;
import studio.semicolon.prc.core.module.exception.StructureLoadException;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class ModuleInputListener implements EventSubscriber<PlayerInputEvent, ModuleInputListener.Context> {
    private static final int CONFIRM_TICKS = 20;

    public record Context(BuildSession session) implements EventContext, EventContext.Data { }

    @Override
    public Optional<Context> expect(PlayerInputEvent e) {
        BuildSession session = BuildSessionManager.getInstance().getSession(e.getPlayer());
        return session != null ? Optional.of(new Context(session)) : Optional.empty();
    }

    @Override
    public EventResult onEvent(PlayerInputEvent e, Context ctx) {
        Input input = e.getInput();
        BuildSession session = ctx.session();
        Player player = e.getPlayer();

        RelativeDirection direction = RelativeDirection.fromInput(input);
        if (direction != null) {
            onMovement(session, direction);
            return EventResult.STOP;
        }

        if (input.isSprint()) {
            onRotation(session, player);
            return EventResult.STOP;
        }

        if (input.isJump()) {
            onConfirm(session, player);
            return EventResult.STOP;
        }

        if (input.isSneak()) {
            PRCSounds.MODULE_BUILD_MODE_CANCEL.play(player);
            BuildSessionManager.getInstance().beginExit(player);
            return EventResult.STOP;
        }

        return EventResult.STOP;
    }

    private void onMovement(BuildSession session, RelativeDirection direction) {
        Chunk cameraChunk = session.getCameraChunk();
        float cameraYaw = session.getCameraEntity().getLocation().getYaw();

        Chunk targetChunk = IndicatorMovement.getTargetChunk(cameraChunk, direction, cameraYaw);
        if (isSameChunk(targetChunk, cameraChunk)) return;

        IndicatorState newState = session.getIndicatorState().moveTo(targetChunk);

        BuildValidationResult validation = new BuildValidationResult(
                BuildValidator.isOccupied(newState),
                BuildValidator.canConnect(session.getCameraChunk(), newState, session.getCameraChunkConnections())
        );

        session.updateState(newState, validation);

        IndicatorRenderer.render(session.getIndicatorDisplay(), newState, session.getIndicatorHeight(), validation.toRenderState());
        PRCSounds.MODULE_MOVE_INDICATOR.play(session.getPlayer());

        if (!validation.canConnect()) {
            session.updateHUD(ModuleMessages.GUIDE_HUD_WITH_WARNING);
        } else {
            session.resetHUD();
        }
    }

    private void onRotation(BuildSession session, Player player) {
        IndicatorState current = session.getIndicatorState();
        IndicatorState rotated = current.rotate();

        BuildValidationResult validation = new BuildValidationResult(
                BuildValidator.isOccupied(rotated),
                BuildValidator.canConnect(session.getCameraChunk(), rotated, session.getCameraChunkConnections())
        );

        session.updateState(rotated, validation);

        IndicatorRenderer.render(
                session.getIndicatorDisplay(),
                rotated,
                session.getIndicatorHeight(),
                validation.toRenderState()
        );

        PRCSounds.MODULE_MOVE_INDICATOR.play(player);

        if (!validation.canConnect()) {
            session.updateHUD(ModuleMessages.GUIDE_HUD_WITH_WARNING);
        } else {
            session.resetHUD();
        }
    }

    private void onConfirm(BuildSession session, Player player) {
        BuildResult canBuild = ModuleBuilder.canBuild(player, session.getIndicatorState(), session.getValidationResult());

        if (!canBuild.isSuccess()) {
            player.sendActionBar(canBuild.getErrorMessage());
            PRCSounds.GLOBAL_ERROR.play(player);
            return;
        }

        boolean confirmed = session.tryConfirm(CONFIRM_TICKS);

        if (!confirmed) {
            Titles.show(player, ModuleMessages.CONFIRM_TITLE, ModuleMessages.CONFIRM_SUB_TITLE, 0.2, 0.6, 0.2);
            PRCSounds.GLOBAL_ERROR.withPitch(1.4F).play(player);
            return;
        }

        try {
            ModuleBuilder.build(player, session.getIndicatorState(), session.getValidationResult(), session.getStructureCoordinateY());
            PRCSounds.MODULE_VALIDATE_SUCCESS.play(player);
            BuildSessionManager.getInstance().beginExit(player);
        } catch (StructureLoadException e) {
            Logger.warn("Structure load failed", e);
            player.sendActionBar(ModuleMessages.NOT_FOUND_STRUCTURE);
            PRCSounds.GLOBAL_ERROR.play(player);
        }
    }

    private static boolean isSameChunk(Chunk a, Chunk b) {
        return a.getX() == b.getX() && a.getZ() == b.getZ();
    }
}