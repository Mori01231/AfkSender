package com.github.mori01231.afksender;

import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
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
        if(!hasGoneAfk){
            // debugging message
            //event.getAffected().sendMessage("You left afk no kick");
            return;
        }

        Player player;

        try{
            player = getPlayer(event.getAffected().getName());
        }catch(Exception e){
            // guess it wasn't a player?
            event.getAffected().sendMessage("You aren't a player.");
            return;
        }

        // check if the player should ignore afk kick
        try{
            if(player.hasPermission("afksender.ignore") || player.isOp()){
                player.sendMessage("Ignored afk kick");
                return;
            }
        }catch (Exception NullPointerException){
            getLogger().info("afksender failed to send " + event.getAffected().getName() + " to afk server.");
        }


        // create bytearray for sending player to server
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF("lifeafk");
        } catch (IOException e) {
            player.sendMessage("Error sending to afk server.");
        }

        player.sendMessage("Sending to afk server");

        // send player to server
        player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());

    }
}
