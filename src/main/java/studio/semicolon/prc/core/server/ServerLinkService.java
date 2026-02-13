package studio.semicolon.prc.core.server;

import io.quill.paper.Bootable;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import studio.semicolon.prc.api.constant.text.ServerLinkConstants;
import studio.semicolon.prc.core.util.Servers;

public class ServerLinkService implements Bootable {
    @Override
    public void start(JavaPlugin plugin) {
        Server server = plugin.getServer();
        Servers.clearServerLinks(server);

        Servers.addServerLink(server, ServerLinkConstants.TITLE_1, ServerLinkConstants.URL_1);
        Servers.addServerLink(server, ServerLinkConstants.TITLE_2, ServerLinkConstants.URL_2);
        Servers.addServerLink(server, ServerLinkConstants.TITLE_3, ServerLinkConstants.URL_3);
        Servers.addServerLink(server, ServerLinkConstants.TITLE_4, ServerLinkConstants.URL_4);
        Servers.addServerLink(server, ServerLinkConstants.TITLE_5, ServerLinkConstants.URL_5);
    }

    @Override
    public void end(JavaPlugin plugin) { }
}