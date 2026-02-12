package studio.semicolon.prc.core.constant.text;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextColor;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public interface ModuleMessages {
    Component SESSION_USING = Component.text("누군가 사용 중입니다. 나중에 다시 시도해주세요.", NamedTextColor.RED);
    Component ALREADY_OCCUPIED = Component.text("이미 다른 모듈이 존재합니다!", NamedTextColor.RED);
    Component INSUFFICIENT_MODULE_ITEM = Component.text("해당 모듈을 소지하고 있지 않습니다.", NamedTextColor.RED);
    Component NOT_FOUND_STRUCTURE = Component.text("구조물을 찾을 수 없습니다.", NamedTextColor.RED);
    Component WRONG_DIRECTION = Component.text("연결할 수 없는 방향입니다!", NamedTextColor.RED);

    Component CONFIRM_TITLE = Component.text("설치 후 되돌릴 수 없습니다.", NamedTextColor.RED);
    Component CONFIRM_SUB_TITLE = Component.text("진행하려면 한번 더 입력하세요.", NamedTextColor.YELLOW);


    // HUD
    Component HUD_MOVE = Component.text("A", TextColor.color(254, 254, 254)).font(Key.key("minecraft", "semicolon"));
    Component HUD_BUILD = Component.text("B", TextColor.color(254, 254, 253)).font(Key.key("minecraft", "semicolon"));
    Component HUD_CANCEL = Component.text("C", TextColor.color(254, 253, 254)).font(Key.key("minecraft", "semicolon"));
    Component HUD_ROTATE = Component.text("D", TextColor.color(253, 254, 254)).font(Key.key("minecraft", "semicolon"));
    Component HUD_MARGIN = Component.text("   ");
    Component HUD_WARNING = Component.text("E", TextColor.color(253, 253, 254)).font(Key.key("minecraft", "semicolon"));
    Component HUD_NEGATIVE_SPACE = Component.text("\u341F");

    Component GUIDE_HUD = Component.text()
            .append(HUD_MOVE, HUD_MARGIN, HUD_BUILD, HUD_MARGIN, HUD_CANCEL, HUD_MARGIN, HUD_ROTATE)
            .shadowColor(ShadowColor.shadowColor(0x00FFFFFF))
            .build();

    Component GUIDE_HUD_WITH_WARNING = Component.text()
            .append(GUIDE_HUD)
            .append(HUD_NEGATIVE_SPACE, HUD_WARNING)
            .build();
}