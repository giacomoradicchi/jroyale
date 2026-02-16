package jroyale.model;

import java.util.Set;

import jroyale.utils.Point;

public interface ICollisionManager {

    public boolean checkCollision(Entity e1, Entity e2);

    public Set<Entity> getCollidingEntitiesWith(Entity e);
    
    public Point fixEntityInsideReachableTile(Entity e, double shiftX, double shiftY);

    

}
