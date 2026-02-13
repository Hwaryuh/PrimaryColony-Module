package studio.semicolon.prc.core.module.build;

import com.google.common.collect.Maps;
import io.quill.paper.Bootable;
import io.quill.paper.event.EventManager;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import studio.semicolon.prc.Module;
import studio.semicolon.prc.api.module.Yaws;
import studio.semicolon.prc.core.module.ModuleMetadata;
import studio.semicolon.prc.core.module.validate.BuildValidationResult;
import studio.semicolon.prc.api.module.indicator.IndicatorMovement;
import studio.semicolon.prc.api.module.indicator.IndicatorRenderer;
import studio.semicolon.prc.api.module.indicator.IndicatorState;
import studio.semicolon.prc.api.module.RelativeDirection;
import studio.semicolon.prc.api.module.Structures;
import studio.semicolon.prc.core.constant.item.module.ModuleItems;
import studio.semicolon.prc.core.constant.text.ModuleMessages;
import studio.semicolon.prc.core.event.listener.module.ModuleInputListener;
import studio.semicolon.prc.core.event.listener.module.ModuleExitListener;
import studio.semicolon.prc.core.module.ModuleType;
import studio.semicolon.prc.core.module.validate.BuildValidator;
import studio.semicolon.prc.core.util.Titles;

import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class BuildSessionManager implements Bootable {
    private static final BuildSessionManager INSTANCE = new BuildSessionManager();

    private final ModuleInputListener inputListener = new ModuleInputListener();
    private final ModuleExitListener exitListener = new ModuleExitListener();

    @Nullable
    private BuildSession currentSession = null;
    private boolean listenersRegistered = false;

    private static final int INDICATOR_HEIGHT_OFFSET = 2;

    private BuildSessionManager() { }

    public static BuildSessionManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void start(JavaPlugin javaPlugin) { }

    @Override
    public void end(JavaPlugin javaPlugin) {
        clearAll();
    }

    @Nullable
    public synchronized BuildSession getSession(Player player) {
        if (currentSession == null) return null;
        return currentSession.getPlayer().equals(player) ? currentSession : null;
    }

    public boolean isUsing() {
        return currentSession != null;
    }

    @Nullable
    public synchronized BuildSession enter(Player player, ModuleType moduleType, double panelInteractionY) {
        if (isUsing()) return null;

        Location originalLocation = player.getLocation().clone();
        GameMode originalGameMode = player.getGameMode();
        ItemStack originalHelmet = player.getInventory().getHelmet();

        float cameraYaw = Yaws.snapToCardinal(player.getLocation().getYaw());

        Chunk cameraChunk = player.getChunk();
        Location cameraLocation = Structures.getCenterLocation(cameraChunk, panelInteractionY + moduleType.getCameraOffset());
        cameraLocation.setYaw(cameraYaw);
        ItemDisplay cameraEntity = spawnCamera(player, cameraLocation);

        Chunk initialChunk = getForwardChunk(cameraChunk, cameraYaw);
        IndicatorState initialState = new IndicatorState(initialChunk, cameraYaw + 180.0F, moduleType);

        double indicatorHeight = panelInteractionY + INDICATOR_HEIGHT_OFFSET;
        ItemDisplay indicatorDisplay = spawnIndicator(player, indicatorHeight);

        BossBar guideHUD = ofGuideHUD();
        guideHUD.addViewer(player);

        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(cameraEntity);
        player.getInventory().setHelmet(ModuleItems.BUILD_MODE_OVERLAY);

        Map<ModuleMetadata.Direction, Boolean> cameraConnections = Maps.newEnumMap(ModuleMetadata.Direction.class);
        for (ModuleMetadata.Direction dir : ModuleMetadata.Direction.values()) {
            cameraConnections.put(dir, ModuleMetadata.canConnectToDirection(cameraChunk, dir));
        }

        boolean isOccupied = BuildValidator.isOccupied(initialState);
        boolean canConnect = BuildValidator.canConnect(cameraChunk, initialState, cameraConnections);

        BuildValidationResult initialValidation = new BuildValidationResult(isOccupied, canConnect);

        IndicatorRenderer.render(
                indicatorDisplay,
                initialState,
                indicatorHeight,
                initialValidation.toRenderState()
        );

        BuildSession session = new BuildSession(
                player,
                originalLocation,
                originalGameMode,
                originalHelmet,
                cameraEntity,
                indicatorDisplay,
                guideHUD,
                indicatorHeight,
                initialState,
                initialValidation,
                cameraConnections
        );

        currentSession = session;
        registerListeners();

        return session;
    }

    public synchronized void beginExit(Player player) {
        if (currentSession == null) return;
        if (!currentSession.getPlayer().equals(player)) return;

        unregisterListeners();

        Titles.fade(player, () -> exit(player));
    }

    public synchronized void exit(Player player) {
        if (currentSession == null) return;

        player.setSpectatorTarget(null);
        player.teleport(currentSession.getOriginalLocation());
        player.setGameMode(currentSession.getOriginalGameMode());
        player.getInventory().setHelmet(currentSession.getOriginalHelmet());

        currentSession.clearSession();
        currentSession = null;
    }

    public synchronized void clearAll() {
        if (currentSession == null) return;

        Player player = currentSession.getPlayer();
        if (player.isOnline()) {
            player.setSpectatorTarget(null);
            player.teleport(currentSession.getOriginalLocation());
            player.setGameMode(currentSession.getOriginalGameMode());
            player.getInventory().setHelmet(currentSession.getOriginalHelmet());
        }
        currentSession.clearSession();
        currentSession = null;
        unregisterListeners();
    }

    private void registerListeners() {
        if (listenersRegistered) return;

        EventManager.getInstance().capture(PlayerInputEvent.class, inputListener);
        Module.getInstance().getServer().getPluginManager().registerEvents(exitListener, Module.getInstance());

        listenersRegistered = true;
    }

    private void unregisterListeners() {
        if (!listenersRegistered) return;

        EventManager.getInstance().release(PlayerInputEvent.class, inputListener);
        HandlerList.unregisterAll(exitListener);

        listenersRegistered = false;
    }

    private ItemDisplay spawnCamera(Player player, Location location) {
        location.setPitch(90.0F);
        return player.getWorld().spawn(location, ItemDisplay.class, id -> {
            id.setItemStack(ItemStack.of(Material.AIR));
        });
    }

    private ItemDisplay spawnIndicator(Player player, double height) {
        Location location = player.getLocation();
        location.setY(height);

        return player.getWorld().spawn(location, ItemDisplay.class);
    }

    private Chunk getForwardChunk(Chunk cameraChunk, float cameraYaw) {
        return IndicatorMovement.getTargetChunk(cameraChunk, RelativeDirection.FORWARD, cameraYaw);
    }

    private BossBar ofGuideHUD() {
        return BossBar.bossBar(ModuleMessages.GUIDE_HUD, 1.0f, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS);
    }
}