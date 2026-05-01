package jroyale.view.game_view;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.IView;
import jroyale.view.game_view.entity_view.EntityView;

public interface IGameView extends IView {

    public double getDx();

    public double getDy();

    public double getWorldMapTopLeftCornerX();

    public double getWorldMapTopLeftCornerY();

    public double getScreenMapTopLeftCornerX();

    public double getScreenMapTopLeftCornerY();

    public double getScreenMapWidth();

    public double getScreenMapHeight();

    public void renderArena();

    public void renderEntity(EntityView entity, double centreX, double centreY, int currentHealth, int maxHealth, double shadowRadius, double angleDirection, int currentFrame, State state, Side side);

    public void renderTimeLeft(int secondsLeft, double alpha);

    public void startDragPlacementPreview();

    public void renderDragPlacementPreview(double centreX, double centreY);

    public void stopDragPlacementPreview();

    public void renderPlayerDeck(EntityType card1, byte elixirCost1, EntityType card2, byte elixirCost2, EntityType card3, byte elixirCost3, EntityType card4, byte elixirCost4, byte elixirLeft, double elixirChargeTimeProgress, byte maxElixir, double alpha);

    public void setSelectedCard(int cardIndex);

    public static IGameView getInstance() {
        return GameView.getInstance();
    }
}
