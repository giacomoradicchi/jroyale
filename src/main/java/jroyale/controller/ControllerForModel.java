package jroyale.controller;

import java.util.List;

import jroyale.model.Entity;
import jroyale.model.Model;
import jroyale.model.troops.Giant;
import jroyale.model.troops.MiniPekka;
import jroyale.model.troops.Pekka;
import jroyale.model.troops.Skeleton;
import jroyale.model.troops.Troop;
import jroyale.model.troops.Valkyrie;
import jroyale.utils.GameData;
import jroyale.utils.Enums.EntityType;

public class ControllerForModel implements IControllerForModel{

    private static ControllerForModel instance = null;

    private ControllerForModel() {}

    // private methods
    private void initTroopsAnimationSteps() {

        MiniPekka.setTotalAnimationSteps(GameData.getInstance().getAnimationSteps(EntityType.MINIPEKKA));
        Giant.setTotalAnimationSteps(GameData.getInstance().getAnimationSteps(EntityType.GIANT));
        Skeleton.setTotalAnimationSteps(GameData.getInstance().getAnimationSteps(EntityType.SKELETON));
        Pekka.setTotalAnimationSteps(GameData.getInstance().getAnimationSteps(EntityType.PEKKA));
        Valkyrie.setTotalAnimationSteps(GameData.getInstance().getAnimationSteps(EntityType.VALKYRIE));
        
    }


    // instance methods
    @Override
    public void initModel() {
        Model.getIstance().init();

        initTroopsAnimationSteps();
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

    @Override
    public boolean isPlayerEntityDroppableOnTile(int row, int col) {
        return Model.getIstance().isPlayerEntityDroppableOnTile(row, col);
    }

    // static methods
    public static IControllerForModel getInstance() {
        if (instance == null) {
            instance = new ControllerForModel();
        }
        return instance;
    }
    
}
