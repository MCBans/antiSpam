package com.firestar.antiSpam;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;

import com.mcbans.firestar.mcbans.BukkitInterface;
import com.mcbans.firestar.mcbans.pluginInterface.Ban;

public class chat extends PlayerListener {

	private HashMap<String, ArrayList<Long>> chatLastSent = new HashMap<String, ArrayList<Long>>();
	private int maxMSG = 5;
	private int maxTM = 4;
	private main plugin = null;
	private BukkitInterface mcb = null;

	public chat(main d, int messageC, int maxTime) {
		maxMSG = messageC;
		maxTM = maxTime * 1000;
		plugin = d;
		setupMCBans();
	}

	public void setupMCBans() {
		Plugin test = plugin.getServer().getPluginManager().getPlugin("mcbans");
		if (mcb == null) {
			if (test != null) {
				mcb = ((BukkitInterface) test);
				plugin.message("Found mcbans, enabling global ban!");
			}
		}
	}

	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		long timeInMillis = System.currentTimeMillis();
		if (!plugin.hasPerm(event.getPlayer())) {
			if (chatLastSent.containsKey(event.getPlayer().getName())) {
				ArrayList<Long> g = new ArrayList<Long>();
				int tmpderp = 1;
				for (Long tmp : chatLastSent.get(event.getPlayer().getName())) {
					if ((maxTM + tmp) > timeInMillis) {
						tmpderp++;
						g.add(tmp);
					} else {
					}
				}
				if (tmpderp >= maxMSG) {
					if (!plugin.getAction(event.getPlayer().getName())) {
						if (mcb != null) {
							Ban ban = new Ban(mcb, "globalBan", event.getPlayer().getName(), event.getPlayer().getAddress().getAddress().getHostAddress(), "[antiSpam]", "spamBot", "", "");
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
				ArrayList<Long> g = new ArrayList<Long>();
				g.add(timeInMillis);
				chatLastSent.put(event.getPlayer().getName(), g);
			}
		}
	}
}
