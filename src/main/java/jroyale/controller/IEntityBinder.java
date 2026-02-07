package jroyale.controller;

import jroyale.model.Entity;
import jroyale.view.View.TroopType;

public interface IEntityBinder {
    
    public void init();

    public TroopType getEntityTypeView(Class<? extends Entity> e);
}
