package de.lojaw.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

public class WorldSaver {
    private static int NumberOfMatches = 0;

    public static List<String> getWorlds() {

        Path rootPath = Path.of("").toAbsolutePath();
        String str = rootPath.toString() + "\\plugins\\" + Main.pluginPrefix + "\\worlds";
        Path worldsDir = Path.of( str);
        List<String> worlds = null;
        try {
            worlds = listDirectories(worldsDir);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return worlds;
    }

    public static void copyWorld(String name) {
        String currentDirectoryName = Path.of("").toAbsolutePath().toString();

        ArrayList<String> matchedPaths = new ArrayList<String>();
        calculateMatches(Path.of(currentDirectoryName), matchedPaths);

        for(String path : matchedPaths) {
            if(path.contains(name)) {
                String source = navigateUpwards(path, currentDirectoryName);
                String destination = currentDirectoryName + "\\plugins\\" + Main.pluginPrefix + "\\worlds";
                copyDirectory(source, destination);
            }
        }
    }

    public static void copyAllWorlds() {
        String currentDirectoryName = Path.of("").toAbsolutePath().toString();

        ArrayList<String> matchedPaths = new ArrayList<String>();
        calculateMatches(Path.of(currentDirectoryName), matchedPaths);

        for(String path : matchedPaths) {
            String source = navigateUpwards(path, currentDirectoryName);
            String destination = currentDirectoryName + "\\plugins\\" + Main.pluginPrefix + "\\worlds";
            copyDirectory(source, destination);
        }
    }

    public static void copyDirectory(String source, String destination) {

        String lastPart = Path.of(source).getFileName().toString();
        File destDir = new File(destination + "\\" + lastPart);
        File srcDir = new File(source);
        try {
            FileUtils.copyDirectory(srcDir, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String navigateUpwards(String pathStr, String homePathStr) {
        Path path = Path.of(pathStr);
        Path homePath = Path.of(homePathStr);
        Path currentPath = null;

        while (path.compareTo(homePath) != 0) {
            currentPath = path;
            path = path.getParent();
        }

        return currentPath.toString();
    }

    public static void calculateMatches(Path path, ArrayList<String> matchedPaths) {
        List<String> names = null;
        List<String> directories = null;
        try {
            names = listFiles(path);
            if(names != null) {
                names.forEach(f -> matchedPaths.add(f));
            }

            directories = listDirectories(path);

            for(String d : directories) {
                if(!d.contains(Main.pluginPrefix)) {
                    calculateMatches(Path.of(d), matchedPaths);
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        if(names != null) {
            NumberOfMatches += names.size();
        }
    }

    public static List<String> listFiles(Path dir) throws IOException {
        try (Stream<Path> stream = Files.list(dir)) {
            return stream
                    .filter(p -> !Files.isDirectory(p))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .filter(s -> isMatch(s))
                    .collect(Collectors.toList());
        }
    }

    public static List<String> listDirectories(Path dir) throws IOException {
        try (Stream<Path> stream = Files.list(dir)) {
            return stream
                    .filter(p -> Files.isDirectory(p))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
    }

    public static boolean isMatch(String s) {

        if(s.endsWith("data")) {
            return true;

        } else if(s.endsWith("playerdata")) {
            return true;

        } else if(s.endsWith("region")) {
            return true;

        } else if(s.endsWith("stats")) {
            return true;

        } else if(s.endsWith("DIM-1")) {
            return true;

        } else if(s.endsWith("DIM1")) {
            return true;

        } else if(s.endsWith("level.dat")) {
            return true;

        } else if(s.endsWith("level.dat_old")) {
            return true;

        } else if(s.endsWith("session.lock")) {
            return true;

        } else if(s.endsWith("uid.dat")) {
            return true;

        }

        return false;
    }

    public static boolean isExist(String worldName) {
        String currentDirectoryName = Path.of("").toAbsolutePath().toString();
        ArrayList<String> matchedPaths = new ArrayList<String>();
        calculateMatches(Path.of(currentDirectoryName), matchedPaths);

        for(String path : matchedPaths) {
            if(path.contains(worldName)) {
                return true;
            }
        }
        return false;
    }
}

