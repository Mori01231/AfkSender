package com.github.mori01231.afksender;

import net.ess3.api.events.AfkStatusChangeEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getPlayer;

public class AfkListener implements Listener {

    private AfkSender plugin;

    public AfkListener(AfkSender plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerAfk(AfkStatusChangeEvent event) {
        boolean hasGoneAfk = event.getValue();

        // check if the player has gone afk or is returning from afk
        if(!hasGoneAfk)
            return;

        String afkServerName = AfkSender.getInstance().getConfig().getString("afk-server-name");
        String playerMessage = ChatColor.translateAlternateColorCodes('&',AfkSender.getInstance().getConfig().getString("afk-server-send-message"));
        String ignoreAfkMessage = ChatColor.translateAlternateColorCodes('&',AfkSender.getInstance().getConfig().getString("ignore-afk-message"));

        // define player
        Player player;

        // get player
        try{
            player = getPlayer(event.getAffected().getName());
        }catch(Exception e){
            // guess it wasn't a player?
            event.getAffected().sendMessage("You aren't a player.");
            return;
        }

        // check if the player has permission to ignore afk kick
        try{
            if(player.hasPermission("afksender.ignore")){
                player.sendMessage(ignoreAfkMessage);
                return;
            }
        }catch (Exception NullPointerException){
            getLogger().info("afksender failed to send " + event.getAffected().getName() + " to afk server. " +
                    "NullPointerException while getting player permission.");
        }


        // create bytearray for sending player to server
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(afkServerName);
        } catch (IOException e) {
            getLogger().info("afksender failed to send " + event.getAffected().getName() + " to afk server. " +
                    "IOException while creating bytearray to send player to afk server.");
        }

        // Format player message
        playerMessage = StringUtils.replace(playerMessage, "%SERVERNAME%", afkServerName);
        player.sendMessage(playerMessage);

        // send player to server
        player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());

    }
}
