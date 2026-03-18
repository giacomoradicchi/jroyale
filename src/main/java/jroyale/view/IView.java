package jroyale.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public interface IView {
    
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

    public void strokeScreenLine(double x1, double y1, double x2, double y2, double alpha, Color color, double lineWidth);

    public void strokeWorldLine(double x1, double y1, double x2, double y2, double alpha, Color color, double lineWidth);

    public void renderPlayerDeck(EntityType card1, byte elixirCost1, EntityType card2, byte elixirCost2, EntityType card3, byte elixirCost3, EntityType card4, byte elixirCost4, byte elixirLeft, double elixirChargeTimeProgress, byte maxElixir);

    public void fillScreenTextFromCenter(String text, double centerX, double centerY, Font font, Color color);

    public void strokeScreenTextFromCenter(String text, double centerX, double centerY, Font font, Color color, double lineWidth);

    public void setSelectedCard(int cardIndex);

    public void processOnMousePressed(double x, double y);

    public void processOnMouseDragged(double x, double y);
    
    public void processOnMouseReleased();

    // for debug
    public void fillPoint(double centreX, double centreY, int size, Color color);

}
