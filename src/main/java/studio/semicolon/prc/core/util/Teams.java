package studio.semicolon.prc.core.util;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.function.Consumer;

public final class Teams {
    private Teams() { }

    public static Team getOrCreate(Player player, String teamName) {
        return getOrCreate(player, teamName, team -> {});
    }

    public static Team getOrCreate(Player player, String teamName, Consumer<Team> initializer) {
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            initializer.accept(team);
        }
        return team;
    }
}
