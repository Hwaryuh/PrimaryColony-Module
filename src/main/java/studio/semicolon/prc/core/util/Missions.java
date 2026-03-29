package studio.semicolon.prc.core.util;

import kr.eme.prcMission.api.events.MissionEvent;
import kr.eme.prcMission.enums.MissionVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Missions {
    public static void progressV1(Player player, String target, String type, int value) {
        Bukkit.getPluginManager().callEvent(new MissionEvent(player, MissionVersion.V1, target, type, value));
    }

    public static void progressV2(Player player, String target, String type, int value) {
        Bukkit.getPluginManager().callEvent(new MissionEvent(player, MissionVersion.V2, target, type, value));
    }
}
