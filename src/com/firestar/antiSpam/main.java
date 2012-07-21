package com.firestar.antiSpam;

import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class main extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    
	private HashMap<String, Boolean> actionTaken = new HashMap<String, Boolean>();
    private chat playerListener = null;
    public Settings settings = null;
    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
    	settings = new Settings();
        this.playerListener = new chat(
    		this, 
    		settings.getInteger("maxChatMessages"),
    		settings.getInteger("maxMessageTime"),
    		settings.getInteger("maxCommands"),
    		settings.getInteger("maxCommandTime"),
    		settings.getString("tempBanDuration"),
    		settings.getString("tempBanMeasure"),
    		settings.getInteger("actionTake")
        );
        getServer().getPluginManager().registerEvents(this.playerListener, this);
    }

    public boolean getAction(String player) {
        if (this.actionTaken.containsKey(player)) {
            return this.actionTaken.get(player);
        }
        return false;
    }

    public void setAction(String player, Boolean set) {
        this.actionTaken.put(player, set);
    }

    public boolean hasPerm(Player player) {
        return player.hasPermission("antispam.ignore");
    }

    public void message(String msg) {
        log.info("[antispam] " + msg);
    }
}