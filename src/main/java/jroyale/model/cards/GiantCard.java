package jroyale.model.cards;

import jroyale.model.troops.Giant;
import jroyale.model.troops.Troop;

public class GiantCard extends Card{

    private static Card instance;
    private static final byte ELIXIR_COST = 8;

    private GiantCard() {
        super(ELIXIR_COST);
    }
    
    public static Card getIstance() {
        if (instance == null) {
            instance = new GiantCard();
        }

        return instance;
    }

    @Override
    protected Troop generateNewTroop(int rowIndex, int columnIndex, byte side) {
        return new Giant(rowIndex, columnIndex, side);
    }
    
}
