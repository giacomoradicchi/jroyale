package jroyale.model.cards;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public abstract class Card {
    
    private byte elixirCost;
    private final EntityType TYPE;

    protected Card(byte elixirCost, EntityType type) {
        this.elixirCost = elixirCost;
        this.TYPE = type;
    }

    public byte getElixirCost() {
        return elixirCost;
    }

    public EntityType getType() {
        return TYPE;
    }

    // abstract methods

    protected abstract void dropCardIntoModel(int rowIndex, int columnIndex, Side side);
}
