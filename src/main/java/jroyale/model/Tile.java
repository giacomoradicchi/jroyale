package jroyale.model;

import java.util.List;
import java.util.ArrayList;

public class Tile {
    private List<Entity> entities;

    public Tile() {
        entities = new ArrayList<>();
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public boolean removeEntity(Entity e) {
        return entities.remove(e);
    }

    public List<Entity> getEntities() {
        return entities;
    }    
}
