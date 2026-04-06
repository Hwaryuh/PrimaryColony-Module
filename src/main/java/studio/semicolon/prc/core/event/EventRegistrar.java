package studio.semicolon.prc.core.event;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import io.quill.paper.Bootable;
import io.quill.paper.event.EventManager;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import studio.semicolon.prc.core.event.listener.advancement.GameModeChangeListener;
import studio.semicolon.prc.core.event.listener.advancement.PlantTouchListener;
import studio.semicolon.prc.core.event.listener.advancement.PlayerFallListener;
import studio.semicolon.prc.core.event.listener.game.AdvancementDoneListener;
import studio.semicolon.prc.core.event.listener.game.BackPackListener;
import studio.semicolon.prc.core.event.listener.game.BuriedChestListener;
import studio.semicolon.prc.core.event.listener.game.DocumentListener;
import studio.semicolon.prc.core.event.listener.game.PlayerListener;
import studio.semicolon.prc.core.event.listener.game.RoPAILeftClickListener;
import studio.semicolon.prc.core.event.listener.game.RoPAIRightClickListener;
import studio.semicolon.prc.core.event.listener.machine.CoffeeListener;
import studio.semicolon.prc.core.event.listener.module.EntryModuleListener;
import studio.semicolon.prc.core.event.listener.machine.MachineDestroyListener;
import studio.semicolon.prc.core.event.listener.machine.MachineInteractListener;
import studio.semicolon.prc.core.event.listener.machine.MachinePlaceListener;
import studio.semicolon.prc.core.event.listener.module.DoorUpdateListener;
import studio.semicolon.prc.core.event.listener.module.ModuleMarkerListener;
import studio.semicolon.prc.core.event.listener.module.ModulePanelListener;
import studio.semicolon.prc.core.event.listener.game.CrystalCaveListener;

public class EventRegistrar implements Bootable {
    private final PlayerListener playerListener = new PlayerListener();
    private final DoorUpdateListener doorUpdateListener = new DoorUpdateListener();

    @Override
    public void start(JavaPlugin plugin) {
        var manager = EventManager.getInstance();

        manager.register(PlayerInteractEvent.class)
                .subscribe(new MachineDestroyListener())
                .subscribe(new MachineInteractListener())
                .subscribe(new MachinePlaceListener())
                .subscribe(new PlantTouchListener())
                .subscribe(new ModuleMarkerListener())
                .build();
        manager.register(PlayerItemConsumeEvent.class)
                .subscribe(new CoffeeListener())
                .build();
        manager.register(PlayerAdvancementCriterionGrantEvent.class)
                .subscribe(new CrystalCaveListener())
                .build();
        manager.register(PlayerInteractEntityEvent.class)
                .subscribe(new RoPAIRightClickListener())
                .subscribe(new ModulePanelListener())
                .subscribe(new BackPackListener())
                .subscribe(new DocumentListener())
                .subscribe(new EntryModuleListener())
                .subscribe(new BuriedChestListener())
                .build();
        manager.register(EntityDamageByEntityEvent.class)
                .subscribe(new RoPAILeftClickListener())
                .build();
        manager.register(EntityDamageEvent.class)
                .subscribe(new PlayerFallListener())
                .build();
        manager.register(PlayerGameModeChangeEvent.class)
                .subscribe(new GameModeChangeListener())
                .build();
        manager.register(PlayerAdvancementDoneEvent.class)
                .subscribe(new AdvancementDoneListener())
                .build();

        plugin.getServer().getPluginManager().registerEvents(playerListener, plugin);
        plugin.getServer().getPluginManager().registerEvents(doorUpdateListener, plugin);
    }

    @Override
    public void end(JavaPlugin plugin) {
        HandlerList.unregisterAll(playerListener);
        HandlerList.unregisterAll(doorUpdateListener);
    }
}
