package com.firestar.antiSpam;

import com.mcbans.firestar.mcbans.BukkitInterface;
import com.mcbans.firestar.mcbans.pluginInterface.Ban;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class chat
  implements Listener
{
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
    if ((this.mcb == null) && 
      (test != null)) {
      this.mcb = ((BukkitInterface)test);
      this.plugin.message("Found mcbans, enabling global ban!");
    }
  }

  @EventHandler
  public void onPlayerChat(PlayerChatEvent event) {
    long timeInMillis = System.currentTimeMillis();
    if (!this.plugin.hasPerm(event.getPlayer()))
      if (this.chatLastSent.containsKey(event.getPlayer().getName())) {
        ArrayList g = new ArrayList();
        int tmpderp = 1;
        for (Long tmp : (ArrayList)this.chatLastSent.get(event.getPlayer().getName())) {
          if (this.maxTM + tmp.longValue() > timeInMillis) {
            tmpderp++;
            g.add(tmp);
          }
        }
        if (tmpderp >= this.maxMSG) {
          if (!this.plugin.getAction(event.getPlayer().getName())) {
            if (this.mcb != null) {
              Ban ban = new Ban(this.mcb, "globalBan", event.getPlayer().getName(), event.getPlayer().getAddress().getAddress().getHostAddress(), "[antiSpam]", "spambot", "", "");
              ban.start();
            } else {
              event.getPlayer().kickPlayer("Stop spamming!");
            }
            this.plugin.setAction(event.getPlayer().getName(), Boolean.valueOf(true));
          }
          event.setCancelled(true);
        }
        g.add(Long.valueOf(timeInMillis));
        this.chatLastSent.put(event.getPlayer().getName(), g);
      } else {
        ArrayList g = new ArrayList();
        g.add(Long.valueOf(timeInMillis));
        this.chatLastSent.put(event.getPlayer().getName(), g);
      }
  }
}