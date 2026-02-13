package studio.semicolon.prc.api.constant.text;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public interface UtilMessages {
    Component FADE_BLACK = Component.text("o", NamedTextColor.BLACK).font(Key.key("minecraft", "semicolon"));
    Component WIKI_LINK = Component.text("[위키 링크]", TextColor.color(250, 130, 55))
            .clickEvent(ClickEvent.openUrl("https://www.notion.so/26780abaaada806a985fc931ef1b715d?v=26780abaaada8045a503000cef475764&source"))
            .hoverEvent(HoverEvent.showText(Component.text("Primary Colony 프로젝트에 대하여...", TextColor.color(228, 233, 234))));
}