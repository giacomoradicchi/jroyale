package jroyale.model;

import jroyale.shared.Side;
import jroyale.utils.Point;

public abstract class Entity implements Comparable<Entity>{

    // bridges position
    public static final Point LEFT_BRIDGE_START_POS = new Point(3.5,17);
    public static final Point RIGHT_BRIDGE_START_POS = new Point(14.5,17);
    public static final Point LEFT_BRIDGE_END_POS = new Point(3.5,15);
    public static final Point RIGHT_BRIDGE_END_POS = new Point(14.5,15);
 
    // player towers position
    public static final Point PLAYER_KING_TOWER_CENTRE = new Point(9, 29);
    public static final Point PLAYER_LEFT_TOWER_CENTRE = new Point(3.5, 25.5);
    public static final Point PLAYER_RIGHT_TOWER_CENTRE = new Point(14.5, 25.5);

    // opponent towers position
    public static final Point OPPONENT_KING_TOWER_CENTRE = new Point(9, 3);
    public static final Point OPPONENT_LEFT_TOWER_CENTRE = new Point(3.5, 6.5);
    public static final Point OPPONENT_RIGHT_TOWER_CENTRE = new Point(14.5, 6.5);

    protected Point position;
    protected byte side;

    private int currentI, currentJ; // current location in map[i][j] tile

    public Entity(double x, double y, byte side) {
        position = new Point(x, y);
        if (side != Side.PLAYER && side != Side.OPPONENT) {
            throw new IllegalArgumentException("Invalid argument side");
        }
        this.side = side;
    }

    public Entity(Point position, byte side) {
        this.position = new Point(position.getX(), position.getY());
        if (side != Side.PLAYER && side != Side.OPPONENT) {
            throw new IllegalArgumentException("Invalid argument side");
        }
        this.side = side;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public int getCurrentI() {
        return currentI;
    }

    public int getCurrentJ() {
        return currentJ;
    }

    public byte getSide() {
        return side;
    }

    // checks if an entity is outside his current Tile
    public boolean isOutsideTile() {
        return currentI != (int) Math.floor(position.getY())
        || currentJ != (int) Math.floor(position.getX());
    }

    // if entity is outside his current Tile (currentI, currentJ), 
    // the model has to update the new currentTile and displace entity from
    // previous tile to new tile.
    public void updateCurrentTile() {
        currentI = (int) Math.floor(position.getY());
        currentJ = (int) Math.floor(position.getX());
    }

    @Override
    public int compareTo(Entity entity) {
        // this method is crucial to achieve depth rendering.
        // order will be based on Y position
        return Double.compare(position.getY(), entity.getY()); // ascendent order
    }

    // abstract methods
    public abstract void update(long elapsed);
}
