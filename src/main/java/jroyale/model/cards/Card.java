package jroyale.model.cards;

import jroyale.utils.Enums.Side;

public abstract class Card {
    
    private byte elixirCost;

    protected Card(byte elixirCost) {
        this.elixirCost = elixirCost;
    }

    public byte getElixirCost() {
        return elixirCost;
    }

    // abstract methods

    protected abstract void dropCardIntoModel(int rowIndex, int columnIndex, Side side);
}
