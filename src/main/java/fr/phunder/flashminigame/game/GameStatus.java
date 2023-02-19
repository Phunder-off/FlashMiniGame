package fr.phunder.flashminigame.game;

public enum GameStatus {
    WAITING("En attente"),
    STARTING("Démarrage en cours"),
    IN_PROGRESS("En cours"),
    FINISHED("Terminé");

    private final String displayName;

    GameStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
