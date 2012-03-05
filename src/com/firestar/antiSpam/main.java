package com.firestar.antiSpam;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {

    private PermissionHandler Permissions = null;
    private static final Logger log = Logger.getLogger("Minecraft");
    private HashMap<String, Boolean> actionTaken = new HashMap<>();
    private chat playerListener = null;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        playerListener = new chat(this, Integer.valueOf(getConfig().getString("maxMessage")), Integer.valueOf(getConfig().getString("maxTime")));
        playerListener = new chat(this, 5, 2);
        PluginManager pm = getServer().getPluginManager();
        getServer().getPluginManager().registerEvents(playerListener, this);
        saveConfig();
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