package studio.semicolon.prc.core.event.listener.game;

import net.minecraft.world.entity.decoration.ItemFrame;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class VanillaDisableListener implements Listener {

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getPlayer().getGameMode().isInvulnerable()) return;

        Entity entity = event.getRightClicked();
        if (entity instanceof ItemFrame) {
            event.setCancelled(true);
        }
    }
}
