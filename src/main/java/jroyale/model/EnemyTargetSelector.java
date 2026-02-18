package jroyale.model;

import java.util.LinkedList;
import java.util.List;

import jroyale.model.troops.Troop;
import jroyale.utils.Point;

public class EnemyTargetSelector implements IEnemyTargetSelector{

    private static EnemyTargetSelector instance = null;

    private static final List<Troop> enemyBuffer = new LinkedList<>(); // enemy buffer to avoid new constructor for each call.

    private EnemyTargetSelector() {}

    @Override
    public Troop getClosestEnemyInVisionRange(Troop troop) {
        enemyBuffer.clear();

        // step 1: getting the list of enemies that might be on range

        int troopI = troop.getCurrentI();
        int troopJ = troop.getCurrentJ();
        double radiusRange = troop.getVisionRange(); 
        int boundsSize = (int) Math.max(1, Math.ceil(radiusRange * 2)) * 2 - 1; // bounds size is ceil(diameter)

        /* boundsSize has to be always odd. the central cell of the 
        (boundsize x boundsize) matrix contains the troop */
        int startI = troopI - boundsSize/2;
        int startJ = troopJ - boundsSize/2;
        int endI = startI + boundsSize;
        int endJ = startJ + boundsSize;

        for (int i = startI; i < endI; i++) {
            for (int j = startJ; j < endJ; j++) {
                // if it goes outside the map there won't be any error since getEntitiesOnTile returns always
                // a linked list (in this case, an empty one)
                
                for (Entity entity : Model.getIstance().getEntitiesOnTile(i, j)) {

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

        if (enemyBuffer.size() == 0) return null; // not a single enemy found in vision range.

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
    public List<Troop> getTroopsInMeleeRange(Troop troop) {
        enemyBuffer.clear();

        int troopI = troop.getCurrentI();
        int troopJ = troop.getCurrentJ();
        double radiusRange = troop.getMeleeRange(); 
        int boundsSize = (int) Math.max(1, Math.ceil(radiusRange * 2)) * 2 - 1; // bounds size is ceil(diameter)

        /* boundsSize has to be always odd. the central cell of the 
        (boundsize x boundsize) matrix contains the troop */
        int startI = troopI - boundsSize/2;
        int startJ = troopJ - boundsSize/2;
        int endI = startI + boundsSize;
        int endJ = startJ + boundsSize;

        for (int i = startI; i < endI; i++) {
            for (int j = startJ; j < endJ; j++) {
                // if it goes outside the map there won't be any error since getEntitiesOnTile returns always
                // a linked list (in this case, an empty one)

                
                for (Entity entity : Model.getIstance().getEntitiesOnTile(i, j)) {

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

    // static methods
    public static IEnemyTargetSelector getInstance() {
        if (instance == null) {
            instance = new EnemyTargetSelector();
        }
        return instance;
    }
    
}
