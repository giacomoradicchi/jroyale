package jroyale.model.towers;

import jroyale.model.Entity;
import jroyale.model.ITowerTargetSelector;
import jroyale.utils.Point;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public abstract class Tower extends Entity {

    
    private static final double TOWER_MASS = Double.POSITIVE_INFINITY; // Tower must not move during collisions

    public Tower(double x, double y, int hitPoints, int damage, Side side) {
        super(x, y, TOWER_MASS, hitPoints, damage, side);
    }

    public Tower(Point position, int hitPoints, int damage, Side side) {
        this(position.getX(), position.getY(), hitPoints, damage, side);
    }

    @Override
    public void update(long elapsed) {
        return;
    }

    @Override
    public void onDelete() {
        ITowerTargetSelector.getInstance().removeTower(this);
    }

    @Override
    public Point getDirection() {
        return Entity.NO_DIRECTION; // tower entity has not a direction since it doesn't move
    }

    @Override
    public State getState() {
        return State.IDLE; 
    }

    @Override
    public double getCollisionRadius() {
        return getFootPrintSize()/2.0; // half footPrintSize
    }

    @Override
    public int getFPSAnimation() {
        return 0;
    }

}
