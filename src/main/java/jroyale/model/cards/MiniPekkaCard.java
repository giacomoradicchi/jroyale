package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.MiniPekka;
import jroyale.shared.Enums.Side;

public class MiniPekkaCard extends Card {

    private static Card instance;
    private static final byte ELIXIR_COST = 4;

    private MiniPekkaCard() {
        super(ELIXIR_COST);
    }
    
    public static Card getIstance() {
        if (instance == null) {
            instance = new MiniPekkaCard();
        }

        return instance;
    }

    @Override
    protected void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        Model.getIstance().addTroop(
            new MiniPekka(rowIndex, columnIndex, side)
        );
    }
    
}
