package fr.phunder.flashminigame.commands;

import fr.phunder.flashminigame.game.Game;
import fr.phunder.flashminigame.game.type.GameType;
import fr.phunder.flashminigame.utils.CommandUtils;
import fr.phunder.flashminigame.utils.message.MessageType;
import fr.phunder.flashminigame.utils.message.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.not.in");
                    return true;
                }
                game.removePlayers(player);
                MessageUtils.playerMsg(player, MessageType.INFO, "game.leave");
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                player.sendMessage("============ Help ============");
                return true;
            }
            if (args[0].equalsIgnoreCase("players")) {
                final Game game = Game.getPlayerGame(player);
                if (game == null) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.not.in");
                    return true;
                }
                String players = game.getPlayers().stream().map(Player::getDisplayName).collect(Collectors.toList()).toString();

                MessageUtils.playerMsg(player, MessageType.INFO, "game.players", new HashMap<String, String>() {{
                    put("{owner}", game.getOwner().getDisplayName());
                    put("{players}", players);
                }});

                return true;
            }
            if (args[0].equalsIgnoreCase("start")) {
                final Game game = Game.getPlayerGame(player);
                if (game == null) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.not.in");
                    return true;
                }
                if (!game.isOwner(player)) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.not.ower");
                    return true;
                }
                game.prestart();
                return true;
            }
        }

        if (nbrArgs == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                final Game game = Game.getPlayerGame(player);
                if (game != null) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.in");
                    return true;
                }

                GameType gameType = GameType.getType(args[1]);
                if (!GameType.getAllDisplayName().contains(args[1]) && gameType == null) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.type.not-exist");
                    gameTypes(player);
                    return true;
                }
                gameCreate(player, gameType);
                return true;
            }

            if (args[0].equalsIgnoreCase("invite")) {
                final Game game = Game.getPlayerGame(player);
                if (game == null) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.not.in");
                    return true;
                }
                if (!game.isOwner(player)) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.not.ower");
                    return true;
                }
                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if (targetPlayer == null || !targetPlayer.isOnline()) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "player.not-found");
                    return true;
                }
                Game.addPlayerInviteMap(player, targetPlayer);

                MessageUtils.playerMsg(player, MessageType.INFO, "game.invite.sender",
                        new HashMap<String, String>() {{
                            put("{target}", targetPlayer.getDisplayName());
                            put("{gameType}", game.getGameType().getDisplayName());
                        }}
                );
                MessageUtils.playerMsg(targetPlayer, MessageType.INFO, "game.invite.receiver",
                        new HashMap<String, String>() {{
                            put("{sender}", player.getDisplayName());
                            put("{gameType}", game.getGameType().getDisplayName());
                        }}
                );
                return true;
            }

            if (args[0].equalsIgnoreCase("join")) {
                final Game game = Game.getPlayerGame(player);
                if (game != null) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.in");
                    return true;
                }
                final Player targetPlayer = Bukkit.getPlayer(args[1]);
                if (targetPlayer == null || !targetPlayer.isOnline()) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "player.not-found");
                    return true;
                }
                final List<UUID> invitations = Game.getPlayerInviteMap(player);
                if (invitations == null) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.not.invite");
                    return true;
                }
                if (!invitations.contains(targetPlayer.getUniqueId())) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.not.invite-by");
                    return true;
                }
                final Game gameTarget = Game.getPlayerGame(targetPlayer);
                if (gameTarget == null) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.not.exist");
                    return true;
                }
                gameTarget.addPlayers(player);
                Game.removePlayerInviteMap(player, targetPlayer);

                MessageUtils.playerMsg(player, MessageType.INFO, "game.join.guest",
                        new HashMap<String, String>() {{
                            put("{target}", targetPlayer.getDisplayName());
                        }}
                );
                MessageUtils.playerMsg(targetPlayer, MessageType.INFO, "game.join.owner",
                        new HashMap<String, String>() {{
                            put("{player}", player.getDisplayName());
                        }}
                );
                return true;
            }

            if (args[0].equalsIgnoreCase("kick")) {
                final Game game = Game.getPlayerGame(player);
                if (game == null) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.not.in");
                    return true;
                }
                if (!game.isOwner(player)) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "game.not.ower");
                    return true;
                }
                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if (targetPlayer == null) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "player.not-found");
                    return true;
                }

                game.removePlayers(targetPlayer);
                MessageUtils.playerMsg(
                        player,
                        MessageType.INFO,
                        "game.kick.owner",
                        new HashMap<String, String>() {{
                            put("{target}", targetPlayer.getDisplayName());
                        }}
                );
                MessageUtils.playerMsg(
                        targetPlayer,
                        MessageType.WARNING,
                        "game.kick.target",
                        new HashMap<String, String>() {{
                            put("{player}", player.getDisplayName());
                        }}
                );
                return true;
            }
        }
        return false;
    }

    private void gameCreate(Player player, GameType gameType) {
        try {
            Constructor<Game> constructor = gameType.getGameClass().getDeclaredConstructor(Player.class);
            Game game = constructor.newInstance(player);
            Game.addPlayerGameMap(player, game);

            MessageUtils.playerMsg(player, MessageType.INFO, "game.create.success",
                    new HashMap<String, String>() {{
                        put("{gameType}", gameType.getDisplayName());
                    }}
            );
        } catch (Exception e) {
            MessageUtils.playerMsg(player, MessageType.INFO, "game.create.error");
        }
    }

    private void gameTypes(Player player) {
        List<String> types = Arrays.stream(GameType.values()).map(GameType::getDisplayName).collect(Collectors.toList());
        MessageUtils.playerMsg(player, MessageType.INFO, "game.type.list", new HashMap<String, String>() {{
            put("{types}", types.toString());
        }});
    }
}
