package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Skeleton;
import jroyale.utils.Enums.Side;

public class SkeletonCard extends Card {

    private static SkeletonCard instance;
    private static final byte ELIXIR_COST = 1;

    private SkeletonCard() {
        super(ELIXIR_COST);
    }
    
    public static Card getIstance() {
        if (instance == null) {
            instance = new SkeletonCard();
        }

        return instance;
    }

    @Override
    protected void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        Model.getIstance().addTroop(
            new Skeleton(rowIndex, columnIndex, side)
        );
    }
    
}
