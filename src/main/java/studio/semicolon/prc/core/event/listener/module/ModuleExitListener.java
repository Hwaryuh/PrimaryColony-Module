package studio.semicolon.prc.core.event.listener.module;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import studio.semicolon.prc.core.module.build.BuildSessionManager;
import studio.semicolon.prc.core.module.build.BuildSession;

public class ModuleExitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSneak(PlayerToggleSneakEvent e) {
        BuildSession session = BuildSessionManager.getInstance().getSession(e.getPlayer());
        if (session != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerStopSpectate(PlayerStopSpectatingEntityEvent e) {
        BuildSession session = BuildSessionManager.getInstance().getSession(e.getPlayer());
        if (session != null) {
            e.setCancelled(true);
        }
    }
}