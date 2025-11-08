package jroyale.model;

import java.util.HashSet;
import java.util.Set;

import jroyale.utils.Circle;
import jroyale.utils.Point;

public class CollisionManager {

    private static IModel model;

    private static Circle collisionCircle1 = new Circle();
    private static Circle collisionCircle2 = new Circle();

    public static void setModel(IModel model) {
        CollisionManager.model = model;
    }

    public static Point pushOutOfUnreachableTiles(Entity e) {
        //Point impactVector = fixInsideMap(e); 
        Point impactVector = new Point(0, 0);

        double radius = e.getCollisionRadius();
        int iTopLeftCorner = (int) Math.floor(e.getY() - radius);
        int jTopLeftCorner = (int) Math.floor(e.getX() - radius);
        int limit = e.getFootPrintSize() + 1;

        // getting impact vector value

        // case 1: unreachable tile is over entity (WORKS!!)
        for (int j = 0; j < limit; j++) {
            if (!model.isTileReachable(iTopLeftCorner, jTopLeftCorner + j)) {
                impactVector.addY(1);
                break;
            }
        }
        
        // case 2: unreachable tile is below entity (WORKS!!)
        for (int j = 0; j < limit; j++) {
            if (!model.isTileReachable(iTopLeftCorner + limit - 1, jTopLeftCorner + j)) {
                impactVector.addY(-1);
                break;
            } 
        }

        // case 3: unreachable tile is on entity's left (WORKS!!)
        for (int i = 0; i < limit; i++) { 
            if (!model.isTileReachable(iTopLeftCorner + i, jTopLeftCorner)) {
                impactVector.addX(+1);
                break;
            }
        }

        // case 4: unreachable tile is on entity's right (WORKS!!)
        for (int i = 0; i < limit; i++) { 
            if (!model.isTileReachable(iTopLeftCorner + i, jTopLeftCorner + limit - 1)) {
                impactVector.addX(-1);
                break;
            }
        } 

        if (impactVector.getX() != 0 || impactVector.getY() != 0) {
            System.out.println(impactVector);
        }
        

        // setting entity position based on impact vector
        double x = impactVector.getX();
        if (x != 0) {
            double boundX = jTopLeftCorner + x;
            if (x == -1) {
                boundX += limit;
            }
            e.setX(boundX + Math.signum(x) * radius);
        }

        double y = impactVector.getY();
        if (y != 0) {
            double boundY = iTopLeftCorner + y;
            if (y == -1) {
                boundY += limit;
            }
            e.setY(boundY + Math.signum(y) * radius);
        }

        return impactVector;

    }

    // fix entity inside map by modifying its position if it's outside.
    // returns impact vector: indicates which axes the entity hit the map boundaries.
    // For example, (1,0) means it hit vertically, (0,1) horizontally, (1,1) both axes (corner)
    // The entity can use impactVector to adjust its movement along blocked axes.
    public static Point fixInsideMap(Entity e) {

        double centerX = e.getX();
        double centerY = e.getY();
        double radius = e.getCollisionRadius();
        double limitX = model.getColsCount();
        double limitY = model.getRowsCount();
        
        Point impactVector = new Point(0, 0);   

        if (centerX - radius < 0) {
            e.setX(radius);
            impactVector.addY(1);
        } else if (centerX + radius > limitX) {
            e.setX(limitX - radius);
            impactVector.addY(1);
        }

        if (centerY - radius < 0) {
            e.setY(radius);
            impactVector.addX(1);
        } else if (centerY + radius > limitY) {
            e.setY(limitY - radius);
            impactVector.addX(1);
        }

        return impactVector;
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
