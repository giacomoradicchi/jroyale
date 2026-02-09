package jroyale.view;

import java.util.EnumMap;
import java.util.Map;

import jroyale.shared.Enums.EntityType;
import jroyale.view.troops.GiantView;
import jroyale.view.troops.MiniPekkaView;
import jroyale.view.troops.SingleSkeletonView;
import jroyale.view.troops.SkeletonArmyView;

public class EntityViewBinder implements IEntityViewBinder{

    private static EntityViewBinder instance = null;
    
    private final static Map<EntityType, EntityView> entityBinder = new EnumMap<>(EntityType.class);

    private EntityViewBinder() {}

    @Override
    public void init() {
        entityBinder.put(EntityType.MINIPEKKA, MiniPekkaView.getInstance());
        entityBinder.put(EntityType.GIANT, GiantView.getInstance());
        entityBinder.put(EntityType.SKELETON, SingleSkeletonView.getInstance());
        entityBinder.put(EntityType.SKELETON_ARMY, SkeletonArmyView.getInstance());
    }

    @Override
    public EntityView getViewInstance(EntityType type) {
        EntityView view = entityBinder.get(type);
        if (view == null) {
            throw new IllegalArgumentException("Couldn't find any EntityView associated with: " + type);
        }
        return view;
    }

    public static IEntityViewBinder getInstance() {
        if (instance == null) {
            instance = new EntityViewBinder();
        }
        return instance;
    }

}
