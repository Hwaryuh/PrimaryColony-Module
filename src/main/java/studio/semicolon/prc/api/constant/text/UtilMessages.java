package studio.semicolon.prc.api.constant.text;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public interface UtilMessages {
    TextColor TUTORIAL_COLOR = TextColor.color(134, 214, 223);

    Component FADE_BLACK = Component.text("o", NamedTextColor.BLACK).font(Key.key("minecraft", "semicolon"));
    Component WIKI_LINK = Component.text("[위키 링크]", TextColor.color(250, 130, 55))
            .clickEvent(ClickEvent.openUrl("https://www.notion.so/26780abaaada806a985fc931ef1b715d?v=26780abaaada8045a503000cef475764&source"))
            .hoverEvent(HoverEvent.showText(Component.text("Primary Colony 프로젝트에 대하여...", TextColor.color(228, 233, 234))));

    Component TUTORIAL_HOME_MODULE = Component.text("[홈 모듈]", TUTORIAL_COLOR);
    Component TUTORIAL_MINE = Component.text("[광산/광물]", TUTORIAL_COLOR);
    Component TUTORIAL_FARM = Component.text("[작물/농사]", TUTORIAL_COLOR);
    Component TUTORIAL_MACHINE = Component.text("[설치형 모듈]", TUTORIAL_COLOR);
    Component TUTORIAL_EXPLORE = Component.text("[탐사]", TUTORIAL_COLOR);
    Component TUTORIAL_MODULE = Component.text("[빌드형 모듈]", TUTORIAL_COLOR);
}
