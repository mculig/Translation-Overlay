package org.translationoverlay.config;

import java.io.FileNotFoundException;
import java.io.FileReader;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ConfigLoader {
	
	private String configPath = "./config/config.json";
	private Config config;
	
	public ConfigLoader(){
			Gson gson = new Gson();
			FileReader fileReader;
			try {
				fileReader = new FileReader(configPath);
				JsonReader jsonReader = new JsonReader(fileReader);
				config=gson.fromJson(jsonReader, Config.class);
			} catch (FileNotFoundException e) {
				System.out.println("FATAL: Failed to load config file! Exiting!");
				System.exit(1);
			}
	}
	
	public Config getConfig(){
		return this.config;
	}

}
