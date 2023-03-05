package fr.phunder.flashminigame.commands.tab_completers;

import fr.phunder.flashminigame.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TcWorld implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String name, String[] args) {

        if (!CommandUtils.validName(name, "world")) return null;
        Player player = CommandUtils.senderIsPlayer(sender);
        if (player == null) return Collections.emptyList();

        final int nbrArgs = args.length;

        if (nbrArgs == 1) {
            return Arrays.asList("create", "tp");
        }

        if (nbrArgs == 2) {
            if (args[0].equalsIgnoreCase("tp")){
                return Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
            }
        }

        return null;
    }
}
