package jroyale.controller;

import java.util.List;

import jroyale.model.Entity;
import jroyale.model.troops.Troop;

public interface IControllerForModel {

    public void initModel();

    public void updateModel(long now);

    public int getNumRowsArena();

    public int getNumColsArena();

    public List<Entity> getEntitiesOrderedByPosY();

    public void addTroop(Troop troop);
} 
