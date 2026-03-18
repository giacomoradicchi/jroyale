package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Skeleton;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public class SkeletonArmyCard extends Card {

    private static SkeletonArmyCard instance;
    private static final byte ELIXIR_COST = 3;
    private static final byte ARMY_SIZE = 5;

    private SkeletonArmyCard() {
        super(ELIXIR_COST, EntityType.SKELETON_ARMY);
    }


    @Override
    public void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        // TODO: drop more skeletons.

        for (int i = 0; i < ARMY_SIZE; i++)
            Model.getInstance().addTroop(
                new Skeleton(rowIndex, columnIndex, side)
            );
    }

    // static methods

    public static Card getInstance() {
        if (instance == null) {
            instance = new SkeletonArmyCard();
        }

        return instance;
    }
    
}
