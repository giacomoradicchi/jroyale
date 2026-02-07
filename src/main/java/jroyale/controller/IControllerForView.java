package jroyale.controller;

import javafx.stage.Stage;
import jroyale.shared.Enums.Side;
import jroyale.shared.Enums.State;
import jroyale.view.View.TroopType;
import jroyale.view.troops.TroopView;

public interface IControllerForView {
    
    public void openWindow(Stage stage);

    public void initView();

    public void updateView(long now);

    public int getNumRowsArena();

    public int getNumColsArena();

    public void renderArena();

    public void renderTroop(double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side, TroopType type);

    public double logicToGraphicX(double logicX);

    public double logicToGraphicY(double logicY);

    public int getNumFramesPerDirection(TroopView troopView, State state);
}
