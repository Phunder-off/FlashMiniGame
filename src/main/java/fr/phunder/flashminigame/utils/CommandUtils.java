package fr.phunder.flashminigame.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUtils {

    public static boolean validName(String name, String commandName){
        return name.equalsIgnoreCase(commandName);
    }

    public static Player senderIsPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette command ne peux etre execute que par un joueur");
            return null;
        }
        return ((Player) sender).getPlayer();
    }



}
