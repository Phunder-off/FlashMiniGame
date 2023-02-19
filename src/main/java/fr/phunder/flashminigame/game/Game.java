package fr.phunder.flashminigame.game;

import fr.phunder.flashminigame.Plugin;
import fr.phunder.flashminigame.game.type.GameType;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Game {
    private UUID uuid;
    private GameType gameType;
    private Player owner;
    private List<Player> players = new ArrayList<>();
    private GameStatus gameStatus = GameStatus.WAITING;
    private Timestamp timeStart;
    private Timestamp timeEnd;

    public Game(Player player) {
        this.uuid = UUID.randomUUID();
        this.setOwner(player);
        this.addPlayers(player);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isOwer(Player player){
        return getOwner().equals(player);
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayers(Player player) {
        this.players.add(player);
    }

    public void removePlayers(Player player) {
        this.players.remove(player);
        if (!getPlayers().isEmpty() && getOwner().equals(player)) {
            this.setOwner(getPlayers().get(0));
            this.getOwner().sendMessage("Tu est devenu chef de ta partie");
        }
        Game.removePlayerGameMap(player);
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    public static Game getPlayerGame(Player player) {
        return Plugin.playerGameMap.get(player);
    }

    public static void removePlayerGameMap(Player player) {
        Plugin.playerGameMap.remove(player);
    }

    public static void addPlayerGameMap(Player player, Game game) {
        Plugin.playerGameMap.put(player, game);
    }

    public static List<Player> getPlayerInviteMap(Player player){
        return Plugin.playerInviteMap.get(player);
    }

    public static void addPlayerInviteMap(Player player, Player target){
        final List<Player> targetInviteMap = getPlayerInviteMap(target);
        targetInviteMap.add(player);
        Plugin.playerInviteMap.put(target, targetInviteMap);
    }



}
