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
    private HashMap<String, Boolean> actionTaken = new HashMap();
    private chat playerListener = null;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        this.playerListener = new chat(this, Integer.valueOf(getConfig().getString("maxMessage")).intValue(), Integer.valueOf(getConfig().getString("maxTime")).intValue());
        this.playerListener = new chat(this, 5, 2);
        PluginManager pm = getServer().getPluginManager();
        getServer().getPluginManager().registerEvents(this.playerListener, this);
        saveConfig();
    }

    public void setupPermissions() {
        Plugin test = getServer().getPluginManager().getPlugin("Permissions");
        if ((this.Permissions == null)
                && (test != null)) {
            this.Permissions = ((Permissions) test).getHandler();
            message("Found Permission Bridge!");
        }
    }

    public boolean getAction(String player) {
        if (this.actionTaken.containsKey(player)) {
            return this.actionTaken.containsKey(player);
        }
        return false;
    }

    public void setAction(String player, Boolean set) {
        this.actionTaken.put(player, set);
    }

    public boolean hasPerm(Player player) {
        if (this.Permissions == null) {
            return player.hasPermission("antiSpam");
        }

        return this.Permissions.has(player, "antiSpam");
    }

    public void message(String msg) {
        log.info("antiSpam: " + msg);
    }
}