package jroyale.controller;

import java.util.List;

import jroyale.model.Entity;
import jroyale.model.cards.Card;
import jroyale.model.troops.Troop;
import jroyale.utils.Enums.EntityType;

public interface IControllerForModel {

    public void initModel();

    public void updateModel(long now);

    public int getNumRowsArena();

    public int getNumColsArena();

    public List<Entity> getEntitiesOrderedByPosY();

    public void addTroop(Troop troop);

    public void dropPlayerEntity(int row, int col, EntityType type);

    public boolean isPlayerEntityDroppableOnTile(int row, int col);

    public Card getFirstHandPlayerCard();

    public Card getSecondHandPlayerCard();

    public Card getThirdHandPlayerCard();

    public Card getFourthHandPlayerCard();
} 
