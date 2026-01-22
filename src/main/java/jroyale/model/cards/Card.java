package jroyale.model.cards;

import jroyale.model.troops.Troop;

public abstract class Card {
    
    private byte elixirCost;

    protected Card(byte elixirCost) {
        this.elixirCost = elixirCost;
    }

    public byte getElixirCost() {
        return elixirCost;
    }

    // abstract methods
    
    protected abstract Troop generateNewTroop(int rowIndex, int columnIndex, byte side);
}
