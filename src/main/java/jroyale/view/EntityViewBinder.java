package jroyale.view;

import java.util.EnumMap;
import java.util.Map;

import jroyale.utils.Enums.EntityType;
import jroyale.view.entity_view.EntityView;
import jroyale.view.entity_view.towers.ArcherTowerView;
import jroyale.view.entity_view.towers.KingTowerView;
import jroyale.view.entity_view.troops.GiantView;
import jroyale.view.entity_view.troops.MiniPekkaView;
import jroyale.view.entity_view.troops.SingleSkeletonView;
import jroyale.view.entity_view.troops.SkeletonArmyView;

public class EntityViewBinder implements IEntityViewBinder{

    private static EntityViewBinder instance = null;
    
    private final static Map<EntityType, EntityView> entityBinder = new EnumMap<>(EntityType.class);

    private EntityViewBinder() {}

    @Override
    public void init() {
        // towers
        entityBinder.put(EntityType.ARCHER_TOWER, ArcherTowerView.getInstance());
        entityBinder.put(EntityType.KING_TOWER, KingTowerView.getInstance());
        
        // troops
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
