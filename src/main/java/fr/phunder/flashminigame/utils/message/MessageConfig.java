package fr.phunder.flashminigame.utils.message;

import fr.phunder.flashminigame.Plugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageConfig {

    public static void loadConfig(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!configFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        Plugin.messagesConfig = YamlConfiguration.loadConfiguration(configFile);
    }


    public static String getMessage(String path) {
        return Plugin.messagesConfig.getString(path);
    }
}
