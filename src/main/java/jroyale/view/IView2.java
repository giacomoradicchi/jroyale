package jroyale.view;

import javafx.stage.Stage;
import jroyale.shared.Enums.Side;
import jroyale.shared.Enums.State;
import jroyale.view.View.TroopType;
import jroyale.view.troops.TroopView;

public interface IView2 {
    
    public void openWindow(Stage stage);

    public void init();

    public void update(long now);

    public double getDx();

    public double getDy();

    public double getMapTopLeftCornerX();

    public double getMapTopLeftCornerY();

    public void renderArena();

    public void renderTroop(double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side, TroopType type);

    public int getNumFramesPerDirection(TroopView troopView, State state);
}
