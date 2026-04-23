package jroyale.view;

import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public interface IMainGUI {

    public void openWindow(Stage stage);

    public Stage getStage();

    public void closeWindow();

    public void init();

    public Task<Void> loadAsync();

    public void update(long now);

    public void addToRoot(Node node);

    public int getCanvasWidth();

    public int getCanvasHeight();

    public double getGlobalScale();

    public void setGlobalScale(double globalScale);

    public void renderWorldImage(Image image, double centerX, double centerY, double width, double height, double alpha);

    public void renderScreenImage(Image image, double centerX, double centerY, double width, double height, double alpha);
        
    public void fillWorldRoundedRect(double centerX, double centerY, double width, double height, double arcWidth, double arcHeight, double alpha, Color color);
        
    public void strokeWorldRoundedRect(double centerX, double centerY, double width, double height, double arcWidth, double arcHeight, double lineWidth, double alpha, Color color);
        
    public void fillScreenRoundedRect(double centerX, double centerY, double width, double height, double arcWidth, double arcHeight, double alpha, Color color);

    public void strokeScreenRoundedRect(double centerX, double centerY, double width, double height, double arcWidth, double arcHeight, double lineWidth, double alpha, Color color);

    public void strokeWorldLine(double x1, double y1, double x2, double y2, double alpha, Color color, double lineWidth);

    public void strokeScreenLine(double x1, double y1, double x2, double y2, double alpha, Color color, double lineWidth);
        
    public void fillScreenTextFromCenter(String text, double centerX, double centerY, Font font, Color color, double alpha);

    public void strokeScreenTextFromCenter(String text, double centerX, double centerY, Font font, Color color, double lineWidth, double alpha);

    public void fillPoint(double centreX, double centreY, int size, Color color);
    
    public void processOnMousePressed(double x, double y);

    public void processOnMouseDragged(double x, double y);
    
    public void processOnMouseReleased();
}
