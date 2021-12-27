package de.lojaw.commands;

import de.lojaw.main.Main;
import de.lojaw.main.WorldSaver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveWorldCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(p.hasPermission(Main.permissionForWorldSaver)) {
                if(args.length == 2) {
                    if(args[0].equals("save")) {
                        if(args[1].equals("all")) {
                            WorldSaver.copyAllWorlds();
                            if(Main.allowSendPlayerMessages)
                                p.sendMessage(Main.playerCopiedAllWorldsMessage);
                        } else {
                            String worldName = args[1];
                            if(WorldSaver.isExist(worldName)) {
                                WorldSaver.copyWorld(worldName);
                                if(Main.allowSendPlayerMessages)
                                    p.sendMessage(Main.playerCopiedAWorldMessage + worldName);
                            } else
                                p.sendMessage(Main.playerIsNotSuchWorld);
                        }
                    } else
                        p.sendMessage(Main.correctCommandUsageMessage);
                } else
                    p.sendMessage(Main.correctCommandUsageMessage);
            } else
                p.sendMessage(Main.noPermissionMessage);
        } else
            sender.sendMessage(Main.consoleErrorMessage);
        return false;
    }
}
