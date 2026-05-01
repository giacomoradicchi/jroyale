package jroyale.model;

import jroyale.utils.Point;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public abstract class Entity implements Comparable<Entity>{
    

    private int currentI, currentJ; // current location in map[i][j] tile
    private boolean animationCompleted; // turns true when currentFrame goes back to start (N-1 -> 0).
    private int hitPoints;
    private final int MAX_HIT_POINTS; 
    private int damage;


    private static final double DEFAULT_COLLISION_RADIUS = 0.5; 
    private static final int DEFAULT_ANIMATION_STEPS = 1; // by default, entity has no animations.
    private static final Point DEFAULT_SPEED = new Point(0, 0); // doesn't move
    
    // divisor used to center the entity inside a tile
    private static final int CENTER_DIVISOR = 2;
    // factor used to calculate the diameter from the radius
    private static final int DIAMETER_FACTOR = 2;
    // minimum value for hit points
    private static final int MIN_HIT_POINTS = 0;
    // index representing the first frame of an animation
    private static final int START_FRAME_INDEX = 0;
    // value used to reset the animation index
    private static final int RESET_ANIMATION_INDEX = 0;
      
    private double collisionRadius;
    private double mass;
    protected Point position;
    protected Side side;
    protected int currentAnimationIndex;
    protected State state; // defines wheather a troop is walking, attacking, etc.

    protected static final Point NO_DIRECTION = new Point(0, 0);
    

    public Entity(double x, double y, double mass, double collisionRadius, int hitPoints, int damage, Side side) {
        
        if (side != Side.PLAYER && side != Side.OPPONENT) {
            throw new IllegalArgumentException("Invalid argument side");
        }
        
        this.position = new Point(x, y);
        this.mass = mass;
        this.side = side;
        this.state = State.IDLE; // initial state
        this.collisionRadius = collisionRadius;
        this.hitPoints = hitPoints;
        this.MAX_HIT_POINTS = hitPoints;
        this.damage = damage;
    }

    public Entity(double x, double y, double mass, int hitPoints, int damage, Side side) {
        this(x, y, mass, DEFAULT_COLLISION_RADIUS, hitPoints, damage, side);
    }

    public Entity(Point position, double mass, double collisionRadius, int hitPoints, int damage, Side side) {
        this(position.getX(), position.getY(), mass, collisionRadius, hitPoints, damage, side);
    }

    public Entity(Point position, double mass, int hitPoints, int damage, Side side) {
        this(position.getX(), position.getY(), mass, DEFAULT_COLLISION_RADIUS, hitPoints, damage, side);
    }

    // costructors for when position is an integer (row and column)

    public Entity(int row, int col, double mass, double collisionRadius, int hitPoints, int damage, Side side) {
        this(col + Model.getInstance().getTileSize() / CENTER_DIVISOR, row + Model.getInstance().getTileSize() / CENTER_DIVISOR, mass, collisionRadius, hitPoints, damage, side);
        // adding half tile size so it will be centered inside tile instead of top left corner.
    }

    public Entity(int row, int col, double mass, int hitPoints, int damage, Side side) {
        this(col + Model.getInstance().getTileSize() / CENTER_DIVISOR, row + Model.getInstance().getTileSize() / CENTER_DIVISOR, mass, DEFAULT_COLLISION_RADIUS, hitPoints, damage, side);
        // adding half tile size so it will be centered inside tile instead of top left corner.
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

    public Side getSide() {
        return side;
    }

    public int getMaxHitPoints() {
        return MAX_HIT_POINTS;
    }

    public int getFootPrintSize() { // number of cells occupied by the entity
        return (int) Math.ceil(getCollisionRadius() * DIAMETER_FACTOR);
    } 

    public double getCollisionRadius() {
        return collisionRadius; 
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getDamage() {
        return damage;
    }

    public double getMass() {
        return mass;
    }

    public Point getSpeed() {
        return DEFAULT_SPEED;
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
            ICollisionManager.getInstance().fixEntityInsideReachableTile(this, shiftX, shiftY)
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

    public void setDamage(int damage) {
        hitPoints -= damage;
        if (hitPoints < MIN_HIT_POINTS) {
            hitPoints = MIN_HIT_POINTS;
        }
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


    public int getTotalAnimationSteps() {
        return DEFAULT_ANIMATION_STEPS;
    }

    public int getCurrentAnimationIndex() { 
        return currentAnimationIndex;
    } 

    public void goToNextFrame() {
        currentAnimationIndex = (currentAnimationIndex + 1) % getTotalAnimationSteps();
        if (currentAnimationIndex == START_FRAME_INDEX) {
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

    protected void setState(State newState) {
        if (this.state == newState) return;
        
        state = newState;
        currentAnimationIndex = RESET_ANIMATION_INDEX;
        animationCompleted = false;
    }

    // abstract methods
    public abstract void update(long elapsed);

    public abstract void onDelete();

    public abstract Point getDirection(); // only entities that have speed attribs 
    // (like troops) will return their direction. otherwise, they'll return null

    public abstract State getState();

    public abstract int getFPSAnimation();

    public abstract EntityType getType();

}