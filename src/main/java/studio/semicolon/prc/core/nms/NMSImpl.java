package studio.semicolon.prc.core.nms;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import studio.semicolon.prc.api.nms.NMS;

import java.util.Collection;
import java.util.UUID;

public final class NMSImpl implements NMS {

    private static final String HIDE_NAMETAG_TEAM = "";
    private static final PlayerTeam NAMETAG_TEAM;

    private final ObjectOpenHashSet<UUID> initializedPlayers = new ObjectOpenHashSet<>();

    static {
        NAMETAG_TEAM = new PlayerTeam(
                MinecraftServer.getServer().getScoreboard(),
                HIDE_NAMETAG_TEAM
        );
        NAMETAG_TEAM.setNameTagVisibility(Team.Visibility.NEVER);
        NAMETAG_TEAM.setColor(ChatFormatting.RESET);
    }

    @Override
    public void hideNametag(Player receiver, Collection<String> targets) {
        var connection = ((CraftPlayer) receiver).getHandle().connection;

        if (initializedPlayers.add(receiver.getUniqueId())) {
            connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(NAMETAG_TEAM, true));
        }

        targets.forEach(name -> connection.send(ClientboundSetPlayerTeamPacket.createPlayerPacket(
                NAMETAG_TEAM,
                name,
                ClientboundSetPlayerTeamPacket.Action.ADD
        )));
    }

    @Override
    public void showNametag(Player receiver, Collection<String> targets) {
        var connection = ((CraftPlayer) receiver).getHandle().connection;

        targets.forEach(name -> connection.send(ClientboundSetPlayerTeamPacket.createPlayerPacket(
                NAMETAG_TEAM,
                name,
                ClientboundSetPlayerTeamPacket.Action.REMOVE
        )));
    }

    @Override
    public void invalidateNametag(Player player) {
        initializedPlayers.remove(player.getUniqueId());
    }
}
