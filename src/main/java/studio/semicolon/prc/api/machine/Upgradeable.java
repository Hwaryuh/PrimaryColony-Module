package studio.semicolon.prc.api.machine;

public interface Upgradeable {
    String KEY_UPGRADE_LEVEL = "upgrade_level";

    int getUpgradeLevel();

    void setUpgradeLevel(int level);
}