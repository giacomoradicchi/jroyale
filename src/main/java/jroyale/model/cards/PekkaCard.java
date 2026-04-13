package jroyale.model.cards;

import jroyale.model.Model;
import jroyale.model.troops.Pekka;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public class PekkaCard extends Card{

    private static PekkaCard instance;
    private static final byte ELIXIR_COST = 7;

    private PekkaCard() {
        super(EntityType.PEKKA);
    }

    @Override
    public void dropCardIntoModel(int rowIndex, int columnIndex, Side side) {
        Model.getInstance().addTroop(
            new Pekka(
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
            instance = new PekkaCard();
        }

        return instance;
    }
    
}
