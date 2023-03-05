package fr.phunder.flashminigame.utils.message;

import fr.phunder.flashminigame.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageConfig {

    public MessageConfig(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!configFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        for (String key : config.getKeys(false)) {
            Plugin.messages.put(key, config.getString(key));
        }
    }

    public static String getMessage(String key) {
        return Plugin.messages.get(key);
    }
}
