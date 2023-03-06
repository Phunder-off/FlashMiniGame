package fr.phunder.flashminigame.game;

import fr.phunder.flashminigame.Plugin;
import fr.phunder.flashminigame.game.type.GameType;
import fr.phunder.flashminigame.utils.WorldUtils;
import fr.phunder.flashminigame.utils.message.MessageType;
import fr.phunder.flashminigame.utils.message.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class Game {

    private final UUID uuid;
    private World world;
    private GameType gameType;
    private UUID owner;
    private final List<UUID> players = new ArrayList<>();
    private GameStatus gameStatus = GameStatus.WAITING;
    private Timestamp timeStart;
    private Timestamp timeEnd;

    public Game(Player player) {
        this.uuid = UUID.randomUUID();
        this.setOwner(player);
        this.addPlayers(player);
    }

    public void start() {
        final String worldTemplate = "world-fmg-" + this.getGameType().getDisplayName() + "-Template/";
        final String worldName = "game-" + this.getUuid();

        if (!WorldUtils.duplicateWorld(worldTemplate, worldName)) {
            MessageUtils.playerMsg(this.getOwner(), MessageType.ERROR, "world.create.error",new HashMap<String,String>(){{
                put("{world}", worldName);
            }});
            return;
        }

        final World world = WorldUtils.loadWorld(worldName);
        if (world == null) {
            MessageUtils.playerMsg(this.getOwner(), MessageType.ERROR, "world.load.error",new HashMap<String,String>(){{
                put("{world}", worldName);
            }});
            return;
        }

        this.world = world;

        //this.getPlayers().forEach(player -> player.teleport(this.getWorld().getSpawnLocation()));

        //new Thread(() -> {
        //    for (int i = 5; i > 0; i--) {
        //        for (Player player : getPlayers()) {
        //            player.sendTitle(String.valueOf(i), "La partie va commencer", 5, 20, 5);
        //        }

        //        try {
        //            Thread.sleep(1000);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //}).start();
    }

    public UUID getUuid() {
        return uuid;
    }

    public World getWorld() {
        return world;
    }

    public Player getOwner() {
        return Bukkit.getPlayer(owner);
    }

    public boolean isOwner(Player player) {
        return getOwner().equals(player);
    }

    public void setOwner(Player owner) {
        this.owner = owner.getUniqueId();
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public List<Player> getPlayers() {
        return players.stream().map(Bukkit::getPlayer).collect(Collectors.toList());
    }

    public void addPlayers(Player player) {
        this.players.add(player.getUniqueId());
        addPlayerGameMap(player, this);
    }

    public void removePlayers(Player player) {
        this.players.remove(player.getUniqueId());
        if (!getPlayers().isEmpty() && getOwner().equals(player.getUniqueId())) {
            this.setOwner(getPlayers().get(0));
            MessageUtils.playerMsg(this.getOwner(), MessageType.INFO, "game.new-owner");
        }
        removePlayerGameMap(player);
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
        return Plugin.playerGameMap.get(player.getUniqueId());
    }

    public static void removePlayerGameMap(Player player) {
        Plugin.playerGameMap.remove(player.getUniqueId());
    }

    public static void addPlayerGameMap(Player player, Game game) {
        Plugin.playerGameMap.put(player.getUniqueId(), game);
    }

    public static List<UUID> getPlayerInviteMap(Player player) {
        return Plugin.playerInviteMap.get(player.getUniqueId());
    }

    public static void addPlayerInviteMap(Player player, Player target) {
        Plugin.playerInviteMap.computeIfAbsent(target.getUniqueId(), k -> new ArrayList<>()).add(player.getUniqueId());
    }

    public static void removePlayerInviteMap(Player player, Player target) {
        final List<UUID> playerInviteMap = getPlayerInviteMap(player);
        playerInviteMap.remove(target.getUniqueId());
        Plugin.playerInviteMap.put(player.getUniqueId(), playerInviteMap);
    }


}
