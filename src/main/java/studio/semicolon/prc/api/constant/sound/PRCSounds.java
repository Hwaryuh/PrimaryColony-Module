package studio.semicolon.prc.api.constant.sound;

import net.kyori.adventure.sound.SoundStop;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public interface PRCSounds {
    SoundData GLOBAL_CLICK = new SoundData("minecraft:semicolon.click", 1.3f, 1.0f);
    SoundData GLOBAL_ERROR = new SoundData("minecraft:semicolon.error", 1.0f, 1.0f);

    SoundData MACHINE_PLACE = new SoundData("minecraft:block.heavy_core.place", 1.0f, 1.4f);
    SoundData MACHINE_INSERT_ITEM = new SoundData("minecraft:item.armor.equip_generic", 1.0f, 0.75f);
    SoundData MACHINE_DESTROY = new SoundData("minecraft:block.copper.place", 1.0f, 0.5f);
    SoundData MACHINE_UPGRADE = new SoundData("minecraft:semicolon.upgrade", 1.2f, 1.0f);

    SoundData COFFEE_MACHINE_START = new SoundData("minecraft:semicolon.coffee_module_start", 1.5f, 1.0f);
    SoundData FURNACE_MACHINE_START = new SoundData("minecraft:semicolon.furnace_module_start", 1.5f, 1.0f);
    SoundData GRINDER_MACHINE_START = new SoundData("minecraft:semicolon.grinder_module_start", 1.5f, 1.0f);
    SoundData PRINT_MACHINE_START = new SoundData("minecraft:semicolon.print_module_start", 1.5f, 1.0f);

    SoundData MODULE_MOVE_INDICATOR = new SoundData("minecraft:semicolon.build_module_move", 1.5f, 1.0f);
    SoundData MODULE_VALIDATE_SUCCESS = new SoundData("minecraft:semicolon.build_module_build", 1.5f, 1.0f);
    SoundData MODULE_BUILD_MODE_CANCEL = new SoundData("minecraft:semicolon.build_module_cancel", 1.5f, 1.0f);
    SoundData MODULE_BUILD_PROGRESS = new SoundData("minecraft:semicolon.build_module_progress", 1.75f, 1.0f);

    SoundData MODULE_DOOR_OPEN = new SoundData("minecraft:semicolon.build_module_door_open", 1.0f, 1.0f);
    SoundData MODULE_DOOR_CLOSE = new SoundData("minecraft:semicolon.build_module_door_close", 1.0f, 1.0f);
    SoundData MODULE_INTERACT_ENTRY = new SoundData("minecraft:semicolon.entry_module_interact", 1.0f, 1.0f);

    record SoundData(String sound, float volume, float pitch) {
        public void play(Location location) {
            play(location, volume, pitch);
        }

        public void play(Player player) {
            play(player, volume, pitch);
        }

        public void play(Location location, float newVolume, float newPitch) {
            location.getWorld().playSound(location, sound, newVolume, newPitch);
        }

        public void play(Player player, float newVolume, float newPitch) {
            player.playSound(player, sound, newVolume, newPitch);
        }

        public void stopAndPlay(Location location) {
            stopAndPlay(location, volume, pitch);
        }

        public void stopAndPlay(Location location, float newVolume, float newPitch) {
            String soundKey = sound.startsWith("minecraft:") ? sound.substring(10) : sound;

            location.getWorld().stopSound(SoundStop.named(NamespacedKey.minecraft(soundKey)));
            location.getWorld().playSound(location, sound, newVolume, newPitch);
        }

        public void stopAndPlay(Player player) {
            stopAndPlay(player, volume, pitch);
        }

        public void stopAndPlay(Player player, float newVolume, float newPitch) {
            String soundKey = sound.startsWith("minecraft:") ? sound.substring(10) : sound;

            player.stopSound(SoundStop.named(NamespacedKey.minecraft(soundKey)));
            player.playSound(player, sound, newVolume, newPitch);
        }

        public SoundData withVolume(float newVolume) {
            return new SoundData(sound, newVolume, pitch);
        }

        public SoundData withPitch(float newPitch) {
            return new SoundData(sound, volume, newPitch);
        }

        public SoundData with(float newVolume, float newPitch) {
            return new SoundData(sound, newVolume, newPitch);
        }
    }
}