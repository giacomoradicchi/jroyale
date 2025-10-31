package jroyale.model;

import java.util.List;
import java.util.ArrayList;

class Tile {
    private List<Entity> entities;

    Tile() {
        entities = new ArrayList<>();
    }

    void addEntity(Entity e) {
        entities.add(e);
    }

    List<Entity> getEntities() {
        return entities;
    }
}
