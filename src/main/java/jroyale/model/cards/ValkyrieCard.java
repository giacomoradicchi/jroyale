package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Valkyrie;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public class ValkyrieCard extends Card{
    
    private static ValkyrieCard instance;
    private static final byte ELIXIR_COST = 4;

    private ValkyrieCard() {
        super(ELIXIR_COST, EntityType.VALKYRIE);
    }
    
    public static Card getIstance() {
        if (instance == null) {
            instance = new ValkyrieCard();
        }

        return instance;
    }

    @Override
    protected void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        Model.getIstance().addTroop(
            new Valkyrie(rowIndex, columnIndex, side)
        );
    }
}
