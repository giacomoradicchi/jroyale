package jroyale.model; 


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jroyale.utils.Point;

public abstract class Troop extends Entity {
    private String name;
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

    protected Point target;
    protected Point speed;
    protected List<Point> defaultRoute;

    public Troop(String name, double x, double y, byte speedType, byte side) {
        super(x, y, side);
        this.name = name;
        this.speed = new Point(0, 0);
        
        if (speedType < VERY_SLOW || speedType > VERY_FAST) {
            this.SPEED_TYPE = MEDIUM;
        } else {
            this.SPEED_TYPE = speedType;
        }
        initTargetList();
        setFirstTarget();
    }

    public Troop(String name, int n, int m, byte speedType, byte side) {
        // The constructor puts the troop in the centre of the cell (n, m).
        // In order to achieve this, it's necessary to shift the posX and posY by +0.5,
        // which is half a cell. In this way, the placing won't be in the top left corner; 
        // instead, it will be in the cell's centre.

        this(name, m + 0.5, n + 0.5, speedType, side);
    }

    public String getName() {
        return name;
    }

    @Override
    public void update(long elapsed) {
        move(elapsed);
        for (Entity e : CollisionManager.checkCollisions(this)) {
            System.out.println("TROVATO");
        }
    }

    // private methods

    private void move(long elapsed) {
        updateSpeed(elapsed);
        shiftPosition(speed);
    }

    private void shiftPosition(Point shift) {
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

    private Point getAimUnitVector() {
        return new Point(target.getX() - getX(), target.getY() - getY()).normalize();
    }

    private Point getLastDirectionUnitVector() {
        if (speed.magnitude() == 0) return getAimUnitVector(); // to avoid division by 0
        return speed.normalize(); 
    }

    private Point getSmoothAimUnitVector() {
        // calculating mean between direction and last direction (for smooth turning)
        //return getAimUnitVector().midpoint(getLastDirectionUnitVector()).normalize();

        return getLastDirectionUnitVector().interpolate(getAimUnitVector(), 0.9).normalize();
    }

    // abstract methods

    protected abstract void goToNextTarget();

    protected abstract void initTargetList();

    protected abstract void setFirstTarget();
}
