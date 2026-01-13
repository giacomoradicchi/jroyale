package jroyale.model.towers;

import jroyale.model.Entity;
import jroyale.model.TowerTargetSelector;
import jroyale.model.troops.TowerAttackerTroop;
import jroyale.utils.Point;

public abstract class Tower extends Entity {

    

    private static final double TOWER_COLLISION_RADIUS_FACTOR = 1.0; // just to make the collision radius slightly bigger

    public Tower(double x, double y, int hitPoints, int damage, byte side) {
        super(x, y, hitPoints, damage, side);
    }

    public Tower(Point position, int hitPoints, int damage, byte side) {
        this(position.getX(), position.getY(), hitPoints, damage, side);
    }

    @Override
    public void update(long elapsed) {
        return;
    }

    @Override
    public void onDelete() {
        TowerTargetSelector.removeTower(this);
        // TODO: implement distruction animations on towers
    }

    @Override
    public Point getDirection() {
        return null; // tower entity has not a direction since it doesn't move
    }

    @Override
    public double getCollisionRadius() {
        return getFootPrintSize() * 0.5 * TOWER_COLLISION_RADIUS_FACTOR;
    }

    @Override
    public int getFPSAnimation() {
        return 0; // TODO
    }

    // abstract methods

    public abstract byte getTowerType();
}
