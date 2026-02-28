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

    public List<Entity> getEntitiesOrderedByPosY(); // necessary to enable depth in rendering

    public List<Entity> getEntitiesOnTile(int i, int j);

    public Card getFirstHandPlayerCard();

    public Card getSecondHandPlayerCard();

    public Card getThirdHandPlayerCard();

    public Card getFourthHandPlayerCard();

    public boolean isTileReachable(int i, int j);

    public boolean isPlayerEntityDroppableOnTile(int i, int j);

    public boolean[][] getPlayerDroppableTiles();

    // just for debugging:
    public boolean[][] getReachableTiles();
}
