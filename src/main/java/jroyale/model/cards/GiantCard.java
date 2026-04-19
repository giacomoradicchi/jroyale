package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Giant;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public class GiantCard extends Card{

    private static GiantCard instance;

    private GiantCard() {
        super(EntityType.GIANT);
    }

    @Override
    public void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {

        //System.out.println("Units: " + stats.getUnitsAmount() + ", " + getType());

        for (byte i = 0; i < stats.getUnitsAmount(); i++)
            Model.getInstance().addTroop(
                new Giant(
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
            instance = new GiantCard();
        }

        return instance;
    }
}
