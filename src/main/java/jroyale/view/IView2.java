package jroyale.view;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public interface IView2 {
    
    public void openWindow(Stage stage);

    public double getCanvasWidth();

    public double getCanvasHeight();

    public void init();

    public void update(long now);

    public double getDx();

    public double getDy();

    public double getMapTopLeftCornerX();

    public double getMapTopLeftCornerY();

    public void renderWorldImage(Image image, double centerX, double centerY, double width, double height);

    public void renderScreenImage(Image image, double centerX, double centerY, double width, double height);

    public void renderArena();

    public void renderEntity(double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side, EntityType type);

}
