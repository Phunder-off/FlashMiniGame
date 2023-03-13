package fr.phunder.flashminigame.commands.tab_completers;

import fr.phunder.flashminigame.game.Game;
import fr.phunder.flashminigame.game.type.GameType;
import fr.phunder.flashminigame.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TcGame implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String name, String[] args) {

        if (!CommandUtils.validName(name, "game")) return null;
        Player player = CommandUtils.senderIsPlayer(sender);
        if (player == null) return Collections.emptyList();

        final int nbrArgs = args.length;

        if (nbrArgs == 1) {
            return Arrays.asList("start", "types", "create", "leave", "invite", "join", "kick" , "players");
        }

        if (nbrArgs == 2) {
            if (args[0].equalsIgnoreCase("create")){
                return GameType.getAllDisplayName();
            }
            if (args[0].equalsIgnoreCase("join")){
                final List<UUID> uuids = Game.getPlayerInviteMap(player);
                if (uuids == null) return null;
                return uuids.stream().map(uuid -> {
                    final Player target = Bukkit.getPlayer(uuid);
                    if (target == null || !target.isOnline()) return null;
                    return target.getDisplayName();
                }).collect(Collectors.toList());
            }
            if (args[0].equalsIgnoreCase("kick")){
                final Game game = Game.getPlayerGame(player);
                if (game == null) return null;
                return game.getPlayers().stream().map(Player::getDisplayName).collect(Collectors.toList());
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