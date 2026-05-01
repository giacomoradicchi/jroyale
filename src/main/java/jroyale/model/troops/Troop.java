package jroyale.model.troops; 

import jroyale.model.ArenaData;
import jroyale.model.Entity;
import jroyale.model.FrameManager;
import jroyale.model.ICollisionManager;
import jroyale.model.IEnemyTargetSelector;
import jroyale.model.Model;
import jroyale.model.TowerTargetSelector;
import jroyale.model.towers.Tower;
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

    public static enum MeleeRange {
        // based on: https://clashroyale.fandom.com/wiki/Cards

        SHORT(0.8),
        MEDIUM(1.2),
        LONG(1.6);

        private double radius;

        private MeleeRange(double radiusRange) {
            this.radius = radiusRange;
        }

        private double getRadiusRange() {
            return this.radius;
        }
    }

    private static final double DEFAULT_VISION_RANGE = 6;
    private static final int DIRECTION_BUFFER_SIZE = 4;
    private static final double TURNING_SPEED = 0.4; // 0: doesn't turn, 1: turns instantly
    private static final Point TANGENT_VECTOR_1 = new Point(); // variable buffers to avoid new constructor for every frame in setTangentSpeed() method
    private static final Point TANGENT_VECTOR_2 = new Point(); //
    // conversion factor for seconds to nanoseconds
    private static final long NANOS_PER_SECOND = 1_000_000_000L;
    // conversion factor for minutes to seconds
    private static final double SECONDS_PER_MINUTE = 60.0;
    // angle used for tangent vector rotation
    private static final int TANGENT_ROTATION_ANGLE = 90;
    // indicator for cases where both entities are unmovable
    private static final double UNMOVABLE_MASS_INDICATOR = -1.0;
    // movement weight constants
    private static final double MAX_MOVE_WEIGHT = 1.0;
    private static final double MIN_MOVE_WEIGHT = 0.0;
    private static final double EQUAL_MASS_WEIGHT = 0.5;
    // divisor used to reduce turning speed during sliding
    private static final int SLIDE_TURNING_REDUCTION = 2;
    // divisor used to find the horizontal center of the arena
    private static final double ARENA_CENTER_DIVISOR = 2.0;
    // initial y directions for aiming
    private static final int PLAYER_INITIAL_AIM_Y = -1;
    private static final int OPPONENT_INITIAL_AIM_Y = 1;

    protected String name;
    protected Speed speedType;
    protected FrameManager frameManager;
    protected MeleeRange melee;
    protected double loadTime;
    protected int hitPoints;
    protected int damage;
    protected Entity target;
    protected Point speed;
    protected Point direction; // it's just a normalised speed. I define a variable direction just to not create an instance of a point each time.
    protected long elapsedIdleTime;
    private Point[] directionBuffer = new Point[DIRECTION_BUFFER_SIZE];
    private int bufferIndex = 0;
    private Point aimUnitVector; // buffer for aiming direction
    protected boolean enemyHit, targetKilled;
    private boolean shouldMove, shouldAttack, shouldIdle;
    protected boolean isAttackingTower = false;

    public Troop(double x, double y, String name, Speed speedType, MeleeRange melee, double mass,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
            super(x, y, mass, collisionRadius, hitPoints, damage, side);
            setTroopStats(name, speedType, melee, loadTime, hitPoints, damage);
    }

    public Troop(int row, int col, String name, Speed speedType, MeleeRange melee, double mass,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
            super(row, col, mass, collisionRadius, hitPoints, damage, side);
            setTroopStats(name, speedType, melee, loadTime, hitPoints, damage);
    }

    private void setTroopStats(String name, Speed speedType, MeleeRange melee, double loadTime, int hitPoints, int damage) {
        this.name = name;
        this.frameManager = new FrameManager(this);
        this.state = State.MOVE;
        this.melee = melee;
        this.speedType = speedType;
        this.loadTime = loadTime;
        this.hitPoints = hitPoints;
        this.damage = damage;

        initTarget();
        initSpeed();
    }

    public String getName() {
        return name;
    }

    @Override
    public Point getSpeed() {
        return speed;
    }

    public double getMeleeRange() {
        return melee.getRadiusRange();
    }

    public double getVisionRange() {
        return DEFAULT_VISION_RANGE;
    }

    @Override
    public State getState() {
        return state;
    }

    protected long getLoadTimeNanoSec(){
        // nanosec in Idle state before attacking again
        return (long) (loadTime * NANOS_PER_SECOND); 
    } 

    @Override
    public void update(long elapsed) {
        frameManager.updateFrame(elapsed);
        
        updateState();
        updateSpeed(elapsed);

        switch (state) {
            case State.MOVE:
                handleMoveState();
                break;
            case State.ATTACK:
                handleAttackState();
                break;
            case State.IDLE:
                handleIdleState(elapsed);
                break;
            default:
                break;
        }

        updateTarget();
        handleCollisions();
    }

    private void updateState() {
        
        if (shouldAttack && isAnimationCompleted()) {
            setState(State.ATTACK);
            enemyHit = false;
            shouldAttack = false;
            shouldMove = false;
        } 
        else if (shouldMove && isAnimationCompleted()) {
            setState(State.MOVE);
            shouldMove = false;
            shouldAttack = false;
            enemyHit = false;
        } 
        else if (shouldIdle) {
            setState(State.IDLE);
            shouldIdle = false;
            enemyHit = false;
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

    protected void slideAlong(Entity other, double turning_speed) {

        if (other != target)
            setTangentSpeed(
                position.getX() - other.getX(), // dx
                position.getY() - other.getY(), // dy
                turning_speed
            ); 
    
    }

    protected boolean selectClosestEnemy() {

        Troop closestEnemy = IEnemyTargetSelector.getInstance().getClosestEnemyInVisionRange(this);

        

        if (closestEnemy == null) { // this means it was not found any enemy in the troop range
            return false;
        }

        
        target = closestEnemy;
        

        return true;
    }

    protected boolean selectClosestTower() {
        Tower possibleTargetTower = TowerTargetSelector.getInstance().getClosestEnemyTower(this);
        
        if (possibleTargetTower == null) return false;

        // target has to change if current target is null or dead
        if (target == null || target.getHitPoints() == 0) { 
            // set move state and reset others  
            target = possibleTargetTower;
            shouldMove = true;
            shouldAttack = false;
            shouldIdle = false;
            enemyHit = true;
            return true;
        } 

        // current target not null and not dead

        double distanceWithTarget = Point.distance(
            getX(), 
            getY(), 
            target.getX(), 
            target.getY()
        );

        double distanceWithPossibleTower = Point.distance(
            getX(), 
            getY(), 
            possibleTargetTower.getX(), 
            possibleTargetTower.getY()
        );

        if (distanceWithTarget <= distanceWithPossibleTower) {
            // doesn't update target if current target is closer 
            return false;
        }
            
        // set move state and reset others  
        target = possibleTargetTower;
        shouldMove = true;
        shouldAttack = false;
        shouldIdle = false;
        enemyHit = true;

        return true;
    }

    private void handleMoveState() {
        shiftPosition(speed);
    }

    private void handleIdleState(long elapsed) {
        

        elapsedIdleTime += elapsed;
        
        if (elapsedIdleTime < getLoadTimeNanoSec()) {
            return;
        }
            

        // idle time terminated: decide wheather attack the troop or move toward the target based on collision.
        if (target != null && ICollisionManager.getInstance().checkNextCollision(this, target)
        && target.getHitPoints() > 0) {
            shouldAttack = true;
        } else {
            shouldMove = true;
        }
            
        elapsedIdleTime = 0;

    }

    private void handleAttackState() {
        
        if (isHitFrameReached()) {
            attackTarget();
        } else if (isAnimationCompleted()) {
            shouldIdle = true;
        }

    }

    private boolean isHitFrameReached() {
        if (currentAnimationIndex == getHitFrame() && !enemyHit) {
            enemyHit = true;
            return true;
        }
        return false;
    }

    protected void attackTarget() {
        if (target == null) return;

        target.setDamage(getDamage());

    }

    private void handleCollisions() {
        for (Entity other : ICollisionManager.getInstance().getCollidingEntitiesWith(this)) {
            if (other == target && state == State.MOVE) {
                shouldAttack = true;

                if (target instanceof Tower) isAttackingTower = true;
            }


            fixDistance(other);
        }
    }

    private void fixDistance(Entity other) {
        double dx = position.getX() - other.getX();
        double dy = position.getY() - other.getY();
        double currentDistance = Math.sqrt(dx * dx + dy * dy);

        // avoiding zero divisions
        if (currentDistance == 0) return;

        double targetDistance = getCollisionRadius() + other.getCollisionRadius();

        // return if not touching
        if (currentDistance >= targetDistance) return;

        // overlap between circles
        double overlap = targetDistance - currentDistance;

        // normalizing direction vector
        double nx = dx / currentDistance;
        double ny = dy / currentDistance;

        // calculating weights based on mass
        double thisMoveWeight = getMoveWeight(other.getMass());
        if (thisMoveWeight == UNMOVABLE_MASS_INDICATOR) return; // they both cannot move

        double otherMoveWeight = MAX_MOVE_WEIGHT - thisMoveWeight;

        // appling shift
        this.shiftPosition(
            nx * overlap * thisMoveWeight,
            ny * overlap * thisMoveWeight
        );

        other.shiftPosition(
            -nx * overlap * otherMoveWeight,
            -ny * overlap * otherMoveWeight
        );
            

        if (state == State.MOVE) 
            slideAlong(other, thisMoveWeight / SLIDE_TURNING_REDUCTION);

        if (other instanceof Troop) 
            ((Troop) other).slideAlong(this, otherMoveWeight / SLIDE_TURNING_REDUCTION);
    }

    private double getMoveWeight(double otherMass) {
        // returns a value in [0, 1] that represent the weight of this entity's mass in relation to otherMass 
        // or -1 if they both shouldn't move (edge case)

        // 1. both unmovable
        if (getMass() == Double.POSITIVE_INFINITY && otherMass == Double.POSITIVE_INFINITY) {
            return UNMOVABLE_MASS_INDICATOR; 
        } 

        // 2. other is unmovable
        if (otherMass == Double.POSITIVE_INFINITY) {
            return MAX_MOVE_WEIGHT; // max weight, other doesn't move
        }
        // 3. this is unmovable 
        if (getMass() == Double.POSITIVE_INFINITY) {
            return MIN_MOVE_WEIGHT; // min weight, this doesn't move
        } 
        // 4. both moveable
        double thisMass = getMass();
        double totalMass = thisMass + otherMass;

        if (totalMass > 0) return otherMass / totalMass;

        // last case: they both have no mass, so their weight is the same
        return EQUAL_MASS_WEIGHT;
    }

    private void setTangentSpeed(double dx, double dy, double turning_speed) {
        // getting the two vectors that are tangent to the entities (they are opposite)
        TANGENT_VECTOR_1.setPoint(dx, dy).normalize().multiply(speed.magnitude()).rotate(TANGENT_ROTATION_ANGLE);
        TANGENT_VECTOR_2.setPoint(TANGENT_VECTOR_1).multiply(-1);
        
        // these 2 vectors have the same magnitude as vector speed

        // computing dot product to see which versor is the closest to previous direction
        double dot1 = TANGENT_VECTOR_1.dotProduct(speed); 
        double dot2 = TANGENT_VECTOR_2.dotProduct(speed); 

        speed.interpolate(dot1 >= dot2 ? TANGENT_VECTOR_1 : TANGENT_VECTOR_2, turning_speed);
    }

    private void updateSpeed(long elapsed) {
        

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
        return elapsed / (double) NANOS_PER_SECOND * speedType.getTilesPerMinute() / SECONDS_PER_MINUTE ;
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
        // 1. aggiorna il buffer con la direzione target corrente
        directionBuffer[bufferIndex] = new Point(aimUnitVector);
        bufferIndex = (bufferIndex + 1) % DIRECTION_BUFFER_SIZE;

        // 2. calcola la media mobile del buffer
        double avgX = 0, avgY = 0;
        for (Point p : directionBuffer) {
            avgX += p.getX();
            avgY += p.getY();
        }

        Point movingAverage = new Point(
            avgX / DIRECTION_BUFFER_SIZE, 
            avgY / DIRECTION_BUFFER_SIZE
        ).normalize();

        // 3. interpola tra la direzione attuale e la media mobile
        return movingAverage.interpolate(getLastDirectionUnitVector(), TURNING_SPEED).normalize();
    }

    private void initSpeed() {
        this.aimUnitVector = side == Side.PLAYER ? new Point(0, PLAYER_INITIAL_AIM_Y) : new Point(0, OPPONENT_INITIAL_AIM_Y); // nord or sud based on side
        fixPathTroughBridge();
        initDirectionBuffer();
        this.speed = new Point(aimUnitVector);
        this.direction = new Point(speed).normalize();
    }

    private void initTarget() {
        target = TowerTargetSelector.getInstance().getClosestEnemyTower(this);
    }

    private void initDirectionBuffer() {
        for (int i = 0; i < DIRECTION_BUFFER_SIZE; i++) {
            directionBuffer[i] = new Point(aimUnitVector);
        }
    }

    private void fixPathTroughBridge() {
        if (target == null) {
            return;
        }
        double targetX = target.getX();
        double targetY = target.getY();

        
        double troopX = getX();
        double troopY = getY();

        
        double bridgeStartY = ArenaData.LEFT_BRIDGE_START_POS.getY(); 
        double bridgeEndY = ArenaData.LEFT_BRIDGE_END_POS.getY(); // left and right bridge have same Y cords

        double leftBridgeStartX = ArenaData.LEFT_BRIDGE_START_POS.getX();
        double rightBridgeStartX = ArenaData.RIGHT_BRIDGE_START_POS.getX();
        double leftBridgeEndX = ArenaData.LEFT_BRIDGE_END_POS.getX();
        double rightBridgeEndX = ArenaData.RIGHT_BRIDGE_END_POS.getX();


        /* going from south to north:

        ***************
        ***************
            |*****| <- end
            |*****|         <- bridge
            |*****| <- start
        ***************
        ***************
               ^
               |
        (troopX, troopY)
        */


        if (troopY > bridgeStartY && targetY <= bridgeStartY) { 
            targetX = (troopX < Model.getInstance().getColsCount() / ARENA_CENTER_DIVISOR) ? leftBridgeStartX : rightBridgeStartX;
            targetY = bridgeStartY; 
        } else if (bridgeEndY < troopY && troopY < bridgeStartY 
        && targetY < bridgeEndY) {
            targetX = troopX;
            targetY = bridgeEndY; 
        }


        /* going from north to south:

        (troopX, troopY)
               |
               v
        ***************
        ***************
            |*****| <- end
            |*****|         <- bridge
            |*****| <- start
        ***************
        ***************
               
        */

        else if (troopY < bridgeEndY && targetY >= bridgeEndY) {
            targetX = (troopX < Model.getInstance().getColsCount() / ARENA_CENTER_DIVISOR) ? leftBridgeEndX : rightBridgeEndX;
            targetY = bridgeEndY; 
        } else if (bridgeEndY < troopY && troopY < bridgeStartY 
        && targetY > bridgeStartY) {
            targetX = troopX;
            targetY = bridgeStartY;
        }

        setAimUnitVector(targetX, targetY);
    } 

    //
    // abstract methods
    //

    protected abstract void updateTarget();

    protected abstract int getHitFrame();

}