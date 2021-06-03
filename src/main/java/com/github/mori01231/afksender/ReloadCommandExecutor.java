package com.github.mori01231.afksender;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommandExecutor implements CommandExecutor {

    private AfkSender plugin;

    public ReloadCommandExecutor(AfkSender plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("afksender.reload")){
            sender.sendMessage("You do not have permissions for this command.");
            return true;
        }


        if(args[0].equalsIgnoreCase("reload")){
            try{
                AfkSender.getInstance().reloadConfig();
                sender.sendMessage("Reloaded config.");
                return true;
            }catch(Exception e){
                sender.sendMessage("Could not reload config.");
            }
        }

        sender.sendMessage("That command does not exist.");
        return true;
    }
}
