package de.lojaw.main;

import de.lojaw.commands.SaveWorldCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends JavaPlugin {

    public static final String pluginPrefix = "WorldSaver";

    public static boolean isAutoSave = true;
    public static boolean isConsoleCopiedAllWorldsMessage = false;
    public static boolean allowSendPlayerMessages = true;
    public static int autoSaveInMinutes = 5;
    public static String permissionForWorldSaver = "worldsaver.saveworld";
    public static String noPermissionMessage = "§cYou have no rights to do this";
    public static String consoleErrorMessage = "You may only use this command as a player";
    public static String correctCommandUsageMessage = "§cPlease use §6/worldsaver save §7<§6world_name§7> §6or §7<§6all§7>";
    public static String playerCopiedAllWorldsMessage = "Saved all worlds";
    public static String playerCopiedAWorldMessage = "Saved ";
    public static String playerIsNotSuchWorld = "The specified world does not exists";
    public static String consoleCopiedAllWorldsMessage = "Saved all worlds automatically";

    private static Main plugin;
    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        loadConfigManager();

        getCommand("WorldSaver").setExecutor(new SaveWorldCommand());

        autoSaveScheduler();


    }

    public void loadConfigManager() {

        Path path = Paths.get("");
        String directoryName = path.toAbsolutePath().toString();

        File pluginFolder = new File(directoryName + "/plugins/" + pluginPrefix);
        File worldsFolder = new File(pluginFolder.getPath() + "/worlds");
        File configFile = new File(pluginFolder.getPath() + "/config.yml");

        if(!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }

        if(!worldsFolder.exists()) {
            worldsFolder.mkdir();
        }

        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
                FileConfiguration config = Main.getPlugin().getConfig();
                config.set("isAutoSave", isAutoSave);
                config.set("isConsoleCopiedAllWorldsMessage", isConsoleCopiedAllWorldsMessage);
                config.set("allowSendPlayerMessages", allowSendPlayerMessages);
                config.set("autoSaveInMinutes", autoSaveInMinutes);
                config.set("permissionForWorldSaver", permissionForWorldSaver);
                config.set("messages.noPermissionMessage", noPermissionMessage);
                config.set("messages.consoleErrorMessage", consoleErrorMessage);
                config.set("messages.correctCommandUsageMessage",correctCommandUsageMessage);
                config.set("messages.playerCopiedAllWorldsMessage", playerCopiedAllWorldsMessage);
                config.set("messages.playerCopiedAWorldMessage", playerCopiedAWorldMessage);
                config.set("messages.consoleCopiedAllWorldsMessage", consoleCopiedAllWorldsMessage);
                config.set("messages.playerIsNotSuchWorld", playerIsNotSuchWorld);
                Main.getPlugin().saveConfig();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isAutoSave = super.getConfig().getBoolean("isAutoSave");
        isConsoleCopiedAllWorldsMessage = super.getConfig().getBoolean("isConsoleCopiedAllWorldsMessage");
        allowSendPlayerMessages = super.getConfig().getBoolean("allowSendPlayerMessages");
        autoSaveInMinutes = super.getConfig().getInt("autoSaveInMinutes");
        permissionForWorldSaver = super.getConfig().getString("permissionForWorldSaver");
        noPermissionMessage = super.getConfig().getString("messages.noPermissionMessage");
        consoleErrorMessage = super.getConfig().getString("messages.consoleErrorMessage");
        correctCommandUsageMessage = super.getConfig().getString("messages.correctCommandUsageMessage");
        playerCopiedAllWorldsMessage = super.getConfig().getString("messages.playerCopiedAllWorldsMessage");
        playerCopiedAWorldMessage = super.getConfig().getString("messages.playerCopiedAWorldMessage");
        consoleCopiedAllWorldsMessage = super.getConfig().getString("messages.consoleCopiedAllWorldsMessage");
        playerIsNotSuchWorld = super.getConfig().getString("messages.playerIsNotSuchWorld");

    }

    public void autoSaveScheduler() {
        if(isAutoSave) {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.runTaskTimer(plugin, () -> {
                WorldSaver.copyAllWorlds();
                if(isConsoleCopiedAllWorldsMessage)
                    System.out.println(consoleCopiedAllWorldsMessage);
            }, 0, 20L * autoSaveInMinutes * 60);
        }
    }
}
