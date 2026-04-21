package jroyale.view.game_view;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.MainGUI;

public class GameView extends MainGUI implements IGameView {
    
    private static IGameView instance = null;

    protected GameView() {}

    // static methods
    public static IGameView getInstance() {
        if (instance == null) {
            instance = new GameView();
        }

        return instance;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'init'");
    }

    @Override
    public void update(long now) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public double getDx() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDx'");
    }

    @Override
    public double getDy() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDy'");
    }

    @Override
    public double getWorldMapTopLeftCornerX() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWorldMapTopLeftCornerX'");
    }

    @Override
    public double getWorldMapTopLeftCornerY() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWorldMapTopLeftCornerY'");
    }

    @Override
    public double getScreenMapTopLeftCornerX() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getScreenMapTopLeftCornerX'");
    }

    @Override
    public double getScreenMapTopLeftCornerY() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getScreenMapTopLeftCornerY'");
    }

    @Override
    public double getScreenMapWidth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getScreenMapWidth'");
    }

    @Override
    public double getScreenMapHeight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getScreenMapHeight'");
    }

    @Override
    public void renderArena() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renderArena'");
    }

    @Override
    public void renderEntity(double centreX, double centreY, int currentHealth, int maxHealth, double shadowRadius,
            double angleDirection, int currentFrame, State state, Side side, EntityType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renderEntity'");
    }

    @Override
    public void renderWorldShadow(double centreX, double centreY, double shadowRadius) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renderWorldShadow'");
    }

    @Override
    public void renderTimeLeft(int secondsLeft, double alpha) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renderTimeLeft'");
    }

    @Override
    public void startDragPlacementPreview() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startDragPlacementPreview'");
    }

    @Override
    public void renderDragPlacementPreview(double centreX, double centreY) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renderDragPlacementPreview'");
    }

    @Override
    public void stopDragPlacementPreview() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stopDragPlacementPreview'");
    }

    @Override
    public void renderPlayerDeck(EntityType card1, byte elixirCost1, EntityType card2, byte elixirCost2,
            EntityType card3, byte elixirCost3, EntityType card4, byte elixirCost4, byte elixirLeft,
            double elixirChargeTimeProgress, byte maxElixir, double alpha) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renderPlayerDeck'");
    }

    @Override
    public void setSelectedCard(int cardIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSelectedCard'");
    }

    @Override
    public void processOnMousePressed(double x, double y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processOnMousePressed'");
    }

    @Override
    public void processOnMouseDragged(double x, double y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processOnMouseDragged'");
    }

    @Override
    public void processOnMouseReleased() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processOnMouseReleased'");
    }
}
