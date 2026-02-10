package jroyale.controller;

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

    public void renderArena();

    public void renderEntity(double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side, EntityType type);

    public double logicToGraphicX(double logicX);

    public double logicToGraphicY(double logicY);

}
