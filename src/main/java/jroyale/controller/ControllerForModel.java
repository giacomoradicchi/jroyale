package jroyale.controller;

import java.util.List;

import jroyale.controller.binders.CardBinder;
import jroyale.model.Entity;
import jroyale.model.IModel;
import jroyale.model.Model;
import jroyale.model.cards.Card;
import jroyale.model.cards.CardStats;
import jroyale.model.troops.Giant;
import jroyale.model.troops.MiniPekka;
import jroyale.model.troops.Pekka;
import jroyale.model.troops.Skeleton;
import jroyale.model.troops.Troop;
import jroyale.model.troops.Valkyrie;
import jroyale.utils.Config;
import jroyale.utils.GameData;
import jroyale.utils.Enums.EntityType;

public class ControllerForModel implements IControllerForModel{

    private static ControllerForModel instance = null;
    private final IModel model = Model.getInstance();

    private ControllerForModel() {}


    // private methods
    private void initTroopsAnimationSteps() {

        MiniPekka.setTotalAnimationSteps(GameData.getInstance().getAnimationSteps(EntityType.MINIPEKKA));
        Giant.setTotalAnimationSteps(GameData.getInstance().getAnimationSteps(EntityType.GIANT));
        Skeleton.setTotalAnimationSteps(GameData.getInstance().getAnimationSteps(EntityType.SKELETONS));
        Pekka.setTotalAnimationSteps(GameData.getInstance().getAnimationSteps(EntityType.PEKKA));
        Valkyrie.setTotalAnimationSteps(GameData.getInstance().getAnimationSteps(EntityType.VALKYRIE));
        
    }

    private void setCardsStats() {
        for (EntityType type : EntityType.values()) {

            Card card = CardBinder.getInstance().getCardInstance(type);
            if (card == null) continue;

            CardStats stats = Config.getInstance().getCardStats(type);

            card.setStats(stats);
        }
    }


    // instance methods
    @Override
    public void initModel(int maxTimeSec) {
        setCardsStats();
        model.init(maxTimeSec);

        initTroopsAnimationSteps();
        CardBinder.getInstance();
    }

    @Override
    public int getTimeLeftSec() {
        return model.getTimeLeftSec();
    }

    @Override
    public void updateModel(long now) {
        model.update(now);
    }

    @Override
    public boolean isGameOver() {
        return model.isGameOver();
    }

    @Override
    public void resetModel() {
        model.reset();
    }

    @Override
    public boolean isPlayerKingTowerDestroyed() {
        return model.isPlayerKingTowerDestroyed();
    }

    @Override
    public boolean isOpponentKingTowerDestroyed() {
        return model.isOpponentKingTowerDestroyed();
    }

    @Override
    public boolean isPlayerLeftTowerDestroyed() {
        return model.isPlayerLeftTowerDestroyed();
    }

    @Override
    public boolean isOpponentLeftTowerDestroyed() {
        return model.isOpponentLeftTowerDestroyed();
    }

    @Override
    public boolean isPlayerRightTowerDestroyed() {
        return model.isPlayerRightTowerDestroyed();
    }

    @Override
    public boolean isOpponentRightTowerDestroyed() {
        return model.isOpponentRightTowerDestroyed();
    }

    @Override
    public int getNumRowsArena() {
        return model.getRowsCount();
    }

    @Override
    public int getNumColsArena() {
        return model.getColsCount();
    }

    @Override
    public List<Entity> getEntitiesOrderedByPosY() {
        return model.getEntitiesOrderedByPosY();
    }

    @Override
    public void addTroop(Troop troop) {
        model.addTroop(troop);
    }

    @Override
    public void setSelectedPlayerCard(int cardIndex) {
        model.setSelectedCard(cardIndex);
    }

    @Override
    public void dropSelectedPlayerCard(int row, int col) {
        model.dropPlayerCard(row, col);
    }

    @Override
    public boolean isPlayerEntityDroppableOnTile(int row, int col) {
        return model.isPlayerEntityDroppableOnTile(row, col);
    }

    @Override
    public Card getFirstHandPlayerCard() {
        return model.getFirstHandPlayerCard();
    }

    @Override
    public Card getSecondHandPlayerCard() {
        return model.getSecondHandPlayerCard();
    }

    @Override
    public Card getThirdHandPlayerCard() {
        return model.getThirdHandPlayerCard();
    }

    @Override
    public Card getFourthHandPlayerCard() {
        return model.getFourthHandPlayerCard();
    }

    @Override
    public byte getPlayerElixirLeft() {
        return model.getPlayerElixirLeft();
    }

    @Override
    public byte getMaxElixir() {
        return model.getMaxElixir();
    }

    @Override
    public int getAvailableDeckCards() {
        return model.getAvailableDeckCards();
    }

    @Override
    public double getPlayerElixirChargeTimeProgress() {
        return model.getPlayerElixirChargeTimeProgress();
    }

    // static methods
    public static IControllerForModel getInstance() {
        if (instance == null) {
            instance = new ControllerForModel();
        }
        return instance;
    }
    
}
