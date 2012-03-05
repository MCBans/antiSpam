package com.firestar.antiSpam;

import com.mcbans.firestar.mcbans.BukkitInterface;
import com.mcbans.firestar.mcbans.pluginInterface.Ban;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class chat
        implements Listener {

    private HashMap<String, ArrayList<Long>> chatLastSent = new HashMap();
    private int maxMSG = 5;
    private int maxTM = 4;
    private main plugin = null;
    private BukkitInterface mcb = null;

    public chat(main d, int messageC, int maxTime) {
        this.maxMSG = messageC;
        this.maxTM = (maxTime * 1000);
        this.plugin = d;
        setupMCBans();
    }

    public void setupMCBans() {
        Plugin test = this.plugin.getServer().getPluginManager().getPlugin("mcbans");
        if ((this.mcb == null)
                && (test != null)) {
            this.mcb = ((BukkitInterface) test);
            this.plugin.message("Found mcbans, enabling global ban!");
        }
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        long timeInMillis = System.currentTimeMillis();
        if (!plugin.hasPerm(event.getPlayer())) {
            if (chatLastSent.containsKey(event.getPlayer().getName())) {
                ArrayList<Long> g = new ArrayList<>();
                int tmpderp = 1;
                for (Long tmp : chatLastSent.get(event.getPlayer().getName())) {
                    if ((maxTM + tmp) > timeInMillis) {
                        tmpderp++;
                        g.add(tmp);
                    }
                }
                if (tmpderp >= maxMSG) {
                    if (!plugin.getAction(event.getPlayer().getName())) {
                        if (mcb != null) {
                            Ban ban = new Ban(mcb, "globalBan", event.getPlayer().getName(), event.getPlayer().getAddress().getAddress().getHostAddress(), "[antiSpam]", "spambot", "", "");
                            ban.start();
                        } else {
                            event.getPlayer().kickPlayer("Stop spamming!");
                        }
                        plugin.setAction(event.getPlayer().getName(), true);
                    }
                    event.setCancelled(true);
                }
                g.add(timeInMillis);
                chatLastSent.put(event.getPlayer().getName(), g);
            } else {
                ArrayList<Long> g = new ArrayList<>();
                g.add(timeInMillis);
                chatLastSent.put(event.getPlayer().getName(), g);
            }
        }
    }
}