package studio.semicolon.prc.core.module.validate.connect;

import org.bukkit.Chunk;

public enum ConnectRule {
    FRONT_ONLY(new FrontOnlyValidator()),
    TWO_WAY(new TwoWayValidator()),
    THREE_WAY(new ThreeWayValidator()),
    ALL_WAY(new AllWayValidator());

    private final ConnectionValidator validator;

    ConnectRule(ConnectionValidator validator) {
        this.validator = validator;
    }

    public boolean validate(Chunk cameraChunk, Chunk indicatorChunk, float indicatorYaw) {
        return validator.canConnect(cameraChunk, indicatorChunk, indicatorYaw);
    }
}