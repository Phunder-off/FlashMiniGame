package fr.phunder.flashminigame.utils.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class WorldUtils {

    public static boolean duplicateWorld(String worldName, String newWorldName) {
        Path worldPath = Paths.get("./world/" + worldName);
        Path newWorldPath = Paths.get("./world/" + newWorldName);

        try (Stream<Path> walk = Files.walk(worldPath)) {
            walk.forEach(sourcePath -> {
                try {
                    if (sourcePath.endsWith("uid.dat")) return;
                    Path targetPath = newWorldPath.resolve(worldPath.relativize(sourcePath));
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.format("I/O error: %s%n", e);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void loadWorlds() {
        final File[] folders = Bukkit.getWorld("world").getWorldFolder().listFiles();

        if (folders != null) {
            for (File folder : folders) {
                final String name = folder.getName();
                if (name.startsWith("world-fmg-")) {
                    loadWorld(name);
                }
            }
        }
    }

    public static World loadWorld(String worldName) {
        return new WorldCreator(worldName).createWorld();
    }
}
