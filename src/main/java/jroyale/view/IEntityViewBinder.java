package jroyale.view;

import jroyale.utils.Enums.EntityType;
import jroyale.view.entity_view.EntityView;

public interface IEntityViewBinder {
    
    public void init();

    public EntityView getViewInstance(EntityType type);
}
