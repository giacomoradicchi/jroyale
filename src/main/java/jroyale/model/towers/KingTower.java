package jroyale.model.towers;

import jroyale.shared.Enums.Side;
import jroyale.shared.TowerIndex;
import jroyale.utils.Point;

public class KingTower extends Tower {

    private final byte TOWER_TYPE;

    private static final int FOOTPRINT_SIZE = 4;

    public static final Point PLAYER_KING_TOWER_CENTRE = new Point(9, 29);
    public static final Point OPPONENT_KING_TOWER_CENTRE = new Point(9, 3);
    
    private static final int HITPOINTS = 2400;
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
        TOWER_TYPE = (side == Side.PLAYER) ? 
            TowerIndex.PLAYER_KING_TOWER : 
            TowerIndex.OPPONENT_KING_TOWER;
    }

    @Override
    public byte getTowerType() {
        return TOWER_TYPE;
    }

    @Override
    public int getFootPrintSize() {
       return FOOTPRINT_SIZE;
    }
    
}