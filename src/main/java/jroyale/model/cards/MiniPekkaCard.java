package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.MiniPekka;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public class MiniPekkaCard extends Card {

    private static MiniPekkaCard instance;
    private static final byte ELIXIR_COST = 4;

    private MiniPekkaCard() {
        super(ELIXIR_COST, EntityType.MINIPEKKA);
    }

    @Override
    public void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        Model.getInstance().addTroop(
            new MiniPekka(rowIndex, columnIndex, side)
        );
    }

    // static methods

    public static Card getInstance() {
        if (instance == null) {
            instance = new MiniPekkaCard();
        }

        return instance;
    }
    
}
