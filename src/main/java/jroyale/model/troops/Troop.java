package jroyale.model.troops; 

import jroyale.model.ArenaData;
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

    private String name;
    private final Speed SPEED_TYPE;
    private FrameManager frameManager;
    private MeleeRange melee;

    private static final double DEFAULT_VISION_RANGE = 6;


    protected Entity target;
    protected Point speed;
    protected Point direction; // it's just a normalised speed. I define a variable direction just to not create an instance of a point each time.
    protected long elapsedIdleTime;

    private static final int DIRECTION_BUFFER_SIZE = 4;
    private Point[] directionBuffer = new Point[DIRECTION_BUFFER_SIZE];
    private int bufferIndex = 0;
    private static final double TURNING_SPEED = 0.3; // 0: doesn't turn, 1: turns instantly
    private Point aimUnitVector; // buffer for aiming direction
    protected boolean enemyHit, targetKilled;

    private static final Point TANGENT_VECTOR_1 = new Point(); // variable buffers to avoid new constructor for every frame in setTangentSpeed() method
    private static final Point TANGENT_VECTOR_2 = new Point(); //

    private boolean shouldMove, shouldAttack, shouldIdle;

    public Troop(String name, double x, double y, int healthPoints, int damage, Speed speedType, MeleeRange melee, Side side) {
        super(x, y, healthPoints, damage, side);
        this.name = name;
        this.frameManager = new FrameManager(this);
        this.state = State.MOVE;
        this.melee = melee;
        this.SPEED_TYPE = speedType;

        initTarget();
        initSpeed();
    }

    public Troop(String name, int n, int m, int healthPoints, int damage, Speed speedType, MeleeRange melee, Side side) {
        // The constructor puts the troop in the centre of the cell (n, m).
        // In order to achieve this, it's necessary to shift the posX and posY by +0.5,
        // which is half a cell. In this way, the placing won't be in the top left corner; 
        // instead, it will be in the cell's centre.

        this(name, m + 0.5, n + 0.5, healthPoints, damage, speedType, melee, side);
    }

    public String getName() {
        return name;
    }

    @Override
    public Point getSpeed() {
        return new Point(speed);
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
        Troop closestEnemy = EnemyTargetSelector.getInstance().getClosestEnemyInVisionRange(this);

        if (closestEnemy == null) { // this means it was not found any enemy in the troop range
            return false;
        }

        target = closestEnemy;

        return true;
    }

    protected boolean selectClosestTower() {
        if (target != null && target.getHitPoints() == 0) { 
            target = TowerTargetSelector.getClosestEnemyTower(this);
            
            // set move state and reset others  
            shouldMove = true;
            shouldAttack = false;
            shouldIdle = false;
            enemyHit = true;

            return true;
        }
        return false;
    }

    private void handleMoveState() {
        shiftPosition(speed);
    }

    private void handleIdleState(long elapsed) {
        

        elapsedIdleTime += elapsed;
        
        if (elapsedIdleTime < getLoadTime()) {
            return;
        }
            

        // idle time terminated: decide wheather attack the troop or move toward the target based on collision.
        if (target != null && CollisionManager.getInstance().checkNextCollision(this, target)
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
        if (target != null)
            target.setDamage(getDamage());
    }

    private void handleCollisions() {
        for (Entity other : CollisionManager.getInstance().getCollidingEntitiesWith(this)) {
            if (other == target && state == State.MOVE) {
                shouldAttack = true;
            }

            fixDistance(other);
        }
    }

    private void fixDistance(Entity other) {
        double dx = position.getX() - other.getX();
        double dy = position.getY() - other.getY();
        double currentDistance = Math.sqrt(dx * dx + dy * dy);

        // Evitiamo divisioni per zero se le entità sono perfettamente sovrapposte
        if (currentDistance == 0) return;

        double targetDistance = getCollisionRadius() + other.getCollisionRadius();

        // Se non si stanno toccando, non fare nulla
        if (currentDistance >= targetDistance) return;

        // Di quanto dobbiamo spostarle in totale?
        double overlap = targetDistance - currentDistance;

        // Normalizziamo il vettore direzione (dx, dy)
        double nx = dx / currentDistance;
        double ny = dy / currentDistance;

        // Calcoliamo i pesi basati sulla massa
        // Se interpFactor è 0.5, si muovono uguali. 
        // Se la mia massa è enorme, interpFactor si avvicina a 0 (io non mi muovo, si muove l'altro)

        double thisMoveWeight;  // Più l'altro è pesante, più mi muovo io
        double otherMoveWeight;    // Più io sono pesante, più si muove l'altro

        // 1. Gestione Masse Infinite (Immobili)
        if (getMass() == Double.POSITIVE_INFINITY && other.getMass() == Double.POSITIVE_INFINITY) {
            return; // Entrambi immobili, non si sposta nessuno
        } else if (other.getMass() == Double.POSITIVE_INFINITY) {
            thisMoveWeight = 1.0;
            otherMoveWeight = 0.0;
        } else if (getMass() == Double.POSITIVE_INFINITY) {
            thisMoveWeight = 0.0;
            otherMoveWeight = 1.0;
        } else {
            // 2. Calcolo basato sul Momentum
            double thisP = getMass() * speed.magnitude();
            double otherP = other.getMass() * other.getSpeed().magnitude();
            double totalP = thisP + otherP;

            if (totalP > 0) {
                // Chi ha meno momentum subisce più spostamento
                thisMoveWeight = otherP / totalP;
                otherMoveWeight = thisP / totalP;
            } else {
                // 3. Fallback: Se entrambi sono fermi, usa solo la massa
                double totalMass = getMass() + other.getMass();
                thisMoveWeight = other.getMass() / totalMass;
                otherMoveWeight = getMass() / totalMass;
            }
        }
        

        // Applichiamo lo spostamento
        this.shiftPosition(
            nx * overlap * thisMoveWeight,
            ny * overlap * thisMoveWeight
        );

        other.shiftPosition(
            -nx * overlap * otherMoveWeight,
            -ny * overlap * otherMoveWeight
        );
            

        if (state == State.MOVE) 
            slideAlong(other, thisMoveWeight/2);

        if (other instanceof Troop) 
            ((Troop) other).slideAlong(this, otherMoveWeight/2);
    }

    private void setTangentSpeed(double dx, double dy, double turning_speed) {
        // getting the two vectors that are tangent to the entities (they are opposite)
        TANGENT_VECTOR_1.setPoint(dx, dy).normalize().multiply(speed.magnitude()).rotate(90);
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
        this.aimUnitVector = side == Side.PLAYER ? new Point(0, -1) : new Point(0, +1); // nord or sud based on side
        fixPathTroughBridge();
        initDirectionBuffer();
        this.speed = new Point(aimUnitVector);
        this.direction = new Point(speed).normalize();
    }

    private void initTarget() {
        target = TowerTargetSelector.getClosestEnemyTower(this);
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


        /* 
        going from south to north:

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
            targetX = (troopX < Model.MAP_COLS / 2.0) ? leftBridgeStartX : rightBridgeStartX;
            targetY = bridgeStartY; 
        } else if (bridgeEndY < troopY && troopY < bridgeStartY 
        && targetY < bridgeEndY) {
            targetX = troopX;
            targetY = bridgeEndY; 
        }


        /* 
        going from north to south:

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
            targetX = (troopX < Model.MAP_COLS / 2.0) ? leftBridgeEndX : rightBridgeEndX;
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

    protected abstract long getLoadTime(); // nanosec in Idle state before attacking again

    protected abstract int getHitFrame();

}