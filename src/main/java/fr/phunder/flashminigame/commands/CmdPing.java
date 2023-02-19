package fr.phunder.flashminigame.commands;

import fr.phunder.flashminigame.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdPing implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
        if (!CommandUtils.validName(name, "ping")) return true;

        final Player player = CommandUtils.senderIsPlayer(sender);
        if (player == null) return true;

        player.sendMessage("Pong !");
        return true;
    }
}
