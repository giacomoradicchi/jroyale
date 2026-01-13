package jroyale.model;

import jroyale.shared.Side;
import jroyale.shared.State;
import jroyale.utils.Point;

public abstract class Entity implements Comparable<Entity>{

    // bridges position
    public static final Point LEFT_BRIDGE_START_POS = new Point(3.5,17);
    public static final Point RIGHT_BRIDGE_START_POS = new Point(14.5,17);
    public static final Point LEFT_BRIDGE_END_POS = new Point(3.5,15);
    public static final Point RIGHT_BRIDGE_END_POS = new Point(14.5,15);

    

    private int currentI, currentJ; // current location in map[i][j] tile
    private boolean animationCompleted; // turns true when currentFrame goes back to start (N-1 -> 0).
    private int hitPoints;
    private final int MAX_HIT_POINTS; 
    private int damage;


    private static final double DEFAULT_COLLISION_RADIUS = 0.5; 
    private static final int DEFAULT_NUM_FRAMES_PER_DIRECTION = 1; // just 1 frame for animation = static view, no animation.
      
    
    protected Point position;
    protected byte side;
    protected int currentFrame;
    protected byte state; // defines wheather a troop is walking, attacking, etc.
    

    public Entity(double x, double y, int hitPoints, int damage, byte side) {
        
        if (side != Side.PLAYER && side != Side.OPPONENT) {
            throw new IllegalArgumentException("Invalid argument side");
        }

        this.position = new Point(x, y);
        this.side = side;
        this.state = State.IDLE; // default state
        this.hitPoints = hitPoints;
        this.MAX_HIT_POINTS = hitPoints;
        this.damage = damage;
    }

    public Entity(Point position, int hitPoints, int damage, byte side) {
        this(position.getX(), position.getY(), hitPoints, damage, side);
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

    public int getMaxHitPoints() {
        return MAX_HIT_POINTS;
    }

    // setters

    public void setPosition(Point pos) {
        position.setPoint(pos);
    }

    public void setX(double x) {
        position.setX(x);
    }

    public void setY(double y) {
        position.setY(y);
    }

    public void shiftPosition(double shiftX, double shiftY) {

        position.setPoint(
            /* sets new point based on shift value given.
            if point (posX + shiftX, posY + shiftY) is inside map, collisionManager's method
            will return the same point. otherwise it will fix it inside map.  
            */ 
            CollisionManager.fixEntityInsideReachableTile(this, shiftX, shiftY)
        ); 
        
    }

    public void shiftPosition(Point shift) {
        shiftPosition(shift.getX(), shift.getY());
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

    public int getFootPrintSize() { // number of cells occupied by the entity
        return (int) Math.ceil(getCollisionRadius() * 2);
    } 

    public double getCollisionRadius() {
        return DEFAULT_COLLISION_RADIUS; // default radius
    }

    public void setDamage(int damage) {
        hitPoints -= damage;
        if (hitPoints < 0) {
            hitPoints = 0;
        }
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public int compareTo(Entity entity) {
        // this method is crucial to achieve depth rendering.
        // order will be based on Y position
        return Double.compare(position.getY(), entity.getY()); // ascendent order
    }

    

    //
    // animation methods
    //

    public int getFramesPerDirection() {
        return DEFAULT_NUM_FRAMES_PER_DIRECTION; 
    }

    public int getCurrentFrame() { 
        return currentFrame;
    } 

    public void goToNextFrame() {
        currentFrame = (currentFrame + 1) % getFramesPerDirection();
        if (currentFrame == 0) {
            animationCompleted = true;
        }
    }

    //
    // end animation methods
    //

    protected boolean isAnimationCompleted() { // returns true just the first time animationCompleted goes true
        boolean completed = animationCompleted;
        if (completed) {
            animationCompleted = false;
        }
        return completed;
    }

    protected void setState(byte newState) {
        state = newState;
        currentFrame = 0;
        animationCompleted = false;
    }

    // abstract methods
    public abstract void update(long elapsed);

    public abstract void onDelete();

    public abstract Point getDirection(); // only entities that have speed attribs 
    // (like troops) will return their direction. otherwise, they'll return null

    public abstract int getFPSAnimation();
}
