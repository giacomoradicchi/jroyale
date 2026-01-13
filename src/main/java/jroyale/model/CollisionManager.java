package jroyale.model;

import java.util.HashSet;
import java.util.Set;

import jroyale.utils.Circle;
import jroyale.utils.Point;

public class CollisionManager {

    private static IModel model;

    // buffer variables to avoid "new" constructor each time
    private static Circle collisionCircle1 = new Circle();
    private static Circle collisionCircle2 = new Circle();
    private static Point bufferedPoint = new Point();

    public static void setModel(IModel model) {
        CollisionManager.model = model;
    }

    public static Point fixEntityInsideReachableTile(Entity e, double shiftX, double shiftY) {
        // making sure abs(shiftX) and abs(shiftY) are less than 1, so there won't be unchecked tiles in the middle
        double biggestCoord = Math.max(Math.abs(shiftX), Math.abs(shiftY));
        if (biggestCoord > 1) {
            shiftX /= biggestCoord;
            shiftY /= biggestCoord;
        }

        int directionOnX = (int) Math.signum(shiftX);
        int directionOnY = (int) Math.signum(shiftY);

        double radius = e.getCollisionRadius();
        double newX = e.getX() + shiftX;
        double newY = e.getY() + shiftY;
        
        int currentI = e.getCurrentI();
        int currentJ = e.getCurrentJ();
        int nextI = (int) Math.floor(newY + directionOnY * radius);
        int nextJ = (int) Math.floor(newX + directionOnX * radius);
        boolean foundUnreachableTile = false;
        
        /* 
         *          -------------        
         *          | b | y | b |
         *          -------------
         *          | x |   | x | 
         *          ------------- 
         *          | b | y | b | 
         *          ------------- 
         * 
         *          x : tiles to check on X based on directionX
         *          y : tiles to check on Y based on directionY
         *          b : tiles to check on both X and Y based on direction and shift values
         * 
        */

        // checking X

        if (!model.isTileReachable(currentI, nextJ)) {
            newX = resolveCollisionCoordinate(directionOnX, nextJ, radius);
            foundUnreachableTile = true;
        } 

        // checking Y
        if (!model.isTileReachable(nextI, currentJ)) {
            newY = resolveCollisionCoordinate(directionOnY, nextI, radius);
            foundUnreachableTile = true;
        } 

        // checking both if not found on specific direction
        if (!foundUnreachableTile && !model.isTileReachable(nextI, nextJ)) {
            if (shiftX > shiftY) { 
                // fixingOnY
                newY = resolveCollisionCoordinate(directionOnY, nextI , radius);
            } else { 
                // fixingOnX
                newX = resolveCollisionCoordinate(directionOnX, nextJ, radius);
            }
        } 

        bufferedPoint.setPoint(newX, newY);
        return bufferedPoint;
    }

    private static double resolveCollisionCoordinate(int direction, int nextTile, double radius) {
        double newCoordinate = 0;
        if (direction == +1) {
            newCoordinate = nextTile - radius;
        } else if (direction == -1) {
            newCoordinate = nextTile + 1 + radius;
        }
        return newCoordinate;
    }

    public static boolean isTileReachable(int i, int j) {
        return model.isTileReachable(i, j);
    }

    // using Set instead of List to avoid duplicates
    public static Set<Entity> checkCollisions(Entity e) {
        // getting list of sorrounding entities that might collide with entity e
        Set<Entity> possibleEntities = getPossibleCollidingEntities(e);

        //System.out.println(possibleEntities.size());

        if (possibleEntities.size() == 0) { // no entities found
            
            return possibleEntities;
        }

        Set<Entity> collidingEntities = new HashSet<>();

        for (Entity possibleEntity : possibleEntities) {
            if (checkCollision(e, possibleEntity)) {
                collidingEntities.add(possibleEntity);
            }
        }

        return collidingEntities;
    }

    //
    // private methods
    //  

    private static Set<Entity> getPossibleCollidingEntities(Entity e) {
        Set<Entity> foundEntities = new HashSet<>();
        // even though there might be some duplicates in the map, since we're using
        // set instead of list, there won't be any duplicate.

        int side = e.getFootPrintSize();

        // checking also nearby tiles:
        /* 
         * -----------------
         * | n | n | n | n |        n = nearby cells
         * -----------------
         * | n | e | e | n |        e = tiles occupied by entity
         * -----------------
         * | n | e | e | n |
         * -----------------
         * | n | n | n | n |
         * -----------------
         * 
         * this new matrix will have two more rows and two more cols:
         */
        int iStart = e.getCurrentI() - side / 2 - 1;
        int jStart = e.getCurrentJ() - side / 2 - 1;
        int limit = side + 2; 

        for (int i = 0; i <= limit; i++) {
            for (int j = 0; j <= limit; j++) {
                foundEntities.addAll(model.getEntitiesOnTile(iStart + i, jStart + j));
            }
        }


        foundEntities.remove(e); // removing entity e from the list of the 
        // possible entities that might collide with e



        return foundEntities;
    }

    private static boolean checkCollision(Entity a, Entity b) {
        // if the intersection of the two shapes is not empty, than they intersect

        // initializing first circle
        setCollisionCircle1(a.getX(), a.getY(), a.getCollisionRadius());

        // initializing second circle
        setCollisionCircle2(b.getX(), b.getY(), b.getCollisionRadius());

        return collisionCircle1.collides(collisionCircle2);
    }

    private static void setCollisionCircle1(double x, double y, double radius) {
        collisionCircle1.setCenterX(x);
        collisionCircle1.setCenterY(y);
        collisionCircle1.setRadius(radius);
    }

    private static void setCollisionCircle2(double x, double y, double radius) {
        collisionCircle2.setCenterX(x);
        collisionCircle2.setCenterY(y);
        collisionCircle2.setRadius(radius);
    }
}
