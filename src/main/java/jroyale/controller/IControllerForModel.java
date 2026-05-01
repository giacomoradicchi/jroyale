package jroyale.controller;

import java.util.List;

import jroyale.model.Entity;
import jroyale.model.cards.Card;
import jroyale.model.troops.Troop;

public interface IControllerForModel {

    public void initModel(int maxTimeSec);

    public void updateModel(long now);

    public boolean isGameOver();

    public void resetModel();

    public boolean isPlayerKingTowerDestroyed();

    public boolean isOpponentKingTowerDestroyed();

    public boolean isPlayerLeftTowerDestroyed();

    public boolean isOpponentLeftTowerDestroyed();

    public boolean isPlayerRightTowerDestroyed();

    public boolean isOpponentRightTowerDestroyed();

    public int getTimeLeftSec();

    public int getNumRowsArena();

    public int getNumColsArena();

    public List<Entity> getEntitiesOrderedByPosY();

    public void addTroop(Troop troop);

    public void setSelectedPlayerCard(int cardIndex);

    public void dropSelectedPlayerCard(int row, int col);

    public boolean isPlayerEntityDroppableOnTile(int row, int col);

    public Card getFirstHandPlayerCard();

    public Card getSecondHandPlayerCard();

    public Card getThirdHandPlayerCard();

    public Card getFourthHandPlayerCard();

    public byte getPlayerElixirLeft();

    public byte getMaxElixir();

    public int getAvailableDeckCards();

    public double getPlayerElixirChargeTimeProgress();

    // static methods
    public static IControllerForModel getInstance() {
        return ControllerForModel.getInstance();
    }

} 
