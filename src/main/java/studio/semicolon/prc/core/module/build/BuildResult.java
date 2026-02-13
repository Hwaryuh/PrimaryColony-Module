package studio.semicolon.prc.core.module.build;

import net.kyori.adventure.text.Component;
import studio.semicolon.prc.api.constant.text.ModuleMessages;

public enum BuildResult {
    SUCCESS,
    CHUNK_OCCUPIED,
    CANNOT_CONNECT,
    INSUFFICIENT_ITEM;

    public boolean isSuccess() {
        return this == SUCCESS;
    }

    public Component getErrorMessage() {
        return switch (this) {
            case SUCCESS -> Component.empty();
            case CHUNK_OCCUPIED -> ModuleMessages.ALREADY_OCCUPIED;
            case CANNOT_CONNECT -> ModuleMessages.WRONG_DIRECTION;
            case INSUFFICIENT_ITEM -> ModuleMessages.INSUFFICIENT_MODULE_ITEM;
        };
    }
}