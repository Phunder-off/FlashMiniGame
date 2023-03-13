package fr.phunder.flashminigame.game.eventListerners;

import fr.phunder.flashminigame.game.Game;
import fr.phunder.flashminigame.game.GameStatus;
import fr.phunder.flashminigame.game.type.GameType;
import fr.phunder.flashminigame.game.type.HideAndSeek;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HideAndSideEventListerners implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        final Player p = e.getEntity();
        final Game game = Game.getPlayerGame(p);

        if (game == null || !game.getGameType().equals(GameType.HIDEANDSEEK) || !game.getGameStatus().equals(GameStatus.IN_PROGRESS)) return;
        final HideAndSeek hideAndSeek = (HideAndSeek) game;

        if (hideAndSeek.isHider(p)) {
            hideAndSeek.removeHider(p);
            hideAndSeek.addSeeker(p);
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        }

        if (hideAndSeek.getHiders().isEmpty()) hideAndSeek.end();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        final Player p = e.getPlayer();
        final Game game = Game.getPlayerGame(p);

        if (game == null || !game.getGameType().equals(GameType.HIDEANDSEEK) || !game.getGameStatus().equals(GameStatus.IN_PROGRESS)) return;
        final HideAndSeek hideAndSeek = (HideAndSeek) game;

        if (hideAndSeek.getHiders().isEmpty()) hideAndSeek.end();
    }


}
