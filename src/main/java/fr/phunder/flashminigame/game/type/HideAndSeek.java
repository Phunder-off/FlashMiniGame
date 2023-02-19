package fr.phunder.flashminigame.game.type;

import fr.phunder.flashminigame.game.Game;
import org.bukkit.entity.Player;

public class HideAndSeek extends Game {

    public HideAndSeek(Player player) {
        super(player);
        setGameType(GameType.HIDEANDSEEK);
    }
}
