package jroyale.model;

import java.util.HashSet;
import java.util.Set;

import jroyale.model.troops.Troop;
import jroyale.utils.Point;

public class EnemyTargetSelector implements IEnemyTargetSelector{

    private static EnemyTargetSelector instance = null;

    private static final Set<Troop> enemyBuffer = new HashSet<>(); // enemy buffer to avoid new constructor for each call.
    
    // factor to convert radius to diameter
    private static final int DIAMETER_FACTOR = 2;
    // factor used to ensure an odd number for the search grid
    private static final int ODD_CONVERSION_FACTOR = 2;
    // adjustment value to obtain an odd number
    private static final int ODD_ADJUSTMENT = 1;
    // divisor used to center the search window on the troop
    private static final int CENTER_DIVISOR = 2;
    // minimum size for the search bounds
    private static final int MIN_BOUNDS_SIZE = 1;
    // value representing an empty collection size
    private static final int EMPTY_SIZE = 0;

    private EnemyTargetSelector() {}

    @Override
    public Troop getClosestEnemyInVisionRange(Troop troop) {
        enemyBuffer.clear();

        // step 1: getting the list of enemies that might be on range

        int troopI = troop.getCurrentI();
        int troopJ = troop.getCurrentJ();
        double radiusRange = troop.getVisionRange(); 
        // bounds size is ceil(diameter)
        int boundsSize = (int) Math.max(MIN_BOUNDS_SIZE, Math.ceil(radiusRange * DIAMETER_FACTOR)) * ODD_CONVERSION_FACTOR - ODD_ADJUSTMENT; 

        /* boundsSize has to be always odd. the central cell of the 
        (boundsize x boundsize) matrix contains the troop */
        int startI = troopI - boundsSize / CENTER_DIVISOR;
        int startJ = troopJ - boundsSize / CENTER_DIVISOR;
        int endI = startI + boundsSize;
        int endJ = startJ + boundsSize;

        for (int i = startI; i < endI; i++) {
            for (int j = startJ; j < endJ; j++) {
                // if it goes outside the map there won't be any error since getEntitiesOnTile returns always
                // a linked list (in this case, an empty one)
                
                for (Entity entity : Model.getInstance().getEntitiesOnTile(i, j)) {

                    double distance = Point.distance(
                        troop.getX(), troop.getY(), // troop position
                        entity.getX(), entity.getY()  // enemy position
                    ); 

                    if (entity instanceof Troop                 // is a troop
                        && entity.getSide() != troop.getSide()  // is an enemy
                        && distance <= radiusRange) {           // is inside range
                            enemyBuffer.add((Troop) entity);
                    }
                }
            }
        }

        if (enemyBuffer.size() == EMPTY_SIZE) return null; // not a single enemy found in vision range.

        // step 2: find the closest enemy in vision range
        double minDistance = Double.MAX_VALUE;
        Troop closestEnemy = null;

        for (Troop enemy : enemyBuffer) {
            double distance = Point.distance(
                troop.getX(), troop.getY(), // troop position
                enemy.getX(), enemy.getY()  // enemy position
            );

            if (distance < minDistance) {
                closestEnemy = enemy;
                minDistance = distance;
            }
        }

        return closestEnemy;

    }


    @Override
    public Set<Troop> getTroopsInMeleeRange(Troop troop) {
        enemyBuffer.clear();

        int troopI = troop.getCurrentI();
        int troopJ = troop.getCurrentJ();
        double radiusRange = troop.getMeleeRange(); 
        // bounds size is ceil(diameter)
        int boundsSize = (int) Math.max(MIN_BOUNDS_SIZE, Math.ceil(radiusRange * DIAMETER_FACTOR)) * ODD_CONVERSION_FACTOR - ODD_ADJUSTMENT; 

        /* boundsSize has to be always odd. the central cell of the 
        (boundsize x boundsize) matrix contains the troop */
        int startI = troopI - boundsSize / CENTER_DIVISOR;
        int startJ = troopJ - boundsSize / CENTER_DIVISOR;
        int endI = startI + boundsSize;
        int endJ = startJ + boundsSize;

        for (int i = startI; i < endI; i++) {
            for (int j = startJ; j < endJ; j++) {
                // if it goes outside the map there won't be any error since getEntitiesOnTile returns always
                // a linked list (in this case, an empty one)

                
                for (Entity entity : Model.getInstance().getEntitiesOnTile(i, j)) {

                    double distance = Point.distance(
                        troop.getX(), troop.getY(), // troop position
                        entity.getX(), entity.getY()  // enemy position
                    ); 

                    if (entity instanceof Troop                 // is a troop
                        && entity.getSide() != troop.getSide()  // is an enemy
                        && distance <= radiusRange) {           // is inside range
                            enemyBuffer.add((Troop) entity);
                    }
                }
            }
        }

        return enemyBuffer;
    }

    @Override
    public boolean isEntityInMeleeRange(Entity entity, Troop troop) {
        return Point.distance(
                        troop.getX(), troop.getY(), // troop position
                        entity.getX(), entity.getY()  // entity position
                    ) <= troop.getMeleeRange(); 
    }
    

    // static methods
    public static IEnemyTargetSelector getInstance() {
        if (instance == null) {
            instance = new EnemyTargetSelector();
        }
        return instance;
    }

}