package jroyale.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
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

    public void addToRoot(Node node);

    public double getDx();

    public double getDy();

    public double getMapTopLeftCornerX();

    public double getMapTopLeftCornerY();

    public void renderWorldImage(Image image, double centerX, double centerY, double width, double height);

    public void renderScreenImage(Image image, double centerX, double centerY, double width, double height);

    public void renderArena();

    public void renderEntity(double centreX, double centreY, int currentHealth, int maxHealth, double shadowRadius, double angleDirection, int currentFrame, State state, Side side, EntityType type);

    public void renderWorldShadow(double centreX, double centreY, double shadowRadius);

    public void startDragPlacementPreview();

    public void renderDragPlacementPreview(double centreX, double centreY);

    public void stopDragPlacementPreview();

    public void fillWorldRoundedRect(double centerX, double centerY, double width, double height, double arcWidth, double arcHeight, double alpha, Color color);

    public void strokeWorldRoundedRect(double centerX, double centerY, double width, double height, double arcWidth, double arcHeight, double lineWidth, double alpha, Color color);

    public void fillScreenRoundedRect(double centerX, double centerY, double width, double height, double arcWidth, double arcHeight, double alpha, Color color);

    public void strokeScreenRoundedRect(double centerX, double centerY, double width, double height, double arcWidth, double arcHeight, double lineWidth, double alpha, Color color);

    public void renderPlayerDeck(EntityType card1, EntityType card2, EntityType card3, EntityType card4);

    public void setSelectedCard(int cardIndex);

    // for debug
    public void fillPoint(double centreX, double centreY, int size, Color color);

}
