package jroyale.model.cards;

import jroyale.model.troops.MiniPekka;
import jroyale.model.troops.Troop;

public class MiniPekkaCard extends Card{

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
    protected Troop generateNewTroop(int rowIndex, int columnIndex, byte side) {
        return new MiniPekka(rowIndex, columnIndex, side);
    }
}
