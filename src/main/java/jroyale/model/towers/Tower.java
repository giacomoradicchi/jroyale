package jroyale.model.towers;


import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jroyale.model.Entity;
import jroyale.utils.Point;

public abstract class Tower extends Entity {

    public Tower(double x, double y, byte side) {
        super(x, y, side);
        hitbox = new Rectangle();
    }

    public Tower(Point position, byte side) {
        this(position.getX(), position.getY(), side);
    }

    @Override
    public void update(long elapsed) {
        // TODO
    }

    @Override 
    public Shape getHitbox() {
        // for towers, hitbox is rectangular

        double side = getFootPrintSize();
        ((Rectangle) hitbox).setX(getX() - side * 0.5);
        ((Rectangle) hitbox).setY(getY() - side * 0.5);
        ((Rectangle) hitbox).setWidth(side);
        ((Rectangle) hitbox).setHeight(side);
        return hitbox;
    }

    // abstract methods

    public abstract byte getTowerType();
}
