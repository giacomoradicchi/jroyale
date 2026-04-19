package jroyale.model.cards;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public abstract class Card {
    
    /* private String name;
    private Speed speed;
    private MeleeRange meleeRange;
    private double collisionRadius;
    private double loadTime;
    private int hitPoints;
    private int damage;
    private byte elixirCost;
    private final EntityType TYPE; */
    protected CardStats stats;
    private final EntityType TYPE;

    protected Card(EntityType type) {
        this.TYPE = type;
    }

    // Setters

    /* public void setStats(CardStats stats) {
        setName(stats.getName());
        setSpeed(stats.getSpeed());
        setMeleeRange(stats.getMeleeRange());
        setCollisionRadius(stats.getCollisionRadius());
        setLoadTime(stats.getLoadTime());
        setHitPoints(stats.getHitPoints());
        setDamage(stats.getDamage());
        setElixirCost(stats.getElixirCost());
    } */

    public void setStats(CardStats stats) {
        this.stats = stats;
    }

    /* public void setName(String name) {
        this.name = name;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
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
 */
    // Getters

    public CardStats getCardStats() {
        return stats;
    }

    /* public String getName() {
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
    } */

    public EntityType getType() {
        return TYPE;
    }

    // abstract methods

    public abstract void dropCardIntoModel(int rowIndex, int columnIndex, Side side);

}
