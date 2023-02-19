package fr.phunder.flashminigame.game.type;

import fr.phunder.flashminigame.game.Game;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum GameType {
    HIDEANDSEEK("Hide-and-seek", HideAndSeek.class);

    private final String displayName;
    private final Class<Game> gameClass;

    GameType(String displayName, Class gameClass) {
        this.displayName = displayName;
        this.gameClass = gameClass;
    }



    public Class<Game> getGameClass() {
        return gameClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static GameType getType(String nameType) {
        for (GameType gameType : GameType.values()) {
            if (nameType.equalsIgnoreCase(gameType.getDisplayName())) {
                return gameType;
            }
        }
        return null;
    }

    public static List<String> getAllDisplayName() {
        return Arrays.stream(GameType.values()).map(GameType::getDisplayName).collect(Collectors.toList());
    }




}