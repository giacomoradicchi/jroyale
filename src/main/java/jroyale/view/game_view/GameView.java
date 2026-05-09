package jroyale.view.game_view;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.FontManager;
import jroyale.view.IMainGUI;
import jroyale.view.View;
import jroyale.view.audio.AudioManager.AudioType;
import jroyale.view.game_view.arena.ArenaView;
import jroyale.view.game_view.deck.DeckView;
import jroyale.view.game_view.entity_view.EntityView;
import jroyale.view.game_view.entity_view.towers.ArcherTowerView;
import jroyale.view.game_view.entity_view.towers.KingTowerView;
import jroyale.view.game_view.entity_view.troops.GiantView;
import jroyale.view.game_view.entity_view.troops.MiniPekkaView;
import jroyale.view.game_view.entity_view.troops.PekkaView;
import jroyale.view.game_view.entity_view.troops.SingleSkeletonView;
import jroyale.view.game_view.entity_view.troops.SkeletonArmyView;
import jroyale.view.game_view.entity_view.troops.ValkyrieView;
import jroyale.view.game_view.ui_and_ux.DragPlacementPreview;
import jroyale.view.game_view.ui_and_ux.GameAudio;
import jroyale.view.game_view.ui_and_ux.TimeLeftRenderer;

public class GameView extends View implements IGameView {
    
    private static IGameView instance = null;

    private static final double GAME_OVER_FONT_SIZE  = 50;
    private static final double GAME_OVER_STROKE_WIDTH  = 3;
    private static final double GAME_OVER_ALPHA  = 1;
    private static final Color  GAME_OVER_TEXT_COLOR = Color.ALICEBLUE;
    private static final Color  GAME_OVER_STROKE_COLOR = Color.BLACK;
    private static final String WIN_TEXT = "Vittoria!";
    private static final String LOSS_TEXT = "Sconfitta...";
    private static final String DRAW_TEXT = "Pareggio!";

    private final IMainGUI gui;

    private int secondsLeft;
    private boolean scrollResultPlayed;

    private GameView() {
        this.gui = IMainGUI.getInstance();
    }

    // instance methods

    @Override
    public IMainGUI getGUI() {
        return gui;
    }

    @Override
    public void loadSprites() {
        // loading sprites
        ArcherTowerView.getInstance();
        KingTowerView.getInstance();
        GiantView.getInstance();
        MiniPekkaView.getInstance();
        PekkaView.getInstance();
        SingleSkeletonView.getInstance();
        SkeletonArmyView.getInstance();
        ValkyrieView.getInstance();
    }

    private void buildUI() {
        ArenaView.getInstance().init(
            controllerForView.getNumRowsArena(),
            controllerForView.getNumColsArena()
        );
        
        DeckView.getInstance().init(controllerForView.getAvailableDeckCards());
        audioManager.loop(AudioType.GAME_MUSIC_2MIN);
    }

    @Override
    protected void onLoadFinished() {
        buildUI();
        controllerForView.startGameLoop();
    }

    @Override
    public void update(long now) {
        gui.clearWindow();

        ArenaView.getInstance().update();
        DragPlacementPreview.getInstance().update(now);
        GameAudio.getInstance().handleGameAudio(secondsLeft);
    }

    @Override
    public void updateTimeLeft(int secondsLeft) {
        this.secondsLeft = secondsLeft;
    }

    @Override
    public void stopCurrentAudio() {
        audioManager.stopCurrentAudio();
    }

    @Override
    public double getDx() {
        return ArenaView.getInstance().getDx();
    }

    @Override
    public double getDy() {
        return ArenaView.getInstance().getDy();
    }

    @Override
    public double getScreenMapTopLeftCornerX() {
        return gui.fromWorldToScreenX(ArenaView.getInstance().getMapBounds().getMinX());
    }

    @Override
    public double getScreenMapTopLeftCornerY() {
        return gui.fromWorldToScreenY(ArenaView.getInstance().getMapBounds().getMinY());
    }

    @Override
    public double getScreenMapWidth() {
        return ArenaView.getInstance().getMapBounds().getWidth() * gui.getGlobalScale();
    }

    @Override
    public double getScreenMapHeight() {
        return ArenaView.getInstance().getMapBounds().getHeight() * gui.getGlobalScale();
    }

    @Override
    public double getWorldMapTopLeftCornerX() {
        return ArenaView.getInstance().getMapBounds().getMinX();
    }

    @Override
    public double getWorldMapTopLeftCornerY() {
        return ArenaView.getInstance().getMapBounds().getMinY();
    }

    @Override
    public void renderArena() {
        ArenaView.getInstance().renderArena(false);
    }

