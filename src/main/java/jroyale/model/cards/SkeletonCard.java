package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Skeleton;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public class SkeletonCard extends Card {

    private static SkeletonCard instance;
    private static final byte ELIXIR_COST = 1;
    private static final byte SKELETONS_DROPPED = 3;

    private SkeletonCard() {
        super(EntityType.SKELETONS);
    }

    @Override
    public void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        for (int i = 0; i < SKELETONS_DROPPED; i++)
            Model.getInstance().addTroop(
                new Skeleton(
                    rowIndex, 
                    columnIndex, 
                    stats.getName(), 
                    stats.getSpeed(), 
                    stats.getMeleeRange(), 
                    stats.getCollisionRadius(), 
                    stats.getLoadTime(), 
                    stats.getHitPoints(), 
                    stats.getDamage(), 
                    side
                )
            );
    }

    // static methods

    public static Card getInstance() {
        if (instance == null) {
            instance = new SkeletonCard();
        }

        return instance;
    }
    
}
