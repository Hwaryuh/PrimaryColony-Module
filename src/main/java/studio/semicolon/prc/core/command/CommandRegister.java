package studio.semicolon.prc.core.command;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.quill.paper.Bootable;
import io.quill.paper.command.CommandRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import studio.semicolon.prc.core.command.module.ModuleLabel;

@SuppressWarnings("UnstableApiUsage")
public class CommandRegister implements Bootable {
    @Override
    public void start(JavaPlugin plugin) {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            CommandRegistry registry = new CommandRegistry(event.registrar());
            registry.register(ModuleLabel.create());
            registry.register(TestLabel.create());
        });
    }

    @Override
    public void end(JavaPlugin plugin) { }
}