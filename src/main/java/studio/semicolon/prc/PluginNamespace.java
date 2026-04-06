package studio.semicolon.prc;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public final class PluginNamespace {
    @NotNull
    public static String get() {
        return Module.getInstance().getName().toLowerCase();
    }

    @NotNull
    public static NamespacedKey key(@NotNull String key) {
        return new NamespacedKey(Module.getInstance(), key);
    }
}
