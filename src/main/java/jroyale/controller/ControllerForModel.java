package jroyale.controller;

import java.util.List;

import jroyale.controller.binders.CardBinder;
import jroyale.model.Entity;
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
import jroyale.utils.Enums.Side;

public class ControllerForModel implements IControllerForModel{

    private static ControllerForModel instance = null;

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
    public void initModel() {
        setCardsStats();
        Model.getInstance().init();

        initTroopsAnimationSteps();
        
    }

    @Override
    public void updateModel(long now) {
        Model.getInstance().update(now);
    }

    @Override
    public int getNumRowsArena() {
        return Model.getInstance().getRowsCount();
    }

    @Override
    public int getNumColsArena() {
        return Model.getInstance().getColsCount();
    }

    @Override
    public List<Entity> getEntitiesOrderedByPosY() {
        return Model.getInstance().getEntitiesOrderedByPosY();
    }

    @Override
    public void addTroop(Troop troop) {
        Model.getInstance().addTroop(troop);
    }

    @Override
    public void setSelectedPlayerCard(int cardIndex) {
        Model.getInstance().setSelectedCard(cardIndex);
    }

    @Override
    public void dropSelectedPlayerCard(int row, int col) {
        Model.getInstance().dropPlayerCard(row, col);
    }

    @Override
    public boolean isPlayerEntityDroppableOnTile(int row, int col) {
        return Model.getInstance().isPlayerEntityDroppableOnTile(row, col);
    }

    @Override
    public Card getFirstHandPlayerCard() {
        return Model.getInstance().getFirstHandPlayerCard();
    }

    @Override
    public Card getSecondHandPlayerCard() {
        return Model.getInstance().getSecondHandPlayerCard();
    }

    @Override
    public Card getThirdHandPlayerCard() {
        return Model.getInstance().getThirdHandPlayerCard();
    }

    @Override
    public Card getFourthHandPlayerCard() {
        return Model.getInstance().getFourthHandPlayerCard();
    }

    @Override
    public byte getPlayerElixirLeft() {
        return Model.getInstance().getPlayerElixirLeft();
    }

    @Override
    public byte getMaxElixir() {
        return Model.getInstance().getMaxElixir();
    }

    @Override
    public int getAvailableDeckCards() {
        return Model.getInstance().getAvailableDeckCards();
    }

    @Override
    public double getPlayerElixirChargeTimeProgress() {
        return Model.getInstance().getPlayerElixirChargeTimeProgress();
    }

    // static methods
    public static IControllerForModel getInstance() {
        if (instance == null) {
            instance = new ControllerForModel();
        }
        return instance;
    }
    
}
