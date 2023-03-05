package fr.phunder.flashminigame.utils.message;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class MessageUtils {
    public static void playerMsg(Player player, MessageType type, String msg) {
        player.sendMessage(type.getColor() + "[" + type.getPrefix() + "] " + MessageConfig.getMessage(msg));
    }

    public static void playerMsg(Player player, MessageType type, String msg, HashMap<String, String> args) {
        String message = MessageConfig.getMessage(msg);
        for (String key : args.keySet()) {
            message = message.replace(key, args.get(key));
        }
        player.sendMessage(type.getColor() + "[" + type.getPrefix() + "] " + message);
    }

}


