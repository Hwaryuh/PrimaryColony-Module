package studio.semicolon.prc.core.module.build;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import studio.semicolon.prc.api.module.indicator.IndicatorState;
import studio.semicolon.prc.core.module.ModuleMetadata;
import studio.semicolon.prc.core.module.validate.BuildValidationResult;

import java.util.Map;

public class BuildSession {
    private final Player player;
    private final Location originalLocation;
    private final GameMode originalGameMode;
    @Nullable
    private final ItemStack originalHelmet;
    private final Entity cameraEntity;
    private final ItemDisplay indicatorDisplay;
    private final BossBar hud;
    private final Component originalHudTexts;
    private final double indicatorHeight;
    private final Map<ModuleMetadata.Direction, Boolean> cameraChunkConnections;

    private IndicatorState indicatorState;
    private BuildValidationResult validationResult;
    private long lastTryTick = 0;
    private boolean confirmRequested = false;

    private static final double STRUCTURE_Y_OFFSET = -8.0;

    public BuildSession(
            Player player,
            Location originalLocation,
            GameMode originalGameMode,
            ItemStack originalHelmet,
            Entity cameraEntity,
            ItemDisplay indicatorDisplay,
            BossBar hud,
            double indicatorHeight,
            IndicatorState initialState,
            BuildValidationResult initialValidation,
            Map<ModuleMetadata.Direction, Boolean> cameraChunkConnections
    ) {
        this.player = player;
        this.originalLocation = originalLocation;
        this.originalGameMode = originalGameMode;
        this.originalHelmet = originalHelmet;
        this.cameraEntity = cameraEntity;
        this.indicatorDisplay = indicatorDisplay;
        this.hud = hud;
        this.originalHudTexts = hud.name();
        this.indicatorHeight = indicatorHeight;
        this.indicatorState = initialState;
        this.validationResult = initialValidation;
        this.cameraChunkConnections = cameraChunkConnections;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getOriginalLocation() {
        return originalLocation;
    }

    public GameMode getOriginalGameMode() {
        return originalGameMode;
    }

    public ItemStack getOriginalHelmet() {
        return originalHelmet;
    }

    public Entity getCameraEntity() {
        return cameraEntity;
    }

    public ItemDisplay getIndicatorDisplay() {
        return indicatorDisplay;
    }

    public IndicatorState getIndicatorState() {
        return indicatorState;
    }

    public double getIndicatorHeight() {
        return indicatorHeight;
    }

    public double getStructureCoordinateY() {
        return indicatorHeight + STRUCTURE_Y_OFFSET; // 결국 패널 인터랙션의 y 좌표 기준 -6
    }

    public Chunk getCameraChunk() {
        return cameraEntity.getLocation().getChunk();
    }

    public BuildValidationResult getValidationResult() {
        return validationResult;
    }

    public Map<ModuleMetadata.Direction, Boolean> getCameraChunkConnections() {
        return cameraChunkConnections;
    }

    public boolean tryConfirm(int thresholdTicks) {
        long currentTick = player.getWorld().getGameTime();
        long ticksSinceLastInput = currentTick - lastTryTick;

        lastTryTick = currentTick;

        if (!confirmRequested) {
            confirmRequested = true;
            return false;
        }

        if (ticksSinceLastInput > 0 && ticksSinceLastInput <= thresholdTicks) {
            confirmRequested = false;
            return true;
        }

        confirmRequested = false;
        return false;
    }

    public void updateHUD(Component message) {
        hud.name(message);
    }

    public void updateState(IndicatorState newState, BuildValidationResult newValidation) {
        this.indicatorState = newState;
        this.validationResult = newValidation;
    }

    public void resetHUD() {
        hud.name(originalHudTexts);
    }

    public void clearSession() {
        cameraEntity.remove();
        indicatorDisplay.remove();
        hud.removeViewer(player);
    }
}