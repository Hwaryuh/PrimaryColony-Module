package studio.semicolon.prc.api.module;

public final class Yaws {
    private Yaws() { }

    public static float normalize(float yaw) {
        return ((yaw % 360) + 360) % 360;
    }

    public static int quantize(float yaw) {
        return Math.round(yaw / 90f) * 90 % 360;
    }

    public static int snapToCardinal(float yaw) {
        return quantize(normalize(yaw));
    }
}