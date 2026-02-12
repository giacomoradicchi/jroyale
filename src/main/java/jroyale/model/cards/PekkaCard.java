package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Pekka;
import jroyale.utils.Enums.Side;

public class PekkaCard extends Card{

    private static Card instance;
    private static final byte ELIXIR_COST = 7;

    private PekkaCard() {
        super(ELIXIR_COST);
    }
    
    public static Card getIstance() {
        if (instance == null) {
            instance = new PekkaCard();
        }

        return instance;
    }

    @Override
    protected void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        Model.getIstance().addTroop(
            new Pekka(rowIndex, columnIndex, side)
        );
    }
    
}
