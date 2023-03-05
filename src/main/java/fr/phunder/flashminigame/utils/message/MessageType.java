package fr.phunder.flashminigame.utils.message;

import org.bukkit.ChatColor;

public enum MessageType {
    INFO("Info", ChatColor.BLUE),
    WARNING("Alert", ChatColor.YELLOW),
    ERROR("Erreur", ChatColor.DARK_RED);

    private final String prefix;
    private final ChatColor color;

    MessageType(String prefix, ChatColor color) {
        this.prefix = prefix;
        this.color = color;
    }

    public String getPrefix() {
        return prefix;
    }

    public ChatColor getColor() {
        return color;
    }
}
