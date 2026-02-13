package studio.semicolon.prc.api.constant.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public interface MenuTitles {
    Component COFFEE_MACHINE_IDLE = Component.text("\u340F\u3473", NamedTextColor.WHITE);
    Component COFFEE_MACHINE_PROCESSING = Component.text("\u340F\u3474", NamedTextColor.WHITE);

    Component FURNACE_MACHINE_IDLE = Component.text("\u340F\u3475", NamedTextColor.WHITE);
    Component FURNACE_MACHINE_PROCESSING = Component.text("\u340F\u3476", NamedTextColor.WHITE);

    Component GRINDER_MACHINE_IDLE = Component.text("\u340F\u3471", NamedTextColor.WHITE);
    Component GRINDER_MACHINE_PROCESSING = Component.text("\u340F\u3472", NamedTextColor.WHITE);

    Component PRINT_MACHINE_SELECTION = Component.text("\u340F\u3478", NamedTextColor.WHITE);
    Component PRINT_MACHINE_CONFIRM = Component.text("\u340F\u3479", NamedTextColor.WHITE);
    Component PRINT_MACHINE_PROCESSING = Component.text("\u340F\u3480", NamedTextColor.WHITE);

    Component BUILD_PANEL = Component.text("\u340F\u3441", NamedTextColor.WHITE);
}