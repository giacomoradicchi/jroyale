package jroyale.model; 


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public abstract class Troop implements Comparable<Troop> {
    private String name;
    private Image pic;
    private final byte SPEED_TYPE;

    static final byte VERY_SLOW = 0;
    static final byte SLOW = 1;
    static final byte MEDIUM = 2;
    static final byte FAST = 3;
    static final byte VERY_FAST = 4;

    private static Map<Byte, Integer> SPEEDS = new HashMap<>() {
        {   // category - associated speed [tiles/minutes]
            // based on: https://clashroyale.fandom.com/wiki/Cards
            put(VERY_SLOW, 30);
            put(SLOW, 45);
            put(MEDIUM, 60);
            put(FAST, 90);
            put(VERY_FAST, 120);
        }
    };

    protected Point2D position;
    protected Point2D target;
    protected Point2D speed;
    protected List<Point2D> defaultRoute;

    public Troop(String name, Image pic, double x, double y, byte speedType) {
        this.name = name;
        this.pic = pic;
        this.position = new Point2D(x, y);
        this.speed = new Point2D(0, 0);
        
        if (speedType < VERY_SLOW || speedType > VERY_FAST) {
            this.SPEED_TYPE = MEDIUM;
        } else {
            this.SPEED_TYPE = speedType;
        }
        initTargetList();
        setFirstTarget();
    }

    public Troop(String name, Image pic, int n, int m, byte speedType) {
        // The constructor puts the troop in the centre of the cell (n, m).
        // In order to achieve this, it's necessary to shift the posX and posY by +0.5,
        // which is half a cell. In this way, the placing won't be in the top left corner; 
        // instead, it will be in the cell's centre.

        this(name, pic, m + 0.5, n + 0.5, speedType);
    }

    @Override
    public int compareTo(Troop troop) {
        // this method is crucial to achieve depth rendering.
        // order will be based on Y position
        return Double.compare(getPosY(), troop.getPosY()); // ascendent order
    }

    public double getPosX() {
        return position.getX();
    }

    public double getPosY() {
        return position.getY();
    }

    public void moove(long elapsed) {
        updateSpeed(elapsed);
        shiftPosition(speed);
    }

    // private methods

    private void shiftPosition(Point2D shift) {
        position = position.add(shift);
    }

    private void updateSpeed(long elapsed) {
        if (hasReachedTarget()) { 
            goToNextTarget();
            
            return;
        }

        // new vector speed will be the smooth aim unit vector (a vector that aims to the next target
        // based on troop position and his last direction) times his absolute speed [tiles/delta_time].
        speed = getSmoothAimUnitVector().multiply(getAbsoluteSpeed(elapsed));
    }

    private double getAbsoluteSpeed(long elapsed) {
        // elapsed is in nanosec (10^(-9) sec) and speed is in tiles/minutes, so the speed in tiles/ns will be:
        return elapsed / 1_000_000_000.0 * SPEEDS.get(SPEED_TYPE) / 60.0 ;
    }

    private boolean hasReachedTarget() {
        return position.distance(target) < speed.magnitude();
    }

    private Point2D getAimUnitVector() {
        return new Point2D(target.getX() - getPosX(), target.getY() - getPosY()).normalize();
    }

    private Point2D getLastDirectionUnitVector() {
        if (speed.magnitude() == 0) return getAimUnitVector(); // to avoid division by 0
        return speed.normalize(); 
    }

    private Point2D getSmoothAimUnitVector() {
        // calculating mean between direction and last direction (for smooth turning)
        //return getAimUnitVector().midpoint(getLastDirectionUnitVector()).normalize();

        return getLastDirectionUnitVector().interpolate(getAimUnitVector(), 0.9).normalize();
    }

    // abstract methods

    protected abstract int getSide();

    protected abstract void goToNextTarget();

    protected abstract void initTargetList();

    protected abstract void setFirstTarget();
}
