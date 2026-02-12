package studio.semicolon.prc.core.event.listener.game;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import io.quill.paper.event.EventContext;
import io.quill.paper.event.EventResult;
import io.quill.paper.event.EventSubscriber;
import io.quill.paper.item.require.ArmorMaterial;
import io.quill.paper.item.require.InventoryRequirement;
import io.quill.paper.item.require.InventoryRequirements;
import io.quill.paper.player.PlayerContext;
import io.quill.paper.player.PlayerContexts;
import io.quill.paper.util.bukkit.Locations;
import io.quill.paper.util.bukkit.Potions;
import io.quill.paper.util.bukkit.Ticks;
import io.quill.paper.util.bukkit.task.Tasks;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import studio.semicolon.prc.core.constant.text.GameMessages;

import java.util.Optional;

public class CrystalCaveListener implements EventSubscriber<PlayerAdvancementCriterionGrantEvent, CrystalCaveListener.Context> {
    private static final String ADVANCEMENT = "pcadv:planet/normal/kirakira";
    private static final String RETURN_FLAG = "crystal_cave_teleporting";

    private static final double RETURN_X = 798;
    private static final double RETURN_Y = -54;
    private static final double RETURN_Z = 929;

    private static final InventoryRequirement DIAMOND_ARMOR = InventoryRequirements.fullArmorSet(ArmorMaterial.DIAMOND);
    private static final int DEBUFF_DURATION = Ticks.sec2Ticks(2.0);

    public sealed interface Context extends EventContext permits Context.Allow, Context.Deny {
        record Allow(Player player) implements Context, Data { }

        sealed interface Deny extends Context, Error permits Deny.Forbidden {
            record Forbidden() implements Deny {
                @Override public Component text() { return GameMessages.CRYSTAL_CAVE_FORBIDDEN; }
            }
        }
    }

    @Override
    public Optional<Context> expect(PlayerAdvancementCriterionGrantEvent e) {
        if (!e.getAdvancement().getKey().toString().equals(ADVANCEMENT)) return Optional.empty();

        Player player = e.getPlayer();

        if (!DIAMOND_ARMOR.test(player.getInventory())) {
            return Optional.of(new Context.Deny.Forbidden());
        }

        return Optional.of(new Context.Allow(player));
    }

    @Override
    public EventResult onEvent(PlayerAdvancementCriterionGrantEvent e, Context ctx) {
        return EventResult.STOP;
    }

    @Override
    public EventResult onError(PlayerAdvancementCriterionGrantEvent e, EventContext.Error error) {
        Player player = e.getPlayer();

        player.damage(1.0);
        Potions.of(PotionEffectType.SLOWNESS)
                .duration(DEBUFF_DURATION)
                .level(1)
                .ambient(false)
                .particles(false)
                .icon(false)
                .applyTo(player);
        Potions.of(PotionEffectType.DARKNESS)
                .duration(DEBUFF_DURATION)
                .level(1)
                .ambient(false)
                .particles(false)
                .icon(false)
                .applyTo(player);


        PlayerContext ctx = PlayerContexts.ctx(player);
        if (ctx.flag(RETURN_FLAG)) return EventResult.TERMINATE;
        ctx.flag(RETURN_FLAG, true);

        Tasks.later(Ticks.sec2Ticks(3), () -> {
            Location returnLoc = Locations.of(player.getWorld(), RETURN_X, RETURN_Y, RETURN_Z);
            player.teleport(returnLoc);
            player.sendMessage(error.text());
            PlayerContexts.ctx(player).removeFlag(RETURN_FLAG);
        });

        return EventResult.TERMINATE;
    }
}