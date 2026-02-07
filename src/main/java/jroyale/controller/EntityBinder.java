package jroyale.controller;

import java.util.HashMap;
import java.util.Map;

import jroyale.model.Entity;
import jroyale.model.troops.Giant;
import jroyale.model.troops.MiniPekka;
import jroyale.model.troops.Skeleton;
import jroyale.model.troops.Troop;
import jroyale.view.View;
import jroyale.view.View.TroopType;

public class EntityBinder implements IEntityBinder {
    
    private Map<Class<? extends Troop>, TroopType> troopBinder = new HashMap<>();

    private static IEntityBinder instance = null;

    private EntityBinder() {}

    // instance methods
    @Override
    public void init() {
        troopBinder.put(MiniPekka.class, View.TroopType.MINI_PEKKA);
        troopBinder.put(Giant.class, View.TroopType.GIANT);
        troopBinder.put(Skeleton.class, View.TroopType.SKELETON);
    }

    @Override
    public TroopType getEntityTypeView(Class<? extends Entity> e) {
        return troopBinder.get(e);
    }

    // static methods
    public static IEntityBinder getInstance() {
        if (instance == null) {
            instance = new EntityBinder();
        }
        return instance;
    }
}
