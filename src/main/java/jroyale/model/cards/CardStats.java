package jroyale.model.cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jroyale.model.troops.Troop.MeleeRange;
import jroyale.model.troops.Troop.Speed;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardStats {
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("speed")
    private Speed speed;

    @JsonProperty("meleeRange")
    private MeleeRange meleeRange;

    @JsonProperty("collisionRadius")
    private double collisionRadius;

    @JsonProperty("loadTime")
    private double loadTime;

    @JsonProperty("hitPoints")
    private int hitPoints;

    @JsonProperty("damage")
    private int damage;

    @JsonProperty("elixirCost")
    private byte elixirCost;

    private static final byte MIN_UNITS_AMOUNT = 1; // at least one entity has to be dropped.
    private static final byte MAX_UNITS_AMOUNT = Byte.MAX_VALUE;
    @JsonProperty("units_amount")
    private byte unitsAmount = MIN_UNITS_AMOUNT;

    @JsonProperty("mass")
    private double mass;

    public CardStats() {}

    public String getName() {
        return name;
    }

    public Speed getSpeed() {
        return speed;
    }
    
    public MeleeRange getMeleeRange() {
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

    public byte getElixirCost() {
        return elixirCost;
    }

    public byte getUnitsAmount() {
        return unitsAmount;
    }

    public double getMass() {
        return mass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeed(String speed) {
        if (speed == null) this.speed = Speed.MEDIUM; // default
        
        try {
            this.speed = Speed.valueOf(speed.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            this.speed = Speed.MEDIUM; // default
        }
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }
    
    public void setMeleeRange(String meleeRange) {
        if (meleeRange == null) this.meleeRange = MeleeRange.MEDIUM; // default
        
        try {
            this.meleeRange = MeleeRange.valueOf(meleeRange.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            this.meleeRange = MeleeRange.MEDIUM; // default
        }
    }

    public void setMeleeRange(MeleeRange meleeRange) {
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

    public void setElixirCost(byte elixirCost) {
        this.elixirCost = elixirCost;
    }

    public void setUnitsAmount(int unitsAmount) {
        this.unitsAmount = (byte) Math.clamp(unitsAmount, MIN_UNITS_AMOUNT, MAX_UNITS_AMOUNT); 
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
}