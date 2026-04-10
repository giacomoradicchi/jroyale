package jroyale.model;

import java.util.List;

import jroyale.model.troops.Troop;
import jroyale.model.cards.Card;

public interface IModel {

    public void init();

    public void update(long now);

    public int getRowsCount();

    public int getColsCount();

    public void addTroop(Troop troop);

    public void setSelectedCard(int cardIndex);

    public void dropPlayerCard(int row, int col);

    public List<Entity> getEntitiesOrderedByPosY(); // necessary to enable depth in rendering

    public List<Entity> getEntitiesOnTile(int i, int j);

    public List<Entity> getOpponentEntities();

    public List<Entity> getPlayerEntities();

    public Card getFirstHandPlayerCard();

    public Card getSecondHandPlayerCard();

    public Card getThirdHandPlayerCard();

    public Card getFourthHandPlayerCard();

    public boolean isTileReachable(int i, int j);

    public boolean isPlayerEntityDroppableOnTile(int i, int j);

    public boolean[][] getPlayerDroppableTiles();

    public byte getPlayerElixirLeft();

    public byte getMaxElixir();

    public int getAvailableDeckCards();

    public double getPlayerElixirChargeTimeProgress();

    // just for debugging:
    public boolean[][] getReachableTiles();
}
