package com.firestar.antiSpam;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class main extends JavaPlugin {

	private PermissionHandler Permissions = null;
	private static final Logger log = Logger.getLogger("Minecraft");
	private HashMap<String, Boolean> actionTaken = new HashMap<String, Boolean>();
	private chat playerListener = null;

	public void onDisable() {
		message("Disabled antiSpam");
	}

	public void onEnable() {
		message("Enabled antiSpam");
		File fileSettings = new File("plugins/antiSpam/settings.yml");
		Configuration main_config = null;
		if (fileSettings.exists()) {
			message("Configuration File Found!");
			main_config = new Configuration(fileSettings);
			main_config.load();
			playerListener = new chat(this, Integer.valueOf(main_config.getString("maxMessage")), Integer.valueOf(main_config.getString("maxTime")));
		} else {
			message("No Configuration Detected, Default Settings!");
			playerListener = new chat(this, 4, 2);
		}
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Highest, this);
		setupPermissions();
	}

	public void setupPermissions() {
		Plugin test = getServer().getPluginManager().getPlugin("Permissions");
		if (Permissions == null) {
			if (test != null) {
				Permissions = ((Permissions) test).getHandler();
				message("Found Permission Bridge!");
			} else {
			}
		}
	}

	public boolean getAction(String player) {
		if (actionTaken.containsKey(player)) {
			return actionTaken.containsKey(player);
		}
		return false;
	}

	public void setAction(String player, Boolean set) {
		actionTaken.put(player, set);
	}

	public boolean hasPerm(Player player) {
		if (Permissions == null) {
			if (player.hasPermission("antiSpam")) {
				return true;
			} else {
				return false;
			}
		} else {
			if (Permissions.has(player, "antiSpam")) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void message(String msg) {
		log.info("antiSpam: " + msg);
	}
}
