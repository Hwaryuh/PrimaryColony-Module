package studio.semicolon.prc.core.util;

import io.quill.paper.util.bukkit.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.ServerLinks;

import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("UnstableApiUsage")
public class Servers {
    public static void addServerLink(Server server, Component name, String url) {
        try {
            server.getServerLinks().addLink(name, new URI(url));
        } catch (URISyntaxException e) {
            Logger.error("" + e);
        }
    }

    public static void clearServerLinks(Server server) {
        ServerLinks serverLinks = server.getServerLinks();
        serverLinks.getLinks().forEach(serverLinks::removeLink);
    }
}