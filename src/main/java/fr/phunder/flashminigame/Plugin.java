package fr.phunder.flashminigame;


import fr.phunder.flashminigame.commands.CmdGame;
import fr.phunder.flashminigame.commands.CmdPing;
import fr.phunder.flashminigame.commands.tab_completers.TcGame;
import fr.phunder.flashminigame.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Plugin extends JavaPlugin {

    public static Map<UUID, Game> playerGameMap = new HashMap<>();
    public static Map<UUID, List<UUID>> playerInviteMap = new HashMap<>();



    @Override
    public void onEnable() {
        getCommand("ping").setExecutor(new CmdPing());
        getCommand("game").setExecutor(new CmdGame());
        getCommand("game").setTabCompleter(new TcGame());
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}