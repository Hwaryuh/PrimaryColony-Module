package studio.semicolon.prc.core.util;

import kr.eme.prcMission.api.events.MissionEvent;
import kr.eme.prcMission.enums.MissionVersion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import studio.semicolon.prc.Module;

public class Missions {
    private static final PluginManager PLUGIN_MANAGER = Module.getInstance().getServer().getPluginManager();
    private static final Plugin MISSION_PLUGIN = PLUGIN_MANAGER.getPlugin("PRCMission");

    public static void progressV1(Player player, String target, String type, int value) {
        if (MISSION_PLUGIN != null && MISSION_PLUGIN.isEnabled()) {
            PLUGIN_MANAGER.callEvent(new MissionEvent(player, MissionVersion.V1, target, type, value));
        }
    }

    public static void progressV2(Player player, String target, String type, int value) {
        if (MISSION_PLUGIN != null && MISSION_PLUGIN.isEnabled()) {
            PLUGIN_MANAGER.callEvent(new MissionEvent(player, MissionVersion.V2, target, type, value));
        }
    }
}