package studio.semicolon.prc.core.util;

import com.google.common.collect.Maps;
import io.quill.paper.util.bukkit.Logger;
import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import java.util.Map;

public class Advancements {
    private static final String ADVANCEMENT_NAMESPACE = "pcadv";
    private static final Map<String, Advancement> CACHE = Maps.newHashMap();

    public static void grant(Player player, String advancementID) {
        Advancement advancement = getOrCache(advancementID);

        if (advancement == null) {
            Logger.warn("Advancement not found: " + ADVANCEMENT_NAMESPACE + ":" + advancementID);
            return;
        }

        AdvancementProgress progress = player.getAdvancementProgress(advancement);

        if (progress.isDone()) return;

        progress.getRemainingCriteria().forEach(progress::awardCriteria);
    }

    private static Advancement getOrCache(String advancementID) {
        return CACHE.computeIfAbsent(advancementID, ID -> Bukkit.getAdvancement(PDCKeys.of(ADVANCEMENT_NAMESPACE, ID)));
    }

    private Advancements() { }
}