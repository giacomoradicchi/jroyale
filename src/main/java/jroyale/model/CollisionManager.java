package jroyale.model;

import java.util.HashSet;
import java.util.Set;

import jroyale.utils.Circle;

public class CollisionManager {

    private static IModel model;

    private static Circle collisionCircle1 = new Circle();
    private static Circle collisionCircle2 = new Circle();

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
        collisionCircle1.setCenterX(a.getX());
        collisionCircle1.setCenterY(a.getY());
        collisionCircle1.setRadius(a.getCollisionRadius());

        // initializing second circle
        collisionCircle2.setCenterX(b.getX());
        collisionCircle2.setCenterY(b.getY());
        collisionCircle2.setRadius(b.getCollisionRadius());

        return collisionCircle1.collides(collisionCircle2);
    }

    public static void setModel(IModel model) {
        CollisionManager.model = model;
    }
}
