package jroyale.utils;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jroyale.model.cards.CardStats;
import jroyale.utils.Enums.EntityType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private static Config instance = null;

    private final ObjectMapper mapper = new ObjectMapper();
    private GameSettings settings;
    private final Map<EntityType, CardStats> allCardStats = new HashMap<>();

    private Config() {
        loadSettings();
        loadAllCardsStats();
    }

    public String getDifficulty() {
        return settings.getDifficulty();
    }

    public void setDifficulty(String difficulty) {
        
        settings.setDifficulty(difficulty);
        System.out.println("new difficulty: " + getDifficulty());
    }

    public int getMaxTimeSec() {
        return settings.getMaxTimeSec();
    }

    private void loadSettings() {
        // 1. loading default settings

        
        try {
            // getResourceAsStream uses "/" as separator no matter the SO
            settings = mapper.readValue(this.getClass().getResourceAsStream("/jroyale/conf/default_settings.json"), GameSettings.class);
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. loading (eventual) user settings

        // works for every SO
        File userSettingsFile = Paths.get("conf", "settings.json").toFile();

        try {
            // overwrites it if correct
            settings = mapper.readValue(userSettingsFile, GameSettings.class);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    private void loadAllCardsStats() {
        File dir = Paths.get("conf", "stats").toFile();

        // loading stats
        loadCardStats(dir, "minipekka.json", EntityType.MINIPEKKA);
        loadCardStats(dir, "giant.json", EntityType.GIANT);
        loadCardStats(dir, "valkyrie.json", EntityType.VALKYRIE);
        loadCardStats(dir, "pekka.json", EntityType.PEKKA);
        loadCardStats(dir, "skeletons.json", EntityType.SKELETONS);
        loadCardStats(dir, "skeleton_army.json", EntityType.SKELETON_ARMY);
    }

    private void loadCardStats(File dir, String name, EntityType type) {
        
        CardStats stats = null;

        // 1. loading default stats
 
        
        try {
            // getResourceAsStream uses "/" as separator no matter the SO
            stats = mapper.readValue(this.getClass().getResourceAsStream("/jroyale/conf/default_stats/" + name), CardStats.class);
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. loading user stats
        File file = new File(dir, name);
        if (!file.exists()) throw new IllegalArgumentException("File \"" + name + "\" in " + dir + "not found.");

        
        try {
            CardStats userStats = mapper.readValue(file, CardStats.class);

            // overwrites only editable stats
            stats.setSpeed(userStats.getSpeed());
            stats.setMeleeRange(userStats.getMeleeRange());
            stats.setLoadTime(userStats.getLoadTime());
            stats.setHitPoints(userStats.getHitPoints());
            stats.setDamage(userStats.getDamage());
            stats.setElixirCost(userStats.getElixirCost());

        } catch (Exception e) {
            e.printStackTrace();
        }

        allCardStats.put(type, stats);
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
