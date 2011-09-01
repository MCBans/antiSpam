package com.firestar.antiSpam;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;

import com.mcbans.firestar.mcbans.bukkitInterface;
import com.mcbans.firestar.mcbans.pluginInterface.ban;


public class chat extends PlayerListener {
	private HashMap<String, ArrayList<Long>> chatLastSent = new HashMap<String, ArrayList<Long>>();
	private int maxMSG = 5;
	private int maxTM = 4;
	private main plugin = null;
	private bukkitInterface mcb = null;
	public chat( main d, int messageC, int maxTime ){
		maxMSG = messageC;
		maxTM = maxTime*1000;
		plugin = d;
		setupMCBans();
	}
	public void setupMCBans() {
		Plugin test = plugin.getServer().getPluginManager().getPlugin("mcbans");
		if(mcb == null) {
		    if(test != null) {
		    	mcb = ((bukkitInterface)test);
		    	plugin.Message("Found mcbans, enabling global ban!");
		    } else {
		    }
		}
	}
	public void onPlayerChat(PlayerChatEvent event){
		long timeInMillis = System.currentTimeMillis();
		if(!plugin.hasPerm(event.getPlayer())){
			if(chatLastSent.containsKey(event.getPlayer().getName())){
				ArrayList<Long> g = new ArrayList<Long>();
				int tmpderp = 1;
				for( Long tmp : chatLastSent.get(event.getPlayer().getName())){
					if((maxTM+tmp)>timeInMillis){
						tmpderp++;
						g.add(tmp);
					}else{
					}
				}
				if(tmpderp>=maxMSG){
					if(mcb!=null){
						ban Ban = new ban(mcb, "globalBan", event.getPlayer().getName(), event.getPlayer().getAddress().getAddress().getHostAddress(), "[antiSpam]", "spamBot", "", "");
						Ban.start();
					}else{
						event.getPlayer().kickPlayer("Stop spamming!");
					}
					event.setCancelled(true);
				}
				g.add(timeInMillis);
				chatLastSent.put(event.getPlayer().getName(), g);
			}else{
				ArrayList<Long> g = new ArrayList<Long>();
				g.add(timeInMillis);
				chatLastSent.put(event.getPlayer().getName(), g);
			}
		}
	}
}