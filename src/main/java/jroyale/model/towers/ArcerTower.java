package jroyale.model.towers;


import jroyale.shared.Side;
import jroyale.shared.TowerIndex;
import jroyale.utils.Point;

public class ArcerTower extends Tower {

    public static final byte LEFT = 0;
    public static final byte RIGHT = 1;

    private static final int FOOTPRINT_SIZE = 3;

    public static final Point PLAYER_LEFT_TOWER_CENTRE = new Point(3.5, 25.5);
    public static final Point PLAYER_RIGHT_TOWER_CENTRE = new Point(14.5, 25.5);

    public static final Point OPPONENT_LEFT_TOWER_CENTRE = new Point(3.5, 6.5);
    public static final Point OPPONENT_RIGHT_TOWER_CENTRE = new Point(14.5, 6.5);

    private final byte TOWER_TYPE;

    public ArcerTower(byte side, byte location) {
        super(getArcherPosition(side, location), side);

        if (side == Side.PLAYER) {
            TOWER_TYPE = (location == RIGHT) ? 
                TowerIndex.PLAYER_RIGHT_TOWER : 
                TowerIndex.PLAYER_LEFT_TOWER;
        } else {
            TOWER_TYPE = (location == RIGHT) ? 
                TowerIndex.OPPONENT_RIGHT_TOWER : 
                TowerIndex.OPPONENT_LEFT_TOWER;
        }
    }

    private static Point getArcherPosition(byte side, byte location) {
        // checks
        if (side != Side.PLAYER && side != Side.OPPONENT) 
            throw new IllegalArgumentException("Invalid argument side");
        
        if (location != LEFT && location != RIGHT) 
            throw new IllegalArgumentException("Invalid argument location");
        
        // end checks

        if (side == Side.PLAYER) {
            return (location == RIGHT) ? 
                PLAYER_RIGHT_TOWER_CENTRE : 
                PLAYER_LEFT_TOWER_CENTRE;
        }

        return (location == RIGHT) ? 
                OPPONENT_RIGHT_TOWER_CENTRE : 
                OPPONENT_LEFT_TOWER_CENTRE;
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
