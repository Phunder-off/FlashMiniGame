package fr.phunder.flashminigame.commands;

import fr.phunder.flashminigame.utils.CommandUtils;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdWorld implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
        if (!CommandUtils.validName(name, "game")) return true;

        final Player player = CommandUtils.senderIsPlayer(sender);
        if (player == null) return true;

        final int nbrArgs = args.length;

        if (nbrArgs == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                new WorldCreator();
            }
        }

        return false;
    }
}
