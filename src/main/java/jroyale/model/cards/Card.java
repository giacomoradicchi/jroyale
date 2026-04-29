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

    public void setStats(CardStats stats) {
        this.stats = stats;
    }

    // Getters

    public CardStats getCardStats() {
        return stats;
    }

    public EntityType getType() {
        return TYPE;
    }

    // abstract methods

    public abstract void dropCardIntoModel(int rowIndex, int columnIndex, Side side);

}
