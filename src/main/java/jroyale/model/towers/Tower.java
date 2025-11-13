package jroyale.model.towers;

import jroyale.model.Entity;
import jroyale.utils.Point;

public abstract class Tower extends Entity {

    private static final double TOWER_COLLISION_RADIUS_FACTOR = 1.0; // just to make the collision radius slightly bigger

    public Tower(double x, double y, byte side) {
        super(x, y, side);
    }

    public Tower(Point position, byte side) {
        this(position.getX(), position.getY(), side);
    }

    @Override
    public void update(long elapsed) {
        // TODO
    }

    @Override
    public Point getDirection() {
        return null; // tower entity has not a direction since it doesn't move
    }

    @Override
    public double getCollisionRadius() {
        return getFootPrintSize() * 0.5 * TOWER_COLLISION_RADIUS_FACTOR;
    }

    // abstract methods

    public abstract byte getTowerType();
}
