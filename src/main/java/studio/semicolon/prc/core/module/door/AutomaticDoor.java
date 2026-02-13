package studio.semicolon.prc.core.module.door;

import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public record AutomaticDoor(ItemDisplay display, Location location, Chunk chunk) {
    private static final NamespacedKey MODULE_DOOR = PDCKeys.of("module_door");
    private static final NamespacedKey IS_ELEVATED = PDCKeys.of("is_elevated");

    public AutomaticDoor(ItemDisplay display) {
        this(display, display.getLocation(), display.getLocation().getChunk());
    }

    /**
     * 문이 현재 열려있는지 확인
     */
    public boolean isElevated() {
        if (!display.isValid()) return false;

        PersistentDataContainer pdc = display.getPersistentDataContainer();
        return Boolean.TRUE.equals(pdc.get(IS_ELEVATED, PersistentDataType.BOOLEAN));
    }

    /**
     * 문 열림 상태 설정
     */
    public void setElevated(boolean elevated) {
        if (!display.isValid()) return;

        PersistentDataContainer pdc = display.getPersistentDataContainer();
        pdc.set(IS_ELEVATED, PersistentDataType.BOOLEAN, elevated);
    }

    /**
     * 유효한 자동문인지 검증
     */
    public boolean isValid() {
        if (!display.isValid()) return false;

        PersistentDataContainer pdc = display.getPersistentDataContainer();
        return Boolean.TRUE.equals(pdc.get(MODULE_DOOR, PersistentDataType.BOOLEAN));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AutomaticDoor other)) return false;
        return display.equals(other.display);
    }

    @Override
    public int hashCode() {
        return display.hashCode();
    }
}