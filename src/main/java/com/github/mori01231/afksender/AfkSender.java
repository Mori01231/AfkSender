package com.github.mori01231.afksender;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AfkSender extends JavaPlugin {

    private static AfkSender instance;

    public AfkSender (){
        instance = this;
    }

    public static AfkSender getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Enabling AfkSender...");

        this.getCommand("afksender").setExecutor(new ReloadCommandExecutor(this));

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.saveDefaultConfig();

        registerEvents();
        getLogger().info("Enabled AfkSender");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents(){

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new AfkListener(this),  this);
    }
}
