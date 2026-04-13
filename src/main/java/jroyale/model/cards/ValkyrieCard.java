package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Valkyrie;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public class ValkyrieCard extends Card{
    
    private static ValkyrieCard instance;
    private static final byte ELIXIR_COST = 4;

    private ValkyrieCard() {
        super(EntityType.VALKYRIE);
    }

    @Override
    public void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        Model.getInstance().addTroop(
            new Valkyrie(
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
            instance = new ValkyrieCard();
        }

        return instance;
    }
}
