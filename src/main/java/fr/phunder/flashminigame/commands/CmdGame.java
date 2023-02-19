package fr.phunder.flashminigame.commands;

import fr.phunder.flashminigame.game.Game;
import fr.phunder.flashminigame.game.type.GameType;
import fr.phunder.flashminigame.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class CmdGame implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
        if (!CommandUtils.validName(name, "game")) return true;

        final Player player = CommandUtils.senderIsPlayer(sender);
        if (player == null) return true;

        final int nbrArgs = args.length;

        if (nbrArgs == 0) return false;

        if (nbrArgs == 1) {
            if (args[0].equalsIgnoreCase("types")) {
                gameTypes(player);
                return true;
            }
            if (args[0].equalsIgnoreCase("leave")) {
                final Game game = Game.getPlayerGame(player);
                if (game == null) {
                    player.sendMessage("Vous etes dans aucune partie");
                    player.sendMessage("Pour cree une parti (/game create <type>)");
                    return true;
                }
                game.removePlayers(player);
                player.sendMessage("Vous venez de quitter votre partie");
                return true;
            }
        }

        if (nbrArgs == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                final Game game = Game.getPlayerGame(player);
                if (game != null) {
                    player.sendMessage("Vous etes deja dans une partie " + game.getGameStatus().getDisplayName());
                    return true;
                }

                GameType gameType = GameType.getType(args[1]);
                if (!GameType.getAllDisplayName().contains(args[1]) && gameType == null) {
                    player.sendMessage("Ce mode de jeu n'existe pas !");
                    gameTypes(player);
                    return true;
                }
                gameCreate(player, gameType);
            }
        }
        return false;
    }

    private void gameCreate(Player player, GameType gameType) {
        try {
            Constructor<Game> constructor = gameType.getGameClass().getDeclaredConstructor(Player.class);
            Game game = constructor.newInstance(player);
            Game.addPlayerGameMap(player, game);
            player.sendMessage("Partie créer avec succes (" + game.getUuid() + ")");
        } catch (Exception e) {
            player.sendMessage("Echec de la creation de la partie !");
        }
    }

    private void gameTypes(Player player) {
        player.sendMessage("Liste de mode de jeux");
        Arrays.stream(GameType.values()).forEach(gameType -> player.sendMessage("- " + gameType.getDisplayName()));
    }
}
