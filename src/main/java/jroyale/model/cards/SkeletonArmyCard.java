package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Skeleton;
import jroyale.shared.Enums.Side;

public class SkeletonArmyCard extends Card {

    private static Card instance;
    private static final byte ELIXIR_COST = 3;

    private SkeletonArmyCard() {
        super(ELIXIR_COST);
    }
    
    public static Card getIstance() {
        if (instance == null) {
            instance = new SkeletonArmyCard();
        }

        return instance;
    }

    @Override
    protected void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        // TODO: drop more skeletons.

        Model.getIstance().addTroop(
            new Skeleton(rowIndex, columnIndex, side)
        );
    }
    
}
