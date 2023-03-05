package fr.phunder.flashminigame.utils;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import java.io.File;

public class WorldUtils {

    public static void loadWorlds() {
        final File[] folders = Bukkit.getWorld("world").getWorldFolder().listFiles();

        if (folders != null) {
            for (File folder : folders) {
                final String name = folder.getName();
                if (name.startsWith("world-fmg-")){
                    new WorldCreator(name).createWorld();
                }
            }
        }
    }
}
