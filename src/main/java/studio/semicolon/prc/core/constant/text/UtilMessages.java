package studio.semicolon.prc.core.constant.text;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public interface UtilMessages {
    Component FADE_BLACK = Component.text("o", NamedTextColor.BLACK).font(Key.key("minecraft", "semicolon"));
}