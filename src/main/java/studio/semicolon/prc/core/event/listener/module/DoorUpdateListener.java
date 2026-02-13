package studio.semicolon.prc.core.event.listener.module;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import studio.semicolon.prc.core.module.door.AutomaticDoorService;

public class DoorUpdateListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!e.hasExplicitlyChangedBlock()) return;
        Player player = e.getPlayer();

        if (!player.isOnGround()) return;
        if (player.isFlying()) return;
        if (player.isGliding()) return;
        if (player.isSwimming()) return;

        AutomaticDoorService.getInstance().updateDoors(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        AutomaticDoorService.getInstance().updateDoors(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        AutomaticDoorService.getInstance().updateDoors(e.getPlayer());
    }
}