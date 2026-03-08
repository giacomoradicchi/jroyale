package jroyale.model.towers;

import jroyale.utils.Point;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public class KingTower extends Tower {

    private static final int FOOTPRINT_SIZE = 4;

    public static final Point PLAYER_KING_TOWER_CENTRE = new Point(9, 29);
    public static final Point OPPONENT_KING_TOWER_CENTRE = new Point(9, 3);
    
    private static final int HITPOINTS = 3000;
    private static final int DAMAGE = 0; // TODO: add damage

    public KingTower(Side side) {
        super(
            (side == Side.PLAYER) ? 
            PLAYER_KING_TOWER_CENTRE : 
            OPPONENT_KING_TOWER_CENTRE,
            HITPOINTS,
            DAMAGE,
            side 
        );
    }

    @Override
    public int getFootPrintSize() {
       return FOOTPRINT_SIZE;
    }

    @Override
    public EntityType getType() {
        return EntityType.KING_TOWER;
    }
    
}