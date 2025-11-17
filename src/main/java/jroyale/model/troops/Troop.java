package jroyale.model.troops; 


import java.util.HashMap;
import java.util.Map;

import jroyale.model.CollisionManager;
import jroyale.model.Entity;
import jroyale.model.FrameManager;
import jroyale.model.Model;
import jroyale.model.TowerTargetSelector;
import jroyale.utils.Point;

public abstract class Troop extends Entity {

    static final byte VERY_SLOW = 0;
    static final byte SLOW = 1;
    static final byte MEDIUM = 2;
    static final byte FAST = 3;
    static final byte VERY_FAST = 4;

    private String name;
    private final byte SPEED_TYPE;
    private FrameManager frameManager;

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

    protected Entity target;
    protected Point speed;
    protected Point direction; // it's just a normalised speed. I define a variable direction just to not create an instance of a point each time.

    private static final double TURNING_SPEED = 0.3; // 0: doesn't turn, 1: turns instantly
    private Point aimUnitVector; // buffer for aiming direction

    public Troop(String name, double x, double y, byte speedType, byte side) {
        super(x, y, side);
        this.name = name;
        this.frameManager = new FrameManager(this);
        
        if (speedType < VERY_SLOW || speedType > VERY_FAST) {
            this.SPEED_TYPE = MEDIUM;
        } else {
            this.SPEED_TYPE = speedType;
        }

        initTarget();
        initSpeed();
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

    public Point getSpeed() {
        return new Point(speed);
    }

    @Override
    public void update(long elapsed) {
        frameManager.updateFrame(elapsed);
        updateTarget();
        move(elapsed);
    }

    @Override 
    public Point getDirection() {
        updateDirection();
        
        return direction;
    }

    // 
    // protected methods
    // 

    protected void slideAlong(Entity other) {
        fixDistance(other);

        if (other != target) {
            setTangentSpeed(
                position.getX() - other.getX(), // dx
                position.getY() - other.getY()  // dy
            );
        } 
    
    }

    //
    // private methods
    //

    private void move(long elapsed) {
        updateSpeed(elapsed);
        for (Entity other : CollisionManager.checkCollisions(this)) {
            slideAlong(other);
        }

        

        shiftPosition(speed);

        // final fixing inside map and outside unreachable tiles:
        Point impactVector = CollisionManager.pushOutOfUnreachableTiles(this);
        if (!impactVector.isZeroVector()) {
            setTangentSpeed(impactVector.getX(), impactVector.getY());  
        }
        

    }

    private void fixDistance(Entity other) { // distance will be the sum of both collision radius
        // getting direction of the line passing through both center points
        double dy = position.getY() - other.getY();
        double dx = position.getX() - other.getX();
        if (dx == 0 && dy == 0) {
            return;
        }
        double angle = Math.atan2(dy, dx);

        // fixing distance between entities. 
        double distance = getCollisionRadius() + other.getCollisionRadius();
        double shiftX = distance * Math.cos(angle);
        double shiftY = distance * Math.sin(angle);
        
        position.setX(other.getX() + shiftX);
        position.setY(other.getY() + shiftY);
    }

    private void setTangentSpeed(double dx, double dy) {
        // getting the two vectors that are tangent to the entities (they are opposite)
        Point tangentVector1 = new Point(dx, dy).normalize().multiply(speed.magnitude()).rotate(90);
        Point tangentVector2 = new Point(tangentVector1).multiply(-1);
        // these 2 vectors have the same magnitude as vector speed

        // computing dot product to see which versor is the closest to previous direction
        double dot1 = tangentVector1.dotProduct(speed); 
        double dot2 = tangentVector2.dotProduct(speed); 

        if (dot1 > dot2) { // the closest is tangentVector1
            speed.interpolate(tangentVector1, TURNING_SPEED);
        } else {
            speed.interpolate(tangentVector2, TURNING_SPEED);
        }
    }

    private void shiftPosition(Point shift) {
        position = position.add(shift);
    }

    private void updateSpeed(long elapsed) {
        

        /* if (hasReachedTarget()) { 
            updateTarget();
            
            return;
        } */

        

        fixPathTroughBridge();

        // new vector speed will be the smooth aim unit vector (a vector that aims to the next target
        // based on troop position and his last direction) times his absolute speed [tiles/delta_time].
        
        speed = getSmoothAimUnitVector().multiply(getAbsoluteSpeed(elapsed));
    }

    

    private void updateDirection() {
        // direction is just a normalised version of speed. its value 
        // is computed only when updateDirection() method is called insiede
        // getDirection. 
        direction.setX(speed.getX());
        direction.setY(speed.getY());
        direction.normalize();
    }

    private double getAbsoluteSpeed(long elapsed) {
        // elapsed is in nanosec (10^(-9) sec) and speed is in tiles/minutes, so the speed in tiles/ns will be:
        return elapsed / 1_000_000_000.0 * SPEEDS.get(SPEED_TYPE) / 60.0 ;
    }

    private boolean hasReachedTarget() {
        return position.distance(target.getX(), target.getY()) < speed.magnitude();
    }

    private void setAimUnitVector(double targetX, double targetY) {
        aimUnitVector.setPoint(targetX - getX(), targetY - getY());
        aimUnitVector.normalize();
    }

    private Point getLastDirectionUnitVector() {
        if (speed.magnitude() == 0) return aimUnitVector; // to avoid division by 0
        return speed.normalize(); 
    }

    private Point getSmoothAimUnitVector() {
        // calculating mean between direction and last direction (for smooth turning)
        //return getAimUnitVector().midpoint(getLastDirectionUnitVector()).normalize();

        return getLastDirectionUnitVector().interpolate(aimUnitVector, TURNING_SPEED).normalize();
    }

    private void initSpeed() {
        this.aimUnitVector = new Point();
        fixPathTroughBridge();
        this.speed = new Point(aimUnitVector);
        this.direction = new Point(speed).normalize();
    }

    private void initTarget() {
        target = TowerTargetSelector.getClosestEnemyTower(this);
    }

    private void fixPathTroughBridge() {
        double targetX = target.getX();
        double targetY = target.getY();

        /* targetX = Model.MAP_COLS/2;
        targetY = 35; */

        double troopY = getY();

        
        double bridgeStartY = Entity.LEFT_BRIDGE_START_POS.getY(); 
        double bridgeEndY = Entity.LEFT_BRIDGE_END_POS.getY(); // left and right bridge have same Y cords

        double leftBridgeStartX = Entity.LEFT_BRIDGE_START_POS.getX();
        double rightBridgeStartX = Entity.RIGHT_BRIDGE_START_POS.getX();
        double leftBridgeEndX = Entity.LEFT_BRIDGE_END_POS.getX();
        double rightBridgeEndX = Entity.RIGHT_BRIDGE_END_POS.getX();


        if (troopY > bridgeStartY && targetY <= bridgeStartY) { 
            targetX = (targetX < Model.MAP_COLS / 2.0) ? leftBridgeStartX : rightBridgeStartX;
            targetY = bridgeStartY; 
        } else if (bridgeEndY < troopY && troopY < bridgeStartY 
        && targetY < bridgeEndY) {
            targetX = (targetX < Model.MAP_COLS / 2.0) ? leftBridgeEndX : rightBridgeEndX;
            targetY = bridgeEndY; 
        }

        else if (troopY < bridgeEndY && targetY >= bridgeEndY) {
            targetX = (targetX < Model.MAP_COLS / 2.0) ? leftBridgeEndX : rightBridgeEndX;
            targetY = bridgeEndY; 
        } else if (bridgeEndY < troopY && troopY < bridgeStartY 
        && targetY > bridgeStartY) {
            targetX = (targetX < Model.MAP_COLS / 2.0) ? leftBridgeStartX : rightBridgeStartX;
            targetY = bridgeStartY;
        }


        setAimUnitVector(targetX, targetY);
    } 

    //
    // abstract methods
    //

    protected abstract void updateTarget();

}