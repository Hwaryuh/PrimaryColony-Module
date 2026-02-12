package studio.semicolon.prc.core.event;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import io.quill.paper.Bootable;
import io.quill.paper.event.EventManager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import studio.semicolon.prc.core.event.listener.game.BackPackListener;
import studio.semicolon.prc.core.event.listener.game.RoPAILeftClickListener;
import studio.semicolon.prc.core.event.listener.game.RoPAIRightClickListener;
import studio.semicolon.prc.core.event.listener.machine.CoffeeListener;
import studio.semicolon.prc.core.event.listener.machine.MachineDestroyListener;
import studio.semicolon.prc.core.event.listener.machine.MachineInteractListener;
import studio.semicolon.prc.core.event.listener.machine.MachinePlaceListener;
import studio.semicolon.prc.core.event.listener.module.ModulePanelListener;
import studio.semicolon.prc.core.event.listener.game.CrystalCaveListener;

public class EventRegistrar implements Bootable {
    @Override
    public void start(JavaPlugin plugin) {
        var manger = EventManager.getInstance();

        manger.register(PlayerInteractEvent.class)
                .subscribe(new MachineDestroyListener())
                .subscribe(new MachineInteractListener())
                .subscribe(new MachinePlaceListener())
                .build();
        manger.register(PlayerItemConsumeEvent.class)
                .subscribe(new CoffeeListener())
                .build();
        manger.register(PlayerAdvancementCriterionGrantEvent.class)
                .subscribe(new CrystalCaveListener())
                .build();
        manger.register(PlayerInteractEntityEvent.class)
                .subscribe(new RoPAIRightClickListener())
                .subscribe(new ModulePanelListener())
                .subscribe(new BackPackListener())
                .build();
        manger.register(EntityDamageByEntityEvent.class)
                .subscribe(new RoPAILeftClickListener())
                .build();
    }

    @Override
    public void end(JavaPlugin plugin) { }
}