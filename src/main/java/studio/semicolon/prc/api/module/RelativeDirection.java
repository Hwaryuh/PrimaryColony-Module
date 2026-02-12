package studio.semicolon.prc.api.module;

import org.bukkit.Input;

@SuppressWarnings("UnstableApiUsage")
public enum RelativeDirection {
    FORWARD(0),
    BACKWARD(180),
    LEFT(270),
    RIGHT(90);

    private final float yawOffset;

    RelativeDirection(float yawOffset) {
        this.yawOffset = yawOffset;
    }

    public float getYawOffset() {
        return yawOffset;
    }

    public static RelativeDirection fromInput(Input input) {
        if (input.isForward()) return FORWARD;
        if (input.isBackward()) return BACKWARD;
        if (input.isLeft()) return LEFT;
        if (input.isRight()) return RIGHT;
        return null;
    }
}