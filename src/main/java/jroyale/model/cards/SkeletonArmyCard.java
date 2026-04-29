package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Skeleton;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public class SkeletonArmyCard extends Card {

    private static SkeletonArmyCard instance;

    private SkeletonArmyCard() {
        super(EntityType.SKELETON_ARMY);
    }


    @Override
    public void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        for (byte i = 0; i < stats.getUnitsAmount(); i++)
            Model.getInstance().addTroop(
                new Skeleton(
                    rowIndex, 
                    columnIndex, 
                    stats.getName(), 
                    stats.getSpeed(), 
                    stats.getMeleeRange(), 
                    stats.getMass(),
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
            instance = new SkeletonArmyCard();
        }

        return instance;
    }
    
}
