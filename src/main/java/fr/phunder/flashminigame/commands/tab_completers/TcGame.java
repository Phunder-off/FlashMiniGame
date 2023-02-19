package fr.phunder.flashminigame.commands.tab_completers;

import fr.phunder.flashminigame.game.type.GameType;
import fr.phunder.flashminigame.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TcGame implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String name, String[] args) {

        if (!CommandUtils.validName(name, "game")) return null;
        Player player = CommandUtils.senderIsPlayer(sender);
        if (player == null) return Collections.emptyList();

        final int nbrArgs = args.length;

        if (nbrArgs == 1) {
            return Arrays.asList("types", "create", "leave", "invite");
        }

        if (nbrArgs == 2) {
            if (args[0].equalsIgnoreCase("create")){
                return GameType.getAllDisplayName();
            }
        }

        return null;
    }
}
/*
argsCompleter.addAll(Arrays.stream(Type.values())
    .map(Type::getDisplayName)
    .collect(Collectors.toList()));
*/