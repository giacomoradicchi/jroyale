package jroyale.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jroyale.model.Entity;
import jroyale.model.IModel;
import jroyale.model.Model;
import jroyale.model.troops.Giant;
import jroyale.model.troops.MiniPekka;
import jroyale.model.troops.Skeleton;
import jroyale.model.troops.Troop;
import jroyale.shared.Enums.State;
import jroyale.view.troops.GiantView;
import jroyale.view.troops.MiniPekkaView;
import jroyale.view.troops.SingleSkeletonView;
import jroyale.view.troops.TroopView;

public class ControllerForModel implements IControllerForModel{

    private static IControllerForModel instance = null;

    // private methods
    private void initModelTroopsFrames() {

        
        MiniPekka.setFramesPerDirection(getNumFrames(MiniPekkaView.getInstance()));
        Giant.setFramesPerDirection(getNumFrames(GiantView.getInstance()));
        Skeleton.setFramesPerDirection(getNumFrames(SingleSkeletonView.getInstance()));
    }

    private Map<State, Integer> getNumFrames(TroopView type) {
        Map<State, Integer> numFrames = new HashMap<>();
        for (State state : State.values()) {
            numFrames.put(
                state, 
                ControllerForView.getInstance().getNumFramesPerDirection(type, state)
            );
        }

        return numFrames;
    }

    // instance methods
    @Override
    public void initModel() {
        Model.getIstance().init();

        initModelTroopsFrames();
    }

    @Override
    public void updateModel(long now) {
        Model.getIstance().update(now);
    }

    @Override
    public int getNumRowsArena() {
        return Model.getIstance().getRowsCount();
    }

    @Override
    public int getNumColsArena() {
        return Model.getIstance().getColsCount();
    }

    @Override
    public List<Entity> getEntitiesOrderedByPosY() {
        return Model.getIstance().getEntitiesOrderedByPosY();
    }

    @Override
    public void addTroop(Troop troop) {
        Model.getIstance().addTroop(troop);
    }

    // static methods
    public static IControllerForModel getInstance() {
        if (instance == null) {
            instance = new ControllerForModel();
        }
        return instance;
    }
    
}
