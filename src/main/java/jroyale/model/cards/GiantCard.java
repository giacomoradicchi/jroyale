package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Giant;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public class GiantCard extends Card{

    private static GiantCard instance;
    private static final byte ELIXIR_COST = 8;

    private GiantCard() {
        super(ELIXIR_COST, EntityType.GIANT);
    }

    @Override
    public void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        Model.getIstance().addTroop(
            new Giant(rowIndex, columnIndex, side)
        );
    }
    
    // static methods
    
    public static Card getInstance() {
        if (instance == null) {
            instance = new GiantCard();
        }

        return instance;
    }
}
