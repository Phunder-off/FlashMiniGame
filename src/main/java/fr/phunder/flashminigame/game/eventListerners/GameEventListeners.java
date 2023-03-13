package fr.phunder.flashminigame.game.eventListerners;

import fr.phunder.flashminigame.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameEventListeners implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        final Player p = e.getPlayer();
        final Game game = Game.getPlayerGame(p);

        if (game != null) {
            game.removePlayer(p);
            p.teleport(Bukkit.getWorld("world").getSpawnLocation());
        }
    }

}
