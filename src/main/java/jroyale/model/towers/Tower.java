package jroyale.model.towers;


import jroyale.model.Entity;
import jroyale.utils.Point;

public abstract class Tower extends Entity {
    
    static double WIDTH; // in map-unit
    static double HEIGHT; // in map-unit

    public Tower(double x, double y, byte side) {
        super(x, y, side);
    }

    public Tower(Point position, byte side) {
        super(position, side);
    }

    @Override
    public void update(long elapsed) {
        // TODO
    }

    // abstract methods
    public abstract byte getTowerType();
}
