package com.firestar.antiSpam;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class main extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");

    private ConcurrentHashMap<String, Boolean> actionTaken = new ConcurrentHashMap<String, Boolean>();
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
                settings.getStringList("ignoreCommands"),
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

    public boolean hasPermIgnoreChat(Player player) {
        return (player.hasPermission("antispam.ignore") || player.hasPermission("antispam.ignore.chat"));
    }

    public boolean hasPermIgnoreCmd(Player player) {
        return (player.hasPermission("antispam.ignore") || player.hasPermission("antispam.ignore.command"));
    }

    public void message(String msg) {
        log.info("[antispam] " + msg);
    }
}