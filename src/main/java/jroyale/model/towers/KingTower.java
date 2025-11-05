package jroyale.model.towers;

import jroyale.model.Entity;
import jroyale.shared.Side;
import jroyale.shared.TowerIndex;

public class KingTower extends Tower {

    private final byte TOWER_TYPE;

    private static final int FOOTPRINT_SIZE = 4;

    public KingTower(byte side) {
        super(
            (side == Side.PLAYER) ? 
            Entity.PLAYER_KING_TOWER_CENTRE : 
            Entity.OPPONENT_KING_TOWER_CENTRE,
        
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