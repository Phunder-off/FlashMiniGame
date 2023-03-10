package fr.phunder.flashminigame.game.type;

import fr.phunder.flashminigame.game.Game;
import fr.phunder.flashminigame.game.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class HideAndSeek extends Game {

    private final List<UUID> hiders = new ArrayList<>();
    private final List<UUID> seekers = new ArrayList<>();

    public HideAndSeek(Player player) {
        super(player);
        setGameType(GameType.HIDEANDSEEK);
    }

    @Override
    public void start() {
        randomSeeker();

        atAllPlayers(player -> {
            if (!isSeeker(player)){
                addHider(player);
            }
        });
        super.start();
    }

    @Override
    public void end() {
        clearHiders();
        clearSeekers();
        Bukkit.unloadWorld(this.getWorld(), false);
        this.getWorld().getWorldFolder().delete();
        super.end();
    }

    private void randomSeeker() {
        final Player randomSeeker = getPlayers().get(new Random().nextInt(getPlayers().size() - 1));
        if (getHiders().contains(randomSeeker.getUniqueId())) removeHider(randomSeeker);
        addSeeker(randomSeeker);
    }

    @Override
    public void removePlayers(Player player) {
        super.removePlayers(player);
        if (isSeeker(player)) removeSeeker(player);
        if (isHider(player)) removeHider(player);
        if (getSeekers().isEmpty()) randomSeeker();
    }

    public List<UUID> getHiders() {
        return hiders;
    }

    public boolean isHider(Player player) {
        return getHiders().contains(player.getUniqueId());
    }

    public void addHider(Player player) {
        this.hiders.add(player.getUniqueId());
    }

    public void removeHider(Player player) {
        this.hiders.remove(player.getUniqueId());
    }

    public void clearHiders() {
        this.seekers.clear();
    }


    public List<UUID> getSeekers() {
        return seekers;
    }

    public boolean isSeeker(Player player) {
        return getSeekers().contains(player.getUniqueId());
    }

    public void addSeeker(Player player) {
        this.seekers.add(player.getUniqueId());
    }

    public void removeSeeker(Player player) {
        this.seekers.remove(player.getUniqueId());
    }

    public void clearSeekers() {
        this.seekers.clear();
    }
}
