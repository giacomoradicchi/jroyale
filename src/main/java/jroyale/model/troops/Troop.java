package jroyale.model.troops; 


import java.util.EnumMap;
import java.util.Map;

import jroyale.model.CollisionManager;
import jroyale.model.EnemyTargetSelector;
import jroyale.model.Entity;
import jroyale.model.FrameManager;
import jroyale.model.Model;
import jroyale.model.TowerTargetSelector;
import jroyale.utils.Point;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public abstract class Troop extends Entity {

    public static enum Speed {
        // category - associated speed [tiles/minute]
        // based on: https://clashroyale.fandom.com/wiki/Cards

        VERY_SLOW(30),
        SLOW(45),
        MEDIUM(60),
        FAST(90),
        VERY_FAST(120);

        private int tilesPerMinute;

        private Speed(int tilesPerMinute) {
            this.tilesPerMinute = tilesPerMinute;
        }

        private int getTilesPerMinute() {
            return tilesPerMinute;
        }
    }

    public static enum Range {
        // based on: https://clashroyale.fandom.com/wiki/Cards

        SHORT(0.8),
        MEDIUM(1.2),
        LONG(1.6);

        private double radius;

        private Range(double radiusRange) {
            this.radius = radiusRange;
        }

        private double getRadiusRange() {
            return this.radius;
        }
    }

    private String name;
    private final Speed SPEED_TYPE;
    private FrameManager frameManager;
    private Range melee;

    

    protected Entity target;
    protected Point speed;
    protected Point direction; // it's just a normalised speed. I define a variable direction just to not create an instance of a point each time.
    protected long elapsedIdleTime;

    private static final double TURNING_SPEED = 0.3; // 0: doesn't turn, 1: turns instantly
    private Point aimUnitVector; // buffer for aiming direction
    protected boolean enemyHit, targetKilled;

    private static final Point TANGENT_VECTOR_1 = new Point(); // variable buffers to avoid new constructor for every frame in setTangentSpeed() method
    private static final Point TANGENT_VECTOR_2 = new Point(); //

    public Troop(String name, double x, double y, int healthPoints, int damage, Speed speedType, Range melee, Side side) {
        super(x, y, healthPoints, damage, side);
        this.name = name;
        this.frameManager = new FrameManager(this);
        this.state = State.MOVE;
        this.melee = melee;
        this.SPEED_TYPE = speedType;

        initTarget();
        initSpeed();
    }

    public Troop(String name, int n, int m, int healthPoints, int damage, Speed speedType, Range melee, Side side) {
        // The constructor puts the troop in the centre of the cell (n, m).
        // In order to achieve this, it's necessary to shift the posX and posY by +0.5,
        // which is half a cell. In this way, the placing won't be in the top left corner; 
        // instead, it will be in the cell's centre.

        this(name, m + 0.5, n + 0.5, healthPoints, damage, speedType, melee, side);
    }

    public String getName() {
        return name;
    }

    public Point getSpeed() {
        return new Point(speed);
    }

    public double getRadiusRange() {
        return melee.radius;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void update(long elapsed) {
        frameManager.updateFrame(elapsed);
        updateTarget();
        if(state != State.ATTACK) {
            updateSpeed(elapsed);
        }
        
        //System.out.println(state);

        if (state == State.IDLE) {
            handleIdleState(elapsed);
        } else {
            move();
        }
        
        

    }

    @Override
    public void onDelete() {
        // TODO: adding animation when entity is deleted.
        return;
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

    protected boolean selectClosestEnemy() {
        Troop closestEnemy = EnemyTargetSelector.getInstance().getClosestEnemyOnRange(this);

        if (closestEnemy == null) { // this means it was not found any enemy in the troop range
            return false;
        }

        target = closestEnemy;

        return true;
    }

    protected boolean selectClosestTower() {
        if (target != null && target.getHitPoints() == 0) { // TODO: aggiungere metodo attack(entity) che attacca una torre e che la rimuove dai target quando la vita è a zero.
            target = TowerTargetSelector.getClosestEnemyTower(this);
            setState(State.MOVE);
            //enemyHit = false; // reset enemyHit
            return true;
        }
        return false;
    }

    //
    // private methods
    //

    private void move() {
        
        handleCollisions();

        if (state == State.ATTACK) {
            handleAttackState();
            
        }

        if (state == State.MOVE) {
            shiftPosition(speed);
        }
        
        
        

    }

    private void handleIdleState(long elapsed) {
        elapsedIdleTime += elapsed;

        if (elapsedIdleTime >= getLoadTime()) {
            setState(State.ATTACK);
            elapsedIdleTime = 0;
        }
    }

    private void handleAttackState() {
        if (isHitFrameReached()) {
            attackTarget();
        } else if (isAnimationCompleted()) {
            setState(State.IDLE);
            enemyHit = false; // reset enemyHit
        }
    }

    private boolean isHitFrameReached() {
        if (currentAnimationIndex == getHitFrame() && !enemyHit) {
            enemyHit = true;
            return true;
        }
        return false;
    }

    private void attackTarget() {
        target.setDamage(getDamage());
    }

    private void handleCollisions() {
        for (Entity other : CollisionManager.checkCollisions(this)) {
            if (other == target && state == State.MOVE) {
                setState(State.ATTACK);
            }

            if (state != State.ATTACK || (state == State.ATTACK && other == target)) {
                slideAlong(other);
            }
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

        shiftPosition(
            other.getX() - position.getX() + shiftX, 
            other.getY() - position.getY() + shiftY
        );
        // it's the same as:
        // position.setX(other.getX() + shiftX);
        // position.setY(other.getY() + shiftY);
        
        /* 
        // checking if entity is still inside reachable tiles.
        CollisionManager.pushOutOfUnreachableTiles(this, shiftX, shiftY); */
    }

    private void setTangentSpeed(double dx, double dy) {
        // getting the two vectors that are tangent to the entities (they are opposite)
        TANGENT_VECTOR_1.setPoint(dx, dy).normalize().multiply(speed.magnitude()).rotate(90);
        TANGENT_VECTOR_2.setPoint(TANGENT_VECTOR_1).multiply(-1);
        
        // these 2 vectors have the same magnitude as vector speed

        // computing dot product to see which versor is the closest to previous direction
        double dot1 = TANGENT_VECTOR_1.dotProduct(speed); 
        double dot2 = TANGENT_VECTOR_2.dotProduct(speed); 

        if (dot1 > dot2) { // the closest is tangentVector1
            speed.interpolate(TANGENT_VECTOR_1, TURNING_SPEED);
        } else {
            speed.interpolate(TANGENT_VECTOR_2, TURNING_SPEED);
        }
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
        return elapsed / 1_000_000_000.0 * SPEED_TYPE.getTilesPerMinute() / 60.0 ;
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
        if (target == null) {
            return;
        }
        double targetX = target.getX();
        double targetY = target.getY();

        

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

        //targetX = Model.MAP_COLS- 3; //Model.MAP_COLS
        //targetY = Model.MAP_ROWS;
        //targetY = Model.MAP_ROWS;

        setAimUnitVector(targetX, targetY);
    } 

    //
    // abstract methods
    //

    protected abstract void updateTarget();

    protected abstract long getLoadTime(); // nanosec in Idle state before attacking again

    protected abstract int getHitFrame();

}