package jroyale.model;

import javafx.scene.shape.Shape;

public class CollisionManager {


    public static boolean checkCollision(Entity a, Entity b) {
        // if the intersection of the two shapes is not empty, than they intersect
        return !Shape.intersect(a.getHitbox(), b.getHitbox()).getBoundsInLocal().isEmpty();
    }
}
