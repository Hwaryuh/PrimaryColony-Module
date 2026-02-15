package studio.semicolon.prc.api.constant.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public interface GameMessages {
    Component CRYSTAL_CAVE_FORBIDDEN = Component.text("날아다니는 분진으로 인해 호흡이 힘들어 더 이상 들어갈 수가 없다, 지금은 무리야...", NamedTextColor.GRAY);
    Component COLD_OUTSIDE = Component.text("얼어붙고 있는 것 같아..");
    Component BURIED_CHEST_NEED_TOOL = Component.text("너무 단단하게 상자가 묻혀있다, 열기 위해서는 상위 도구로 깨내야할 것 같다.", NamedTextColor.GRAY);
    Component BURIED_CHEST_FOUND_ITEM = Component.text("상자 속에서 무언가를 찾았다.", NamedTextColor.GRAY);

    static Component getBackPackSearchingMessage(int dotCount) {
        return Component.text("가방을 수색하는 중" + ".".repeat(dotCount), NamedTextColor.GRAY);
    }
}