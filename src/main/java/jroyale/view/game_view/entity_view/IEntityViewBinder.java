package jroyale.view.game_view.entity_view;

import jroyale.utils.Enums.EntityType;

public interface IEntityViewBinder {
    
    public void init();

    public EntityView getViewInstance(EntityType type);
}
