package com.firestar.antiSpam;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Settings{
	private YamlConfiguration config;
	public boolean exists = false;
	
	public Settings(){
		File plugin_settings = new File("plugins/antispam/settings.yml");
		if (!plugin_settings.exists()) {
			System.out.print("[antispam] settings.yml not found, generating default..");
			this.generate();
			plugin_settings = new File("plugins/antispam/settings.yml");
			config = YamlConfiguration.loadConfiguration(plugin_settings);
		} else {
			config = YamlConfiguration.loadConfiguration(plugin_settings);
		}
	}
	public void generate(){
		InputStream in = null;
		try {
			in = Settings.class.getClassLoader().getResourceAsStream("defaults/settings.yml");
			File file = new File("plugins/antispam/");
			if(!file.exists()){
				file.mkdir();
			}
			file = new File("plugins/antispam/settings.yml"); 
			OutputStream out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024];
		 
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			in.close();
			out.flush();
			out.close();
		}
		catch(IOException e) {
			System.err.println("[antispam] Error writing settings file.");
		}
	}
	public void reload() {
		File plugin_settings = new File("plugins/antispam/settings.yml");
		if (!plugin_settings.exists()) {
		} else {
			config = YamlConfiguration.loadConfiguration(plugin_settings);
		}
	}
	public String getString( String variable ){
		return config.getString( variable, "" );
	}
	public String getPrefix ( ) {
		return config.get("prefix", "").toString();
	}
	public Integer getInteger( String variable ){
		return config.getInt( variable, 0 );
	}
	public boolean getBoolean( String variable ){
		return config.getBoolean( variable, true );
	}
	public double getDouble( String variable ){
		return config.getDouble( variable, 1.00 );
	}
	public float getFloat( String variable ){
		return Float.valueOf(config.getString(variable, "0.00" ));
	}
}