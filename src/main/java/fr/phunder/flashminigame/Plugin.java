package fr.phunder.flashminigame;


import fr.phunder.flashminigame.commands.CmdGame;
import fr.phunder.flashminigame.commands.CmdPing;
import fr.phunder.flashminigame.commands.CmdWorld;
import fr.phunder.flashminigame.commands.tab_completers.TcGame;
import fr.phunder.flashminigame.commands.tab_completers.TcWorld;
import fr.phunder.flashminigame.game.Game;
import fr.phunder.flashminigame.utils.WorldUtils;
import fr.phunder.flashminigame.utils.message.MessageConfig;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Plugin extends JavaPlugin {

    public static Map<UUID, Game> playerGameMap = new HashMap<>();
    public static Map<UUID, List<UUID>> playerInviteMap = new HashMap<>();
    public static FileConfiguration messagesConfig;

    @Override
    public void onEnable() {
        MessageConfig.loadConfig(this);
        WorldUtils.loadWorlds();
        getCommand("ping").setExecutor(new CmdPing());
        getCommand("game").setExecutor(new CmdGame());
        getCommand("game").setTabCompleter(new TcGame());
        getCommand("world").setExecutor(new CmdWorld());
        getCommand("world").setTabCompleter(new TcWorld());
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}