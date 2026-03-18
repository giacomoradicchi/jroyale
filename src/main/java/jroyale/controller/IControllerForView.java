package jroyale.controller;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public interface IControllerForView {
    
    public void openWindow(Stage stage);

    public void initView();

    public void updateView(long now);

    public int getNumRowsArena();

    public int getNumColsArena();

    public double getDx();

    public double getDy();

    public void renderArena();

    public void renderEntity(double centreX, double centreY, int currentHealth, int maxHealth, double shadowRadius, double angleDirection, int currentFrame, State state, Side side, EntityType type);

    public void fillPoint(double centreX, double centreY, int size, Color color);

    public double logicToGraphicX(double logicX);

    public double logicToGraphicY(double logicY);

    public void handleMouseSelectedTile(int row, int col);

    public void handleMouseReleased();

    public boolean shouldRenderDragPlacementPreview();

    public void renderDragPlacementPreview(double centreX, double centreY);

    public void renderPlayerDeck(EntityType card1, byte elixirCost1, EntityType card2, byte elixirCost2, EntityType card3, byte elixirCost3, EntityType card4, byte elixirCost4, byte elixirLeft, double elixirChargeTimeProgress, byte maxElixir);

    public int getSelectedCol();

    public int getSelectedRow();

    public void setSelectedPlayerCard(int cardIndex);

    public void dropSelectedPlayerCardOnLastMousePos();
    
    public int getAvailableDeckCards();

}
