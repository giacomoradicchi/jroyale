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

    private static Map<String, Integer> SPEEDS = new HashMap<>() {
        {   // category - associated speed [tiles/minutes]
            // based on: https://clashroyale.fandom.com/wiki/Cards
            put("Very Slow", 30);
            put("Slow", 45);
            put("Medium", 60);
            put("Fast", 90);
            put("Very Fast", 120);
        }
    };

    protected Point2D position;
    protected Point2D target;
    protected Point2D speed;
    protected List<Point2D> defaultRoute;

    public Troop(String name, Image pic, double x, double y) {
        this.name = name;
        this.pic = pic;
        this.position = new Point2D(x, y);
        this.speed = new Point2D(0, 0);
        
        initTargetList();
        setFirstTarget();
        updateSpeed();
    }

    public Troop(String name, Image pic, int n, int m) {
        // The constructor puts the troop in the centre of the cell (n, m).
        // In order to achieve this, it's necessary to shift the posX and posY by +0.5,
        // which is half a cell. In this way, the placing won't be in the top left corner; 
        // instead, it will be in the cell's centre.

        this(name, pic, m + 0.5, n + 0.5);
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

    public void moove() {
        updateSpeed();
        shiftPosition(speed);
    }

    // private methods

    private void shiftPosition(Point2D shift) {
        position = position.add(shift);
    }

    private void updateSpeed() {
        if (hasReachedTarget()) { 
            goToNextTarget();
            
            return;
        }

        double module = 0.1; // TODO

        // new vector speed will be the smooth aim unit vector (a vector that aims to the next target
        // based on troop position and his last direction) times his absolute speed [tiles/delta_time].
        speed = getSmoothAimUnitVector().multiply(module);
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
