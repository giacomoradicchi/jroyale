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
        // getting entity direction:
        Point direction = e.getDirection();
        if (direction == null || direction.isZeroVector()) { // that means that entity doesn't move, so 
            // this method won't be effective.
            return null;
        }

        // getting direction regarding each axes. value wil be in {-1, 0, 1} 
        int directionOnX = (int) Math.signum(direction.getX());
        int directionOnY = (int) Math.signum(direction.getY());

        /* 
         * in this way, it will be easier to check unreachable tiles based on direction.
         * for example, if (directionOnX, directionOnY) = (+1, +1), that means the entity 
         * is moving towards south-east. 
         *                                      x
         * based on our reference system: ------> 
         *                                |
         *                                |
         *                              y v 
         * 
         *
         * if directionX != 0 and directionY != 0, we will have to check both vertical and
         * horizontal tiles. for example, if (directionOnX, directionOnY) = (+1, +1), this method
         * has to check both tiles on his right and tiles below him. 
         *
         *
         *
         * however, to avoid 
         * problems with the corner tile that is common to both bound tiles
         *
         */

        

        Point impactVector = new Point(0, 0);

        double radius = e.getCollisionRadius();
        int iTopLeftCorner = (int) Math.floor(e.getY() - radius);
        int jTopLeftCorner = (int) Math.floor(e.getX() - radius);
        int size = e.getFootPrintSize() + 1;


        /* 
            
         * 1) find unreachable non-corner edges

         * -----------------
         * |   | x | x |   |
         * -----------------
         * | x |   |   | x |
         * -----------------
         * | x |   |   | x |    only arays with size > 2 have non-corner edges
         * -----------------
         * |   | x | x |   |
         * -----------------
         */

        impactVector.setX(getOppositeDirectionX(directionOnX, directionOnY, iTopLeftCorner, jTopLeftCorner, size));
        impactVector.setY(getOppositeDirectionY(directionOnX, directionOnY, iTopLeftCorner, jTopLeftCorner, size));

        if (impactVector.isZeroVector() && hasCommonCorner(directionOnX, directionOnY)) { 
            int commonCornerI = getCommonCornerI(directionOnY, iTopLeftCorner, size);
            int commonCornerJ = getCommonCornerJ(directionOnX, jTopLeftCorner, size);

            System.out.println(commonCornerI);

            if (!model.isTileReachable(commonCornerI, commonCornerJ)) {
                impactVector.setY(-directionOnY);
            }
        } 


        if (!impactVector.isZeroVector()) { // if an unreachable non-corner edges is found, 
            // entity position is adjusted
            applyImpactCorrection(e, impactVector, iTopLeftCorner, jTopLeftCorner, size);
        }

        // no unreachable non-corner edges found or bound size too small

        /* 
         * 2) find unreachable corner edges
         * -----------------
         * | x |   |   | x |
         * -----------------
         * |   |   |   |   |
         * -----------------
         * |   |   |   |   |    
         * -----------------
         * | x |   |   | x |
         * -----------------
         * 
         * ---------
         * | x | x | 
         * ---------
         * | x | x |   
         * ---------
         * 
         */
        // 

        
        

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

    //
    // for collision with unreachable tiles
    //

    private static void applyImpactCorrection(Entity e, Point impactVector, int iTopLeftCorner, int jTopLeftCorner, int size) {
        // setting entity position based on impact vector
        double x = impactVector.getX();
        double y = impactVector.getY();
        double radius = e.getCollisionRadius();

        if (x != 0) {
            double boundX = jTopLeftCorner + x;
            if (x == -1) {
                boundX += size;
            }
            e.setX(boundX + Math.signum(x) * radius);
        }

        
        if (y != 0) {
            double boundY = iTopLeftCorner + y;
            if (y == -1) {
                boundY += size;
            }
            e.setY(boundY + Math.signum(y) * radius);
        } 
    }

    private static int getOppositeDirectionX(int directionOnX, int directionOnY, int iTopLeftCorner, int jTopLeftCorner, int size) {
        if (unreachableTileRightEntity(iTopLeftCorner, jTopLeftCorner, size, directionOnX, directionOnY)) { // moving right
            return -1;
        }  

        if (unreachableTileLeftEntity(iTopLeftCorner, jTopLeftCorner, size, directionOnX, directionOnY)) { // moving left
            return +1;
        }
        
        return 0; 
    }

    private static int getOppositeDirectionY(int directionOnX, int directionOnY, int iTopLeftCorner, int jTopLeftCorner, int size) {
        if (unreachableTileBelowEntity(iTopLeftCorner, jTopLeftCorner, size, directionOnX, directionOnY)) // moving down
        {
            return -1;
        }
        if (unreachableTileOverEntity(iTopLeftCorner, jTopLeftCorner, size, directionOnX, directionOnY)) { // moving up
            return +1;

        } 

        return 0;
    }

    private static boolean hasCommonCorner(int directionOnX, int directionOnY) {
        return directionOnX != 0 && directionOnY != 0;
    }

    private static int getCommonCornerJ(int directionOnX, int jTopLeftCorner, int size) {
        // supposing this method is only called when hasCommonCorner = true

        if (directionOnX == +1) {
            return jTopLeftCorner + size - 1;
        }
        return jTopLeftCorner;

    }

    private static int getCommonCornerI(int directionOnY, int iTopLeftCorner, int size) {
        // supposing this method is only called when hasCommonCorner = true

        if (directionOnY == +1) {
            return iTopLeftCorner + size - 1;
        }
        return iTopLeftCorner;

    }

    // returns true if there's an unreachable tile over entity e, false otherwise. 
    // it doesn't check corners
    private static boolean unreachableTileOverEntity(int iTopLeftCorner, int jTopLeftCorner, int size, int directionOnX, int directionOnY) {
        if (directionOnY != -1) return false; // that means is not moving up

        int row = iTopLeftCorner;
        int col = jTopLeftCorner;

        int start = 0;
        if (directionOnX == -1) {
            // common corner is on bottom-left position
            start++;
        } 
        int end =  size;
        if (directionOnX == +1) {
            // common corner is on bottom-right position
            end--;
        }

        for (int j = start; j < end; j++) { 
            
            if (!model.isTileReachable(row, col + j)) {
                return true;
            }
        }

        return false;
    }

    private static boolean unreachableTileBelowEntity(int iTopLeftCorner, int jTopLeftCorner, int size, int directionOnX, int directionOnY) {
        if (directionOnY != +1) return false; // that means is not moving down


        int row = iTopLeftCorner + size - 1;
        int col = jTopLeftCorner;

        int start = 0;
        if (directionOnX == -1) {
            // common corner is on bottom-left position
            start++;
        } 
        int end =  size;
        if (directionOnX == +1) {
            // common corner is on bottom-right position
            end--;
        }

        for (int j = start; j < end; j++) { 
            
            if (!model.isTileReachable(row, col + j)) {
                return true;
            }
        }

        return false;
    }

    private static boolean unreachableTileLeftEntity(int iTopLeftCorner, int jTopLeftCorner, int size, int directionOnX, int directionOnY) {
        if (directionOnX != -1) return false; // that means is not moving on X pos

        int row = iTopLeftCorner;
        int col = jTopLeftCorner;

        int start = 0;
        if (directionOnY == -1) {
            // common corner is on top-left position
            start++;
        } 
        int end =  size;
        if (directionOnY == +1) {
            // common corner is on bottom-left position
            end--;
        }
        
        
        for (int i = start; i < end; i++) { 

            if (!model.isTileReachable(row + i, col)) {
                return true;
            }
        } 

        return false;
    }

    private static boolean unreachableTileRightEntity(int iTopLeftCorner, int jTopLeftCorner, int size, int directionOnX, int directionOnY) {
        if (directionOnX != +1) return false; // that means is not moving on X pos
        
        int row = iTopLeftCorner;
        int col = jTopLeftCorner + size - 1;

        int start = 0;
        if (directionOnX == +1 && directionOnY == -1) {
            // common corner is on top-right position
            start++;
        } 
        int end =  size;
        if (directionOnX == +1 && directionOnY == +1) {
            // common corner is on bottom-right position
            end--;
        }

        for (int i = start; i < end; i++) { 
            
            if (!model.isTileReachable(row + i, col)) {
                return true;
            }
        } 

        return false;

    }

}
