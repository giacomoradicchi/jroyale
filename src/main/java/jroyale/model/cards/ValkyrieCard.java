package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Valkyrie;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public class ValkyrieCard extends Card{
    
    private static ValkyrieCard instance;

    private ValkyrieCard() {
        super(EntityType.VALKYRIE);
    }

    @Override
    public void dropCard(int rowIndex, int columnIndex, Side side) {
        for (byte i = 0; i < stats.getUnitsAmount(); i++)
            Model.getInstance().addTroop(
                new Valkyrie(
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
            instance = new ValkyrieCard();
        }

        return instance;
    }
}
