package studio.semicolon.prc.core.util;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import studio.semicolon.prc.core.constant.text.UtilMessages;

public class Titles {
    public static void fade(Player player, Runnable callBack) {
        io.quill.paper.util.bukkit.Titles.showWithCallback(
                player,
                UtilMessages.FADE_BLACK,
                Component.empty(),
                0.5, 0.0, 0.5,
                0.5,
                callBack
        );
    }
}