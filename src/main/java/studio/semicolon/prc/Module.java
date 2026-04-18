package studio.semicolon.prc;

import io.quill.paper.Quill;
import org.bukkit.plugin.java.JavaPlugin;
import studio.semicolon.prc.api.nms.NMS;
import studio.semicolon.prc.core.command.CommandRegister;
import studio.semicolon.prc.core.event.EventRegistrar;
import studio.semicolon.prc.api.machine.MachineManager;
import studio.semicolon.prc.core.module.build.BuildSessionManager;
import studio.semicolon.prc.core.nms.NMSImpl;
import studio.semicolon.prc.core.server.ServerLinkService;

public final class Module extends JavaPlugin {
    private static Module instance;
    private static NMS nms;

    @Override
    public void onLoad() {
        instance = this;
        nms = new NMSImpl();
    }

    @Override
    public void onEnable() {
        Quill.initialize(this,
                new EventRegistrar(),
                new CommandRegister(),
                new MachineManager(),
                new ServerLinkService(),
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

    public static NMS nms() {
        return nms;
    }
}
