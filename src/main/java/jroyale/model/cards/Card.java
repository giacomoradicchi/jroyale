package jroyale.model.cards;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public abstract class Card {
    
    protected CardStats stats;
    private final EntityType TYPE;

    protected Card(EntityType type) {
        this.TYPE = type;
    }

    // setters
    public void setStats(CardStats stats) {
        this.stats = stats;
    }

    // getters
    public CardStats getCardStats() {
        return stats;
    }

    public EntityType getType() {
        return TYPE;
    }

    // abstract methods

    public abstract void dropCardIntoModel(int rowIndex, int columnIndex, Side side);

}
