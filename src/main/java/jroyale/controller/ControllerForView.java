package jroyale.controller;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jroyale.utils.GameData;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.IView;
import jroyale.view.View;
import jroyale.view.entity_view.troops.GiantView;
import jroyale.view.entity_view.troops.MiniPekkaView;
import jroyale.view.entity_view.troops.PekkaView;
import jroyale.view.entity_view.troops.SkeletonView;
import jroyale.view.entity_view.troops.ValkyrieView;

public class ControllerForView implements IControllerForView {

    private static ControllerForView instance;

    private IView view;

    private int lastSelectedColumnIndex = -1;
    private int lastSelectedRowIndex = -1;

    private ControllerForView() {
        // empty
    }

    // private methods
    private void initTroopsFramesPerDirection() {
        GiantView.setNumFramesPerDirection(GameData.getInstance().getAnimationSteps(EntityType.GIANT));
        MiniPekkaView.setNumFramesPerDirection(GameData.getInstance().getAnimationSteps(EntityType.MINIPEKKA));
        SkeletonView.setNumFramesPerDirection(GameData.getInstance().getAnimationSteps(EntityType.SKELETONS));
        PekkaView.setNumFramesPerDirection(GameData.getInstance().getAnimationSteps(EntityType.PEKKA));
        ValkyrieView.setNumFramesPerDirection(GameData.getInstance().getAnimationSteps(EntityType.VALKYRIE));
    }

    // instance methods
    
    @Override
    public void openWindow(Stage stage) {
        view = View.getInstance();
        view.openWindow(stage);
    }

    @Override
    public void initView() {
        initTroopsFramesPerDirection();
        view.init();

    }

    @Override
    public void updateView(long now) {
        view.update(now);
    }

    @Override
    public int getNumRowsArena() {
        return ControllerForModel.getInstance().getNumRowsArena();
    }

    @Override
    public int getNumColsArena() {
        return ControllerForModel.getInstance().getNumColsArena();
    }

    @Override
    public double getDx() {
        return view.getDx();
    }

    @Override
    public double getDy() {
        return view.getDy();
    }

    @Override
    public void renderArena() {
        view.renderArena();
    }

    @Override
    public void renderEntity(double centreX, double centreY, int currentHealth, int maxHealth, double shadowRadius, double angleDirection, int currentFrame, State state,
            Side side, EntityType type) {
        view.renderEntity(centreX, centreY, currentHealth, maxHealth, shadowRadius, angleDirection, currentFrame, state, side, type);
    }

    @Override
    public void renderTimeLeft(int secondsLeft) {
        view.renderTimeLeft(secondsLeft);
    }

    @Override
    public void fillPoint(double centreX, double centreY, int size, Color color) {
        View.getInstance().fillPoint(centreX, centreY, size, color);
    }

    @Override
    public double logicToGraphicX(double logicCoordX) {
        return view.getWorldMapTopLeftCornerX() + logicCoordX * view.getDx();
    }

    @Override
    public double logicToGraphicY(double logicCoordY) {
        return view.getWorldMapTopLeftCornerY() + logicCoordY * view.getDy();
    }

    @Override
    public void handleMouseSelectedTile(int row, int col) {

        // 1. Valid Position 
        if (ControllerForModel.getInstance().isPlayerEntityDroppableOnTile(row, col)) {
            if (!isPositionValid()) View.getInstance().startDragPlacementPreview();

            lastSelectedColumnIndex = col;
            lastSelectedRowIndex = row;
            return;
            
        } 
        
        // 2. Position outside bounds
        if(row < 0 || row >= ControllerForModel.getInstance().getNumRowsArena()
               || col < 0 || col >= ControllerForModel.getInstance().getNumColsArena()) {
            resetLastSelectedTile();
            View.getInstance().stopDragPlacementPreview();
            return;
        }

        // 3. Position inside bounds but (row, col) not droppable

        // Tries the horizontal slide (based on previous row)
        if (ControllerForModel.getInstance().isPlayerEntityDroppableOnTile(lastSelectedRowIndex, col)) {
            lastSelectedColumnIndex = col;
        }

        // Tries the vertical slide (based on previous col)
        if (ControllerForModel.getInstance().isPlayerEntityDroppableOnTile(row, lastSelectedColumnIndex)) {
            lastSelectedRowIndex = row;
        } 
        
    }

    @Override
    public void handleMouseReleased() {
        // TODO: drop selected troop
        resetLastSelectedTile();
        View.getInstance().stopDragPlacementPreview();
    }

    private void resetLastSelectedTile() {
        lastSelectedColumnIndex = -1;
        lastSelectedRowIndex = -1;
    }

    @Override
    public boolean shouldRenderDragPlacementPreview() {
        return isPositionValid();
    }

    @Override
    public void renderDragPlacementPreview(double centreX, double centreY) {
        View.getInstance().renderDragPlacementPreview(centreX, centreY);
    }

    @Override
    public void renderPlayerDeck(EntityType card1, byte elixirCost1, EntityType card2, byte elixirCost2, EntityType card3, byte elixirCost3, EntityType card4, byte elixirCost4, byte elixirLeft, double elixirChargeTimeProgress, byte maxElixir) {
        View.getInstance().renderPlayerDeck(card1, elixirCost1, card2, elixirCost2, card3, elixirCost3, card4, elixirCost4, elixirLeft, elixirChargeTimeProgress, maxElixir);
    }

    private boolean isPositionValid() {
        return lastSelectedColumnIndex != -1 && lastSelectedRowIndex != -1;
    }

    @Override
    public int getSelectedCol() {
        return lastSelectedColumnIndex;
    }

    @Override
    public int getSelectedRow() {
        return lastSelectedRowIndex;
    }

    @Override
    public void setSelectedPlayerCard(int cardIndex) {
        ControllerForModel.getInstance().setSelectedPlayerCard(cardIndex);
    }

    @Override
    public void dropSelectedPlayerCardOnLastMousePos() {
        if (isPositionValid())
            ControllerForModel.getInstance().dropSelectedPlayerCard(lastSelectedRowIndex, lastSelectedColumnIndex);
        
    }

    @Override
    public int getAvailableDeckCards() {
        return ControllerForModel.getInstance().getAvailableDeckCards();
    }

    // static methods
    public static IControllerForView getInstance() {
        if (instance == null) {
            instance = new ControllerForView();
        }

        return instance;
    }
    
}
