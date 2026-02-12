package studio.semicolon.prc.core.constant.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public interface MachineMessages {
    Component PLACE_INVALID = Component.text("이곳에는 설치할 수 없습니다!", NamedTextColor.RED);
    Component PLACE_EXISTS = Component.text("이미 모듈이 존재합니다.", NamedTextColor.RED);
    Component PLACE_NOT_ON_STORAGE = Component.text("지정된 구역에만 설치할 수 있습니다. (스토리지 모듈)", NamedTextColor.YELLOW);

    Component INTERACT_LOCKED = Component.text("다른 플레이어가 사용 중입니다.", NamedTextColor.RED);

    Component DESTROY_PROCESSING = Component.text("모듈이 작동 중입니다.", NamedTextColor.RED);
    Component DESTROY_EXIST_RESULT = Component.text("모듈에 결과물이 있습니다. 결과물을 회수한 후 제거해주세요.", NamedTextColor.RED);
    Component DESTROY_LOCKED = Component.text("다른 플레이어가 사용 중인 모듈은 제거할 수 없습니다.", NamedTextColor.RED);

    Component COFFEE_INSUFFICIENT_INGREDIENTS = Component.text("재료가 부족합니다. 머그잔과 커피콩이 필요합니다.", NamedTextColor.RED);
    Component COFFEE_EXIST_RESULT = Component.text("결과물을 회수한 후 진행해주세요.", NamedTextColor.YELLOW);

    Component PRINT_MACHINE_INVALID_INGREDIENTS = Component.text("재료가 부족합니다.", NamedTextColor.RED);

    Component FURNACE_INSUFFICIENT_FUEL = Component.text("연료 아이템이 필요합니다.", NamedTextColor.RED);
    Component FURNACE_INSUFFICIENT_INGREDIENTS = Component.text("올바르지 않은 재료입니다.", NamedTextColor.RED);
    Component FURNACE_INSUFFICIENT_RECIPE = Component.text("레시피 아이템이 필요합니다.", NamedTextColor.RED);

    Component GRINDER_INSUFFICIENT_ORE = Component.text("광석 아이템이 필요합니다.", NamedTextColor.RED);
    Component GRINDER_INVALID_ORE = Component.text("알 수 없는 광석입니다.", NamedTextColor.RED);
    Component GRINDER_CANNOT_PROCESS = Component.text("해당 광석을 분쇄하려면 Al-Cu 합금 기어 설치가 필요합니다.", NamedTextColor.RED);
}