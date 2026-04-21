package jroyale.controller;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jroyale.utils.GameData;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.FontManager;
import jroyale.view.game_view.GameView;
import jroyale.view.game_view.IGameView;
import jroyale.view.game_view.entity_view.troops.GiantView;
import jroyale.view.game_view.entity_view.troops.MiniPekkaView;
import jroyale.view.game_view.entity_view.troops.PekkaView;
import jroyale.view.game_view.entity_view.troops.SkeletonView;
import jroyale.view.game_view.entity_view.troops.ValkyrieView;

public class ControllerForView implements IControllerForView {

    private static ControllerForView instance;

    private IGameView gameView;

    private IControllerForModel controllerForModel;

    private int lastSelectedColumnIndex = -1;
    private int lastSelectedRowIndex = -1;
    private long initialTimeGameOver = -1;
    private long timePassedSinceGameOver;
    private double initialGlobalScaleSinceGameOver;

    private static final long FADE_OUT_DURATION_NANOSEC = 250_000_000L;
    private static final long ZOOM_OUT_DURATION_NANOSEC = 2_000_000_000L;
    private static final double MIN_GLOBAL_SCALE = 0.92;
    private static final double SMOOTHNESS_CURVE = 2; // defines how smoothly the curve will go from 1 to 0

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
        gameView = GameView.getInstance();
        controllerForModel = ControllerForModel.getInstance();
        gameView.openWindow(stage);
    }

    @Override
    public void initView() {
        initTroopsFramesPerDirection();
        gameView.init();

    }

    @Override
    public void updateView(long now) {
        gameView.update(now);

        if (!controllerForModel.isGameOver()) return;

        //
        // game over
        //

        if (initialTimeGameOver == -1) {
            initialTimeGameOver = now;
            initialGlobalScaleSinceGameOver = gameView.getGlobalScale();
            resetLastSelectedTile();
        }

        timePassedSinceGameOver = now - initialTimeGameOver;
        updateGlobalScale();
    }

    private void updateGlobalScale() {
        // returns if has passed no time since game over or view.globalScale is already at its minimum
        if (timePassedSinceGameOver == 0 || gameView.getGlobalScale() == MIN_GLOBAL_SCALE) return;

        // smooth change

        double timeRatio = (double) timePassedSinceGameOver / ZOOM_OUT_DURATION_NANOSEC;
        double smoothFactor = smoothnessFunction(timeRatio);
        double globalScale = smoothFactor * initialGlobalScaleSinceGameOver + (1 - smoothFactor) * MIN_GLOBAL_SCALE; // linear interpolation using smooth factor
        gameView.setGlobalScale(globalScale);
    }

    private boolean isGameOver() {
        return initialTimeGameOver != -1;
    }

    @Override
    public int getNumRowsArena() {
        return controllerForModel.getNumRowsArena();
    }

    @Override
    public int getNumColsArena() {
        return controllerForModel.getNumColsArena();
    }

    @Override
    public double getDx() {
        return gameView.getDx();
    }

    @Override
    public double getDy() {
        return gameView.getDy();
    }

    @Override
    public void renderArena() {
        gameView.renderArena();
    }

    @Override
    public void renderEntity(double centreX, double centreY, int currentHealth, int maxHealth, double shadowRadius, double angleDirection, int currentFrame, State state,
            Side side, EntityType type) {
        gameView.renderEntity(centreX, centreY, currentHealth, maxHealth, shadowRadius, angleDirection, currentFrame, state, side, type);
    }

    @Override
    public void renderTimeLeft(int secondsLeft) {
        double alpha = getAlphaBasedGameOver();

        if (alpha > 0) gameView.renderTimeLeft(secondsLeft, alpha);
    }

    @Override
    public void renderGameOver() {
        String gameOverText = getGameOverText("Vittoria!", "Pareggio!", "Sconfitta...");

        gameView.fillScreenTextFromCenter(gameOverText, gameView.getCanvasWidth()/2, gameView.getCanvasHeight()/2, FontManager.getInstance().getBoldFont(50), Color.ALICEBLUE, 1);
        gameView.strokeScreenTextFromCenter(gameOverText, gameView.getCanvasWidth()/2, gameView.getCanvasHeight()/2, FontManager.getInstance().getBoldFont(50), Color.BLACK, 3, 1);
    }

    private String getGameOverText(String winText, String tieText, String lossText) {
        boolean playerKingTowerDestroyed = controllerForModel.isPlayerKingTowerDestroyed();
        boolean opponentKingTowerDestroyed = controllerForModel.isOpponentKingTowerDestroyed();

        // 1) edge case: both towers get destroyed at the same time
        if (playerKingTowerDestroyed && opponentKingTowerDestroyed) return tieText;
        
        // 2) opponent tower is still standing while player one is destroyed 
        if (playerKingTowerDestroyed) return lossText;
        
        // 3) player tower is still standing while opponent one is destroyed
        if (opponentKingTowerDestroyed) return winText;

        // 4) both towers are still standing
        
        byte playerTowerCount = 0;
        if (controllerForModel.isPlayerLeftTowerDestroyed()) playerTowerCount++;
        if (controllerForModel.isPlayerRightTowerDestroyed()) playerTowerCount++;

        byte opponentTowerCount = 0;
        if (controllerForModel.isOpponentLeftTowerDestroyed()) opponentTowerCount++;
        if (controllerForModel.isOpponentRightTowerDestroyed()) opponentTowerCount++;

        // 4.1) player has more destroyed towers than opponent (victory)
        if (playerTowerCount > opponentTowerCount) return lossText;

        // 4.2) opponent has more destroyed towers than player (loss)
        if (playerTowerCount < opponentTowerCount) return winText;
        
        // 4.3) player has the same amount of destroyed towers as opponent (tie)
        return tieText;
    }

    @Override
    public void fillPoint(double centreX, double centreY, int size, Color color) {
        gameView.fillPoint(centreX, centreY, size, color);
    }

    @Override
    public double logicToGraphicX(double logicCoordX) {
        return gameView.getWorldMapTopLeftCornerX() + logicCoordX * gameView.getDx();
    }

    @Override
    public double logicToGraphicY(double logicCoordY) {
        return gameView.getWorldMapTopLeftCornerY() + logicCoordY * gameView.getDy();
    }

    @Override
    public void handleMouseSelectedTile(int row, int col) {
        if (isGameOver()) return;
        
        // 1. Valid Position 
        if (controllerForModel.isPlayerEntityDroppableOnTile(row, col)) {
            if (!isPositionValid()) gameView.startDragPlacementPreview();

            lastSelectedColumnIndex = col;
            lastSelectedRowIndex = row;
            return;
            
        } 
        
        // 2. Position outside bounds
        if(row < 0 || row >= controllerForModel.getNumRowsArena()
               || col < 0 || col >= controllerForModel.getNumColsArena()) {
            resetLastSelectedTile();
            gameView.stopDragPlacementPreview();
            return;
        }

        // 3. Position inside bounds but (row, col) not droppable

        // Tries the horizontal slide (based on previous row)
        if (controllerForModel.isPlayerEntityDroppableOnTile(lastSelectedRowIndex, col)) {
            lastSelectedColumnIndex = col;
        }

        // Tries the vertical slide (based on previous col)
        if (controllerForModel.isPlayerEntityDroppableOnTile(row, lastSelectedColumnIndex)) {
            lastSelectedRowIndex = row;
        } 
        
    }

    @Override
    public void handleMouseReleased() {
        resetLastSelectedTile();
        gameView.stopDragPlacementPreview();
    }

    private void resetLastSelectedTile() {
        lastSelectedColumnIndex = -1;
        lastSelectedRowIndex = -1;
    }

    @Override
    public boolean shouldRenderDragPlacementPreview() {
        return !isGameOver() && isPositionValid();
    }

    @Override
    public void renderDragPlacementPreview(double centreX, double centreY) {
        gameView.renderDragPlacementPreview(centreX, centreY);
    }

    @Override
    public void renderPlayerDeck(EntityType card1, byte elixirCost1, EntityType card2, byte elixirCost2, EntityType card3, byte elixirCost3, EntityType card4, byte elixirCost4, byte elixirLeft, double elixirChargeTimeProgress, byte maxElixir) {
        double alpha = getAlphaBasedGameOver();
        if (alpha > 0) gameView.renderPlayerDeck(card1, elixirCost1, card2, elixirCost2, card3, elixirCost3, card4, elixirCost4, elixirLeft, elixirChargeTimeProgress, maxElixir, alpha);
    }

    private double getAlphaBasedGameOver() {
        // returns the alpha value based on time passed since game over
        if (initialTimeGameOver == -1) return 1;

        if (timePassedSinceGameOver >= FADE_OUT_DURATION_NANOSEC) return 0;

        return smoothnessFunction((double) timePassedSinceGameOver / FADE_OUT_DURATION_NANOSEC);
    }

    private double smoothnessFunction(double x) {
        // this function was built for x in [0, 1]

        // edge cases
        if (x <= 0) return 1;  
        if (x >= 1) return 0;

        // goes smoothly from 1 to 0 (linearly if SMOOTHNESS_CURVE = 1)
        return 1.0 / (1.0 + Math.pow((1-x)/x, - Math.abs(SMOOTHNESS_CURVE))); 
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
        if (isGameOver()) return;

        controllerForModel.setSelectedPlayerCard(cardIndex);
    }

    @Override
    public void dropSelectedPlayerCardOnLastMousePos() {
        if (isPositionValid())
            controllerForModel.dropSelectedPlayerCard(lastSelectedRowIndex, lastSelectedColumnIndex);
        
    }

    @Override
    public int getAvailableDeckCards() {
        return controllerForModel.getAvailableDeckCards();
    }

    // static methods
    public static IControllerForView getInstance() {
        if (instance == null) {
            instance = new ControllerForView();
        }

        return instance;
    }
    
}
