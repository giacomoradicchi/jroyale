package jroyale.controller;

import javafx.stage.Stage;
import jroyale.shared.Enums.Side;
import jroyale.shared.Enums.State;
import jroyale.view.IView2;
import jroyale.view.View.TroopType;
import jroyale.view.View2;
import jroyale.view.troops.TroopView;

public class ControllerForView implements IControllerForView {

    private static ControllerForView instance;

    private IView2 view;

    private ControllerForView() {
        // empty
    }

    
    @Override
    public void openWindow(Stage stage) {
        view = View2.getInstance();
        view.openWindow(stage);
    }

    @Override
    public void initView() {
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
    public void renderArena() {
        view.renderArena();
    }

    @Override
    public void renderTroop(double centreX, double centreY, double angleDirection, int currentFrame, State state,
            Side side, TroopType type) {
        view.renderTroop(centreX, centreY, angleDirection, currentFrame, state, side, type);
    }

    @Override
    public double logicToGraphicX(double logicCoordX) {
        return view.getMapTopLeftCornerX() + logicCoordX * view.getDx();
    }

    @Override
    public double logicToGraphicY(double logicCoordY) {
        return view.getMapTopLeftCornerY() + logicCoordY * view.getDy();
    }

    @Override
    public int getNumFramesPerDirection(TroopView troopView, State state) {
        return view.getNumFramesPerDirection(troopView, state);
    }

    // static methods
    public static IControllerForView getInstance() {
        if (instance == null) {
            instance = new ControllerForView();
        }

        return instance;
    }
    
}
