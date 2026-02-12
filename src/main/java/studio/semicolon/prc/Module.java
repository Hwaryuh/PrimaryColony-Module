package studio.semicolon.prc;

import io.quill.paper.Quill;
import org.bukkit.plugin.java.JavaPlugin;
import studio.semicolon.prc.core.command.CommandRegister;
import studio.semicolon.prc.core.event.EventRegistrar;
import studio.semicolon.prc.api.machine.MachineManager;
import studio.semicolon.prc.core.module.build.BuildSessionManager;

public final class Module extends JavaPlugin {
    private static Module instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Quill.initialize(this,
                new EventRegistrar(),
                new MachineManager(),
                new CommandRegister(),
                BuildSessionManager.getInstance()
        );
    }

    @Override
    public void onDisable() {
        Quill.shutdown(this);
    }

    public static Module getInstance() {
        return instance;
    }
}