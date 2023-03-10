package fr.phunder.flashminigame.game;

import fr.phunder.flashminigame.Plugin;
import fr.phunder.flashminigame.game.type.GameType;
import fr.phunder.flashminigame.utils.world.WorldUtils;
import fr.phunder.flashminigame.utils.count.CountdownTimer;
import fr.phunder.flashminigame.utils.message.MessageType;
import fr.phunder.flashminigame.utils.message.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
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

    protected Game(Player player) {
        this.uuid = UUID.randomUUID();
        this.setOwner(player);
        this.addPlayers(player);
        setGameStatus(GameStatus.WAITING);
    }

    public void prestart() {
        setGameStatus(GameStatus.STARTING);

        final String worldTemplate = "world-fmg-" + this.getGameType().getDisplayName() + "-Template/";
        final String worldName = "game-" + this.getUuid();

        MessageUtils.playerMsg(this.getOwner(), MessageType.INFO, "world.create.waiting");
        if (!WorldUtils.duplicateWorld(worldTemplate, worldName)) {
            MessageUtils.playerMsg(this.getOwner(), MessageType.ERROR, "world.create.error", new HashMap<String, String>() {{
                put("{world}", worldName);
            }});
            return;
        }

        final World world = WorldUtils.loadWorld(worldName);
        if (world == null) {
            MessageUtils.playerMsg(this.getOwner(), MessageType.ERROR, "world.load.error", new HashMap<String, String>() {{
                put("{world}", worldName);
            }});
            return;
        }

        this.setWorld(world);

        atAllPlayers((Player p) -> p.teleport(this.getWorld().getSpawnLocation()));

        new CountdownTimer(
                5,
                (String time) -> atAllPlayers((Player p) -> p.sendTitle(time, "La partie va commencer", 5, 10, 5)),
                this::start
        ).runTaskTimer(Plugin.plugin, 0, 20);
    }

    public void start() {
        atAllPlayers((Player p) -> p.sendTitle("Good luck !", "La partie commence !", 5, 10, 5));

        setGameStatus(GameStatus.IN_PROGRESS);
        setTimeStart(new Timestamp(System.currentTimeMillis()));
    }

    public void end(){
        atAllPlayers((Player p) -> {
            p.teleport(Bukkit.getWorld("world").getSpawnLocation());
            p.sendTitle("Terminer", "La partie est terminer", 5, 20, 5);
        });

        setGameStatus(GameStatus.FINISHED);
        setTimeEnd(new Timestamp(System.currentTimeMillis()));
    }

    protected void atAllPlayers(Consumer<Player> action) {
        for (Player player : this.getPlayers()) {
            action.accept(player);
        }
    }


    public UUID getUuid() {
        return uuid;
    }


    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }


    public Player getOwner() {
        return Bukkit.getPlayer(owner);
    }

    public void setOwner(Player owner) {
        this.owner = owner.getUniqueId();
    }

    public boolean isOwner(Player player) {
        return getOwner().getUniqueId().equals(player.getUniqueId());
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
        if (!getPlayers().isEmpty() && getOwner().getUniqueId().equals(player.getUniqueId())) {
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