    @Override
    public void renderEntity(EntityView entity, double centreX, double centreY, int currentHealth, int maxHealth, double shadowRadius, double angleDirection, int currentFrame, State state, Side side) {
        entity.render(centreX, centreY, currentHealth, maxHealth, shadowRadius, angleDirection, currentFrame, state, side);
    }

    @Override
    public void renderTimeLeft(double alpha) {
        TimeLeftRenderer.getInstance().renderTimeLeft(secondsLeft, alpha);
    }

    private int getColFromMouseX(double mouseX) {
        return (int) Math.floor(
            (mouseX - getScreenMapTopLeftCornerX()) / (getDx() * gui.getGlobalScale())
        ); 
    }

    private int getRowFromMouseY(double mouseY) {
        return (int) Math.floor(
            (mouseY - getScreenMapTopLeftCornerY()) / (getDy() * gui.getGlobalScale())
        ); 
    }

    @Override
    public void startDragPlacementPreview() {
        DeckView.getInstance().setVisibleSelectedCard(false);
        DragPlacementPreview.getInstance().startAnimation();
    }

    @Override
    public void renderDragPlacementPreview(double centreX, double centreY) {
        DragPlacementPreview.getInstance().render(centreX, centreY);
    }

    @Override
    public void stopDragPlacementPreview() {
        DeckView.getInstance().setVisibleSelectedCard(true);
        DragPlacementPreview.getInstance().stopAnimation();
    }

    @Override
    public void renderPlayerDeck(EntityType card1, byte elixirCost1, EntityType card2, byte elixirCost2, EntityType card3, byte elixirCost3, EntityType card4, byte elixirCost4, byte elixirLeft, double elixirChargeTimeProgress, byte maxElixir, double alpha) {
        DeckView.getInstance().renderPlayerDeck(card1, elixirCost1, card2, elixirCost2, card3, elixirCost3, card4, elixirCost4, elixirLeft, elixirChargeTimeProgress, maxElixir, alpha);
    }

    @Override
    public void setSelectedCard(int cardIndex) {
        controllerForView.setSelectedPlayerCard(cardIndex);
    }

    @Override
    public void handleGameOver(int result) {
        playScrollAudio(result);
        renderGameOver(result);
    }

    private void playScrollAudio(int result) {
        if (!scrollResultPlayed) {
            setScrollAudio(result);
            scrollResultPlayed = true;
        }
    }

    private void setScrollAudio(int result) {
        AudioType scrollAudio;

        if (result > 0)         scrollAudio = AudioType.SCROLL_WIN;
        else if (result < 0)    scrollAudio = AudioType.SCROLL_LOSE;
        else                    scrollAudio = AudioType.SCROLL_DRAW;

        audioManager.stopCurrentAudio();
        audioManager.play(scrollAudio);
    }

    private void renderGameOver(int result) {
        String gameOverText = getGameOverText(result);
        Font gameOverFont = FontManager.getInstance().getBoldFont(GAME_OVER_FONT_SIZE);

        gui.fillScreenTextFromCenter(gameOverText, gui.getCanvasWidth()/2, gui.getCanvasHeight()/2, gameOverFont, GAME_OVER_TEXT_COLOR, GAME_OVER_ALPHA);
        gui.strokeScreenTextFromCenter(gameOverText, gui.getCanvasWidth()/2, gui.getCanvasHeight()/2, gameOverFont, GAME_OVER_STROKE_COLOR, GAME_OVER_STROKE_WIDTH, GAME_OVER_ALPHA);
    
    }

    private String getGameOverText(int result) {
        if (result > 0) return WIN_TEXT;
        if (result < 0) return LOSS_TEXT;

        return DRAW_TEXT;
    }

    @Override
    public void processOnMousePressed(double x, double y) {
        controllerForView.handleMouseSelectedTile(
            getRowFromMouseY(y),
            getColFromMouseX(x)
        );
    }

    @Override
    public void processOnMouseDragged(double x, double y) {
        controllerForView.handleMouseSelectedTile(
            getRowFromMouseY(y),
            getColFromMouseX(x)
        );
    }

    @Override
    public void processOnMouseReleased() {
        controllerForView.dropSelectedPlayerCardOnLastMousePos();

        controllerForView.handleMouseReleased();
    }

    @Override
    public void playNotDroppedSound() {
        audioManager.play(AudioType.GAME_SPELL_NOT_READY);
    }

    @Override
    public void handleAudioEffect(int currentFrame, State state, EntityView entityView) {
        entityView.handleAudioEffect(currentFrame, state);
    }

    // static methods

    public static IGameView getInstance() {
        if (instance == null) {
            instance = new GameView();
        }

        return instance;
    }

}
