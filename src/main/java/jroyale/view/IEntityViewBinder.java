package jroyale.view;

import jroyale.shared.Enums.EntityType;

public interface IEntityViewBinder {
    
    public void init();

    public EntityView getViewInstance(EntityType type);
}
