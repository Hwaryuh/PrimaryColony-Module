package studio.semicolon.prc.core.util;

import kr.eme.prcMission.api.events.MissionEvent;
import kr.eme.prcMission.enums.MissionVersion;
import org.bukkit.entity.Player;
import studio.semicolon.prc.Module;

public class Missions {
    public static void progressMissionV1(Player player, String target, String type, int value) {
        Module.getInstance().getServer().getPluginManager().callEvent(new MissionEvent(player, MissionVersion.V1, target, type, value));
    }

    public static void progressMissionV2(Player player, String target, String type, int value) {
        Module.getInstance().getServer().getPluginManager().callEvent(new MissionEvent(player, MissionVersion.V2, target, type, value));
    }
}