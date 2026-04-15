package studio.semicolon.prc.api.machine;

import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataType;

public abstract class UpgradeableMachine extends AbstractMachine {
    private static final String KEY_UPGRADE_LEVEL = "upgrade_level";

    protected UpgradeableMachine(Location location, String machineType) {
        super(location, machineType);
    }

    public int getUpgradeLevel() {
        return getPDC().getOrDefault(PDCKeys.of(KEY_UPGRADE_LEVEL), PersistentDataType.INTEGER, 0);
    }

    public void setUpgradeLevel(int level) {
        var pdc = getPDC();
        pdc.set(PDCKeys.of(KEY_UPGRADE_LEVEL), PersistentDataType.INTEGER, level);
        savePDC(pdc);
    }
}
