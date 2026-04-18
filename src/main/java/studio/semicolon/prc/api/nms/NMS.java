package studio.semicolon.prc.api.nms;

import org.bukkit.entity.Player;
import java.util.Collection;

public interface NMS {

    void hideNametag(Player receiver, Collection<String> targets);

    void showNametag(Player receiver, Collection<String> targets);

    void invalidateNametag(Player player);
}
