package studio.semicolon.prc.api.constant.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public interface ServerLinkConstants {
    Component TITLE_1 = Component.text("홈페이지");
    Component TITLE_2 = Component.text("패트리온");
    Component TITLE_3 = Component.text("유튜브");
    Component TITLE_4 = Component.text("팀장 출몰 지역", TextColor.color(245, 155, 100));
    Component TITLE_5 = Component.text("이난오 유튜브", TextColor.color(207, 181, 255));
    Component TITLE_6 = Component.text("마구니의 마크공방", TextColor.color(148, 120, 197));

    String URL_1 = "https://semicolonstudio.kro.kr/#home";
    String URL_2 = "https://www.patreon.com/c/SemicolonStudio_";
    String URL_3 = "https://www.youtube.com/@semicolonstudio_0nline";
    String URL_4 = "https://soundcloud.com/replonline";
    String URL_5 = "https://www.youtube.com/@leenanoh";
    String URL_6 = "https://pokmagoon.gumroad.com";
}
