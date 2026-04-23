package jroyale.view.game_view;

import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import jroyale.controller.ControllerForView;
import jroyale.model.troops.Valkyrie;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.MainGUI;
import jroyale.view.MouseManager;
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
import jroyale.view.game_view.entity_view.troops.SkeletonView;
import jroyale.view.game_view.entity_view.troops.TroopView;
import jroyale.view.game_view.entity_view.troops.ValkyrieView;
import jroyale.view.game_view.ui.DragPlacementPreview;
import jroyale.view.game_view.ui.TimeLeftRenderer;

public class GameView extends MainGUI implements IGameView {
    
    private static IGameView instance = null;

    private GameView() {}

    // instance methods

    @Override
    public void load() {
        ArenaView.getInstance().init(
            ControllerForView.getInstance().getNumRowsArena(),
            ControllerForView.getInstance().getNumColsArena()
        );


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

    @Override
    public void buildUI() {
        MouseManager.getInstance().init(stage.getScene());
        DeckView.getInstance().init(ControllerForView.getInstance().getAvailableDeckCards());
    }

    @Override
    public void update(long now) {
        //clearWindow();
        ArenaView.getInstance().update();
        DragPlacementPreview.getInstance().update(now);
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
        return fromWorldToScreenX(ArenaView.getInstance().getMapBounds().getMinX());
    }

    @Override
    public double getScreenMapTopLeftCornerY() {
        return fromWorldToScreenY(ArenaView.getInstance().getMapBounds().getMinY());
    }

    @Override
    public double getScreenMapWidth() {
        return ArenaView.getInstance().getMapBounds().getWidth() * globalScale;
    }

    @Override
    public double getScreenMapHeight() {
        return ArenaView.getInstance().getMapBounds().getHeight() * globalScale;
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
    public void renderWorldShadow(double centerX, double centerY, double shadowRadius) {
        renderScreenShadow(
            fromWorldToScreenX(centerX), 
            fromWorldToScreenY(centerY), 
            shadowRadius * globalScale
        );
    }

    private void renderScreenShadow(double centreX, double centreY, double shadowRadius) {
        // Definiamo il gradiente radiale
        gc.save();
        RadialGradient gradient = new RadialGradient(
            0,      // focusAngle
            0,      // focusDistance
            centreX, // centerX (coordinata assoluta sul canvas)
            centreY, // centerY (coordinata assoluta sul canvas)
            shadowRadius, // radius (metà del diametro)
            false,  // proportional: false perché usiamo i pixel esatti
            null,   // cycleMethod
            new Stop(0, new Color(0, 0, 0, 0.7)),              // Centro opaco
            new Stop(1, Color.TRANSPARENT)       // Bordo trasparente
        );

        gc.setFill(gradient);
        gc.fillOval(centreX - shadowRadius, centreY - shadowRadius, shadowRadius*2, shadowRadius*2); // Disegna il cerchio
        gc.restore();
    }

    @Override
    public void renderTimeLeft(int secondsLeft, double alpha) {
        TimeLeftRenderer.getInstance().renderTimeLeft(secondsLeft, alpha);
    }

    private int getColFromMouseX(double mouseX) {
        return (int) Math.floor(
            (mouseX - getScreenMapTopLeftCornerX()) / (getDx() * globalScale)
        ); 
    }

    private int getRowFromMouseY(double mouseY) {
        return (int) Math.floor(
            (mouseY - getScreenMapTopLeftCornerY()) / (getDy() * globalScale)
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
        ControllerForView.getInstance().setSelectedPlayerCard(cardIndex);
    }

    @Override
    public void processOnMousePressed(double x, double y) {
        ControllerForView.getInstance().handleMouseSelectedTile(
            getRowFromMouseY(y),
            getColFromMouseX(x)
        );
    }

    @Override
    public void processOnMouseDragged(double x, double y) {
        ControllerForView.getInstance().handleMouseSelectedTile(
            getRowFromMouseY(y),
            getColFromMouseX(x)
        );
    }

    @Override
    public void processOnMouseReleased() {
        ControllerForView.getInstance().dropSelectedPlayerCardOnLastMousePos();

        ControllerForView.getInstance().handleMouseReleased();
    }

    // static methods

    public static IGameView getInstance() {
        if (instance == null) {
            instance = new GameView();
        }

        return instance;
    }

}
