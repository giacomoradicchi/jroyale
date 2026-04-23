package jroyale.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameSettings {

    private static final int MIN_TIME_SEC = 0;
    
    @JsonProperty("difficulty")
    private String difficulty;

    @JsonProperty("maxTimeSec")
    private int maxTimeSec;

    public GameSettings() {}

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        switch (difficulty.strip().toUpperCase()) {
            case "BASIC":
                this.difficulty = "BASIC";
                break;
            case "STANDARD":
                this.difficulty = "STANDARD";
                break;
            case "EXPERT":
                this.difficulty = "EXPERT";
                break;
            case "MASTER":
                this.difficulty = "MASTER";
                break;
        
            default:
                return; // don't change anything if not valid
        }
        
    }

    public int getMaxTimeSec() {
        return maxTimeSec;
    }

    public void setMaxTime(int maxTimeSec) {
        if (maxTimeSec >= MIN_TIME_SEC) // cannot be less than MIN_TIME_SEC
            this.maxTimeSec = maxTimeSec;
    }
}