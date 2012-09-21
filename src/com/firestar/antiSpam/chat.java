package com.firestar.antiSpam;

import com.mcbans.firestar.mcbans.BukkitInterface;
import com.mcbans.firestar.mcbans.org.json.JSONException;
import com.mcbans.firestar.mcbans.org.json.JSONObject;
import com.mcbans.firestar.mcbans.pluginInterface.Ban;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class chat
        implements Listener {

    private HashMap<String, ArrayList<Long>> chatLastSent = new HashMap<String, ArrayList<Long>>();
    private HashMap<String, ArrayList<Long>> commandLastSent = new HashMap<String, ArrayList<Long>>();
    private int maxMSG = 5;
    private int maxTM = 4;
    private int maxCom = 5;
    private int maxCTM = 3;
    private List<String> ignoreCom;
    private main pluginMain = null;
    private String Duration = null;
    private String Measure = null;
    private int act = 1; // DEFAULT KICK
    private main plugin = null;
    private BukkitInterface mcb = null;
    public chat(
    		main d,
    		int messageC,
    		int maxTime,
    		int commandC,
    		int maxCTime,
    		List<String> ignoreC,
    		String duration,
    		String measure,
    		int action
    	){
    	pluginMain = d;
        this.maxMSG = messageC;
        this.maxTM = (maxTime * 1000);
        this.maxCom = commandC;
        this.maxCTM = (maxCTime * 1000);
        this.ignoreCom = ignoreC;
        this.act = action;
        this.Duration = duration;
        this.Measure = measure;
        this.plugin = d;
        setupMCBans();
    }

    public void setupMCBans() {
        Plugin test = this.plugin.getServer().getPluginManager().getPlugin("mcbans");
        if ((this.mcb == null) && (test != null)) {
        	if(test.isEnabled()){
        		this.mcb = ((BukkitInterface) test);
        		this.plugin.message("Found mcbans, enabling mcbans!");
        	}
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	plugin.setAction(event.getPlayer().getName(), false);
    	chatLastSent.remove(event.getPlayer().getName());
    	commandLastSent.remove(event.getPlayer().getName());
    }
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        long timeInMillis = System.currentTimeMillis();
        if (!plugin.hasPerm(event.getPlayer())) {
            for (String ic : ignoreCom){
                if(event.getMessage().startsWith(ic)){ // check ignore
                    return;
                }
            }
            if (commandLastSent.containsKey(event.getPlayer().getName())) {
                ArrayList<Long> g = new ArrayList<Long>();
                int tmpderp = 1;
                for (Long tmp : commandLastSent.get(event.getPlayer().getName())) {
                    if ((maxCTM + tmp) > timeInMillis) {
                        tmpderp++;
                        g.add(tmp);
                    }
                }
                if (tmpderp >= maxCom) {
                    if (!plugin.getAction(event.getPlayer().getName())) {
                    	this.takeAction(event.getPlayer(),1);
                        plugin.setAction(event.getPlayer().getName(), true);
                    }
                    event.setCancelled(true);
                }
                g.add(timeInMillis);
                commandLastSent.put(event.getPlayer().getName(), g);
            } else {
                ArrayList<Long> g = new ArrayList<Long>();
                g.add(timeInMillis);
                commandLastSent.put(event.getPlayer().getName(), g);
            }
        }
    }
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        long timeInMillis = System.currentTimeMillis();
        if (!plugin.hasPerm(event.getPlayer())) {
            if (chatLastSent.containsKey(event.getPlayer().getName())) {
                ArrayList<Long> g = new ArrayList<Long>();
                int tmpderp = 1;
                for (Long tmp : chatLastSent.get(event.getPlayer().getName())) {
                    if ((maxTM + tmp) > timeInMillis) {
                        tmpderp++;
                        g.add(tmp);
                    }
                }
                if (tmpderp >= maxMSG) {
                    if (!plugin.getAction(event.getPlayer().getName())) {
                        this.takeAction(event.getPlayer(),2);
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
    public void takeAction(Player player, int actT){
    	switch(act){
    		case 1:
    			player.kickPlayer(pluginMain.settings.getString("kickMessage"));
    		break;
    		case 2: // Use bukkit banning
    			player.setBanned(true);
				player.kickPlayer(pluginMain.settings.getString("localBanMessage"));
    		break;
    		case 3:
    			if(mcb!=null){
    				Ban ban = new Ban(mcb, "localBan", player.getName(), player.getAddress().getAddress().getHostAddress(), "[antiSpam]", pluginMain.settings.getString("localBanMessage"), "", "");
    				(new Thread(ban)).start();
    			}else{
    				System.out.println("[AntiSpam] Error, MCbans not found. Option 3 cannot be used, defaulting to Bukkit bans");
    				player.setBanned(true);
    				player.kickPlayer(pluginMain.settings.getString("localBanMessage"));
    			}
    		break;
    		case 4:
    			if(mcb!=null){
    				JSONObject actionCause = new JSONObject();
    		    	switch(actT){
    		    		case 1:
    		    			int offx = 0;
    		    			for(long h : commandLastSent.get(player.getName())){
    		    				try {
									actionCause.put(String.valueOf(offx), h);
								} catch (JSONException e) {
									e.printStackTrace();
								}
    		    				offx++;
    		    			}
    		    		break;
    		    		case 2:
    		    			int offxI = 0;
    		    			for(long h : chatLastSent.get(player.getName())){
    		    				try {
									actionCause.put(String.valueOf(offxI), h);
								} catch (JSONException e) {
									e.printStackTrace();
								}
    		    				offxI++;
    		    			}
    		    		break;
    		    	}
	    			Ban ban = new Ban(mcb, "globalBan", player.getName(), player.getAddress().getAddress().getHostAddress(), "[antiSpam]", "spambot", "", "", actionCause, false);
	    			(new Thread(ban)).start();
		    	}else{
					System.out.println("[AntiSpam] Error, MCbans not found. Option 4 cannot be used, defaulting to Bukkit bans");
					player.setBanned(true);
					player.kickPlayer(pluginMain.settings.getString("localBanMessage"));
				}
    		break;
    		case 5:
    			if(mcb!=null){
	    			Ban ban = new Ban(mcb, "tempBan", player.getName(), player.getAddress().getAddress().getHostAddress(), "[antiSpam]", pluginMain.settings.getString("tempBanMessage"), Duration, Measure);
	    			(new Thread(ban)).start();
	    		}else{
    				System.out.println("[AntiSpam] Error, MCbans not found. Option 5 cannot be used, defaulting to Bukkit bans");
    				player.setBanned(true);
    				player.kickPlayer(pluginMain.settings.getString("localBanMessage"));
    			}
    		break;
    		default:
    			System.out.println("[AntiSpam] Error, "+act+" is not an option.");
    		break;
    	}
    }
}