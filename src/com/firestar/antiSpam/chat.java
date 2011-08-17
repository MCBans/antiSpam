package com.firestar.antiSpam;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;


public class chat extends PlayerListener {
	private HashMap<String, ArrayList<Long>> chatLastSent = new HashMap<String, ArrayList<Long>>();
	private int maxMSG = 5;
	private int maxTM = 4;
	public chat( int messageC, int maxTime ){
		maxMSG = messageC;
		maxTM = maxTime*1000;
	}
	public void onPlayerChat(PlayerChatEvent event){
		long timeInMillis = System.currentTimeMillis();
		if(chatLastSent.containsKey(event.getPlayer().getName())){
			ArrayList<Long> g = new ArrayList<Long>();
			int tmpderp = 1;
			for( Long tmp : chatLastSent.get(event.getPlayer().getName())){
				if((maxTM+tmp)>timeInMillis){
					
				}else{
					tmpderp++;
					g.add(tmp);
				}
			}
			if(tmpderp>=maxMSG){
				event.getPlayer().kickPlayer("Stop spamming!");
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