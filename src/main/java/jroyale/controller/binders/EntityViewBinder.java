package jroyale.controller.binders;

import java.util.EnumMap;
import java.util.Map;

import jroyale.utils.Enums.EntityType;
import jroyale.view.game_view.entity_view.EntityView;
import jroyale.view.game_view.entity_view.towers.ArcherTowerView;
import jroyale.view.game_view.entity_view.towers.KingTowerView;
import jroyale.view.game_view.entity_view.troops.GiantView;
import jroyale.view.game_view.entity_view.troops.MiniPekkaView;
import jroyale.view.game_view.entity_view.troops.PekkaView;
import jroyale.view.game_view.entity_view.troops.SingleSkeletonView;
import jroyale.view.game_view.entity_view.troops.SkeletonArmyView;
import jroyale.view.game_view.entity_view.troops.ValkyrieView;

public class EntityViewBinder {

    private static EntityViewBinder instance = null;
    
    private final static Map<EntityType, EntityView> entityBinder = new EnumMap<>(EntityType.class);

    private EntityViewBinder() {
        // towers
        entityBinder.put(EntityType.ARCHER_TOWER, ArcherTowerView.getInstance());
        entityBinder.put(EntityType.KING_TOWER, KingTowerView.getInstance());
        
        // troops
        entityBinder.put(EntityType.MINIPEKKA, MiniPekkaView.getInstance());
        entityBinder.put(EntityType.GIANT, GiantView.getInstance());
        entityBinder.put(EntityType.SKELETONS, SingleSkeletonView.getInstance());
        entityBinder.put(EntityType.SKELETON_ARMY, SkeletonArmyView.getInstance());
        entityBinder.put(EntityType.PEKKA, PekkaView.getInstance());
        entityBinder.put(EntityType.VALKYRIE, ValkyrieView.getInstance());
    }

    public EntityView getViewInstance(EntityType type) {
        EntityView view = entityBinder.get(type);
        if (view == null) {
            throw new IllegalArgumentException("Couldn't find any EntityView associated with: " + type);
        }
        return view;
    }

    public static EntityViewBinder getInstance() {
        if (instance == null) {
            instance = new EntityViewBinder();
        }
        return instance;
    }

}
