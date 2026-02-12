package studio.semicolon.prc.api.item;

import org.bukkit.Material;

public interface IConfigureItem {
    static ConfiguredItemBuilder builder(Material material) {
        return new ConfiguredItemBuilder(material);
    }
}