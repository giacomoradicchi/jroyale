package jroyale.model;

import java.util.List;

import jroyale.model.troops.Troop;
import jroyale.model.cards.Card;
import jroyale.model.cards.Deck;

public interface IModel {

    public void init(int maxTimeSec);

    public void update(long now);

    public boolean isGameOver();

    public void reset();
    
    public boolean isPlayerKingTowerDestroyed();

    public boolean isOpponentKingTowerDestroyed();

    public boolean isPlayerLeftTowerDestroyed();

    public boolean isOpponentLeftTowerDestroyed();

    public boolean isPlayerRightTowerDestroyed();

    public boolean isOpponentRightTowerDestroyed();

    public int getTimeLeftSec();

    public int getRowsCount();

    public int getColsCount();

    public double getTileSize();

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

    public boolean[][] getReachableTiles();
}
