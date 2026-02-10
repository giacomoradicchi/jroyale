package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Giant;
import jroyale.utils.Enums.Side;

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
    protected void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        Model.getIstance().addTroop(
            new Giant(rowIndex, columnIndex, side)
        );
    }
    
}
