package jroyale.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jroyale.model.cards.CardStats;
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
    private final Map<EntityType, CardStats> allCardStats = new HashMap<>();

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

        File dir = new File("conf/stats/");

        // loading file 
        loadCardStats(dir, "minipekka.json", EntityType.MINIPEKKA);
        loadCardStats(dir, "giant.json", EntityType.GIANT);
        loadCardStats(dir, "valkyrie.json", EntityType.VALKYRIE);
        loadCardStats(dir, "pekka.json", EntityType.PEKKA);
        loadCardStats(dir, "skeletons.json", EntityType.SKELETONS);
        loadCardStats(dir, "skeleton_army.json", EntityType.SKELETON_ARMY);
        
    }

    public String getDifficulty() {
        return settings.getDifficulty();
    }

    private void loadCardStats(File dir, String name, EntityType type) {
        File file = new File(dir, name);
        if (!file.exists()) throw new IllegalArgumentException("File \"" + name + "\" in " + dir + "not found.");

        try {
            allCardStats.put(type, mapper.readValue(file, CardStats.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CardStats getCardStats(EntityType type) {
        CardStats stats = allCardStats.get(type);

        if (stats == null) throw new IllegalArgumentException("\"" + type + "\" not found.");

        return stats;
    }
    
    // static methods

	public static Config getInstance() {
		if (instance == null)
			instance = new Config();
		return instance;
	}
}
