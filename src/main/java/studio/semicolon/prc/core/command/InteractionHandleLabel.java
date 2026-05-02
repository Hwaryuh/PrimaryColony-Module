package studio.semicolon.prc.core.command;

import io.quill.paper.command.ArgumentKey;
import io.quill.paper.command.CommandResult;
import io.quill.paper.command.argument.ArgType;
import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

public class InteractionHandleLabel {
    private static final ArgumentKey<String> AXIS = ArgumentKey.stringKey("axis");
    private static final ArgumentKey<Double> AMOUNT = ArgumentKey.doubleKey("amount");
    private static final ArgumentKey<Double> WIDTH = ArgumentKey.doubleKey("width");
    private static final ArgumentKey<Double> HEIGHT = ArgumentKey.doubleKey("height");

    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("handleInteraction")
                .child(c -> c
                        .name("move").playerOnly()
                        .argument(AXIS, ArgType.string()).and()
                        .argument(AMOUNT, ArgType.doubleValue()).and()
                        .runPlayer(ctx -> {
                            Player player = ctx.sender().player();
                            Interaction interaction = findNearest(player);
                            if (interaction == null) return CommandResult.failure("근처에 Interaction 엔티티가 없습니다.");

                            String axis = ctx.arg(AXIS).toLowerCase();
                            double amount = ctx.arg(AMOUNT);

                            Location loc = interaction.getLocation();
                            switch (axis) {
                                case "x" -> loc.add(amount, 0, 0);
                                case "y" -> loc.add(0, amount, 0);
                                case "z" -> loc.add(0, 0, amount);
                                default -> {
                                    return CommandResult.failure("축은 x, y, z 중 하나여야 합니다.");
                                }
                            }

                            interaction.teleport(loc);
                            player.sendMessage(Component.text("Interaction을 " + axis + "축으로 " + amount + " 이동했습니다."));
                            return CommandResult.success();
                        })
                        .build()
                )
                .child(c -> c
                        .name("size").playerOnly()
                        .argument(WIDTH, ArgType.doubleValue(0.01, 10.0)).and()
                        .argument(HEIGHT, ArgType.doubleValue(0.01, 10.0)).and()
                        .runPlayer(ctx -> {
                            Player player = ctx.sender().player();
                            Interaction interaction = findNearest(player);
                            if (interaction == null) return CommandResult.failure("근처에 Interaction 엔티티가 없습니다.");

                            float width = (float) (double) ctx.arg(WIDTH);
                            float height = (float) (double) ctx.arg(HEIGHT);

                            interaction.setInteractionWidth(width);
                            interaction.setInteractionHeight(height);
                            player.sendMessage(Component.text("크기를 width=" + width + ", height=" + height + "로 변경했습니다."));
                            return CommandResult.success();
                        })
                        .build()
                )
                .build();
    }

    private static Interaction findNearest(Player player) {
        World world = player.getWorld();
        Location origin = player.getLocation();

        List<Entity> nearby = world.getNearbyEntities(origin, 2, 2, 2).stream()
                .filter(e -> e instanceof Interaction)
                .sorted(Comparator.comparingDouble(e -> e.getLocation().distanceSquared(origin)))
                .toList();

        return nearby.isEmpty() ? null : (Interaction) nearby.getFirst();
    }
}
