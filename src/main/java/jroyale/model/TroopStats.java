package jroyale.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TroopStats {
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("speed")
    private String speed;

    @JsonProperty("meleeRange")
    private String meleeRange;

    @JsonProperty("collisionRadius")
    private double collisionRadius;

    @JsonProperty("loadTime")
    private double loadTime;

    @JsonProperty("hitPoints")
    private int hitPoints;

    @JsonProperty("damage")
    private int damage;

    public TroopStats() {}

    public String getName() {
        return name;
    }

    public String getSpeed() {
        return speed;
    }
    
    public String getMeleeRange() {
        return meleeRange;
    }

    public double getCollisionRadius() {
        return collisionRadius;
    }

    public double getLoadTime() {
        return loadTime;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getDamage() {
        return damage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
    
    public void setMeleeRange(String meleeRange) {
        this.meleeRange = meleeRange;
    }

    public void setCollisionRadius(double collisionRadius) {
        this.collisionRadius = collisionRadius;
    }

    public void setLoadTime(double loadTime) {
        this.loadTime = loadTime;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}