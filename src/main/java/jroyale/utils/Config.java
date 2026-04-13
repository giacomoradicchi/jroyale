package jroyale.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jroyale.model.TroopStats;
import jroyale.utils.Enums.EntityType;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private static Config instance = null;

    private final ObjectMapper mapper = new ObjectMapper();
    private GameSettings settings;
    private final Map<EntityType, TroopStats> allTroops = new HashMap<>();

    private Config() {
        
        // builds path (works for every SO)
        Path configPath = Paths.get("conf", "settings.json"); // under /conf/settings.json (relative path)

        try {
            settings = mapper.readValue(configPath.toFile(), GameSettings.class);

        } catch (Exception e) {
            settings = new GameSettings();
            settings.setDifficulty("STANDARD");
            
            // TODO: load default json 
        } 

        //
        // game stats loading
        //

        /* 
        File folder = new File("conf/stats/");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));

        for (File file : listOfFiles) {
            TroopStats troop;
            try {
                troop = mapper.readValue(file, TroopStats.class);
                allTroops.put(file.getName(), troop);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }  */

        File dir = new File("conf/stats/");

        // loading file 
        loadTroopStats(dir, "minipekka.json", EntityType.MINIPEKKA);
        
    }

    public String getDifficulty() {
        return settings.getDifficulty();
    }

    public TroopStats getTroopStats(EntityType type) {
        TroopStats stats = allTroops.get(type);

        if (stats == null) throw new IllegalArgumentException("\"" + type + "\" not found.");

        return stats;
    }

    private void loadTroopStats(File dir, String name, EntityType type) {
        File file = new File(dir, name);
        if (!file.exists()) throw new IllegalArgumentException("File \"" + name + "\" in " + dir + "not found.");

        try {
            allTroops.put(type, mapper.readValue(file, TroopStats.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //---------------------------------------------------------------
	// STATIC METHODS
	//---------------------------------------------------------------
	public static Config getInstance() {
		if (instance == null)
			instance = new Config();
		return instance;
	}
}
