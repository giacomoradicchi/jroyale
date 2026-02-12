package jroyale.controller;

import javafx.stage.Stage;
import jroyale.utils.GameData;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.IView2;
import jroyale.view.View2;
import jroyale.view.entity_view.troops.GiantView;
import jroyale.view.entity_view.troops.MiniPekkaView;
import jroyale.view.entity_view.troops.PekkaView;
import jroyale.view.entity_view.troops.SkeletonView;

public class ControllerForView implements IControllerForView {

    private static ControllerForView instance;

    private IView2 view;

    private ControllerForView() {
        // empty
    }

    // private methods
    private void initTroopsFramesPerDirection() {
        GiantView.setNumFramesPerDirection(GameData.getInstance().getAnimationSteps(EntityType.GIANT));
        MiniPekkaView.setNumFramesPerDirection(GameData.getInstance().getAnimationSteps(EntityType.MINIPEKKA));
        SkeletonView.setNumFramesPerDirection(GameData.getInstance().getAnimationSteps(EntityType.SKELETON));
        PekkaView.setNumFramesPerDirection(GameData.getInstance().getAnimationSteps(EntityType.PEKKA));
    }

    // instance methods
    
    @Override
    public void openWindow(Stage stage) {
        view = View2.getInstance();
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
    public void renderEntity(double centreX, double centreY, double shadowRadius, double angleDirection, int currentFrame, State state,
            Side side, EntityType type) {
        view.renderEntity(centreX, centreY, shadowRadius, angleDirection, currentFrame, state, side, type);
    }

    @Override
    public double logicToGraphicX(double logicCoordX) {
        return view.getMapTopLeftCornerX() + logicCoordX * view.getDx();
    }

    @Override
    public double logicToGraphicY(double logicCoordY) {
        return view.getMapTopLeftCornerY() + logicCoordY * view.getDy();
    }

    // static methods
    public static IControllerForView getInstance() {
        if (instance == null) {
            instance = new ControllerForView();
        }

        return instance;
    }
    
}
