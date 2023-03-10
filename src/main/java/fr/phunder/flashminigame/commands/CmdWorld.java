package fr.phunder.flashminigame.commands;

import fr.phunder.flashminigame.utils.CommandUtils;
import fr.phunder.flashminigame.utils.message.MessageType;
import fr.phunder.flashminigame.utils.message.MessageUtils;
import fr.phunder.flashminigame.utils.world.EmptyChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CmdWorld implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
        if (!CommandUtils.validName(name, "world")) return true;

        final Player player = CommandUtils.senderIsPlayer(sender);
        if (player == null) return true;

        final int nbrArgs = args.length;

        if (nbrArgs == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                new WorldCreator("world-fmg-" + args[1]).generator(new EmptyChunkGenerator()).createWorld();
                MessageUtils.playerMsg(
                        player,
                        MessageType.INFO,
                        "world.create",
                        new HashMap<String, String>() {{
                            put("{world}", args[1]);
                        }}
                );
                return true;
            }
            if (args[0].equalsIgnoreCase("tp")) {
                World world = Bukkit.getWorld(args[1]);
                if (world == null) {
                    MessageUtils.playerMsg(player, MessageType.ERROR, "world.not.exist",
                            new HashMap<String, String>() {{
                                put("{world}", args[1]);
                            }}
                    );
                    return true;
                }
                player.teleport(world.getSpawnLocation());
                MessageUtils.playerMsg(player, MessageType.INFO, "world.tp", new HashMap<String, String>() {{
                    put("{world}", args[1]);
                }});
                return true;
            }
        }
        return false;
    }
}
