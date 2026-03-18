package jroyale.view;

import javafx.util.Duration;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Point;


public class CardView extends StackPane {
    
    private final Point startingPos;
    private final Point dragOffset = new Point(0, 0);
    private EntityType type;

    private final static int ANIMATION_TIME_MILLIS = 150;
    private final static double SCALE_ON_CLICK = 1.05;

    public CardView(double x, double y, double width, double height) {
        this.startingPos = new Point(x, y);

        // Initial positioning (centered on the provided coordinates)
        this.setLayoutX(x - width / 2);
        this.setLayoutY(y - height / 2);

        // Interactive Hitbox: transparent rectangle to capture mouse events
        Rectangle hitbox = new Rectangle(width, height, Color.TRANSPARENT);
        this.getChildren().add(hitbox);
        
        // Ensure the pane itself is clickable even where transparent
        this.setPickOnBounds(true);

        setupEvents();
    }

    private void setupEvents() {
        this.setOnMousePressed(e -> {

            DeckView.getInstance().setSelectedCard(this);

            // Store the click location RELATIVE to the card's top-left corner
            // to prevent the card from "snapping" its corner to the mouse cursor.
            dragOffset.setPoint(e.getX(), e.getY());
            
            // Bring this node to the front of the parent container
            this.toFront();
            
            // Subtle visual feedback: scale up when picked up
            this.setScaleX(SCALE_ON_CLICK);
            this.setScaleY(SCALE_ON_CLICK);

            // send data to view:
            View.getInstance().processOnMousePressed(
                e.getSceneX(), 
                e.getSceneY()
            );
            
            e.consume();

        });

        this.setOnMouseDragged(e -> {
            // Update node position based on scene coordinates minus the initial offset
            this.setLayoutX(e.getSceneX() - dragOffset.getX());
            this.setLayoutY(e.getSceneY() - dragOffset.getY());
            e.consume();

            // send data to view:
            View.getInstance().processOnMouseDragged(
                e.getSceneX(), 
                e.getSceneY()
            );
        });

        this.setOnMouseReleased(e -> {

            // notify view:
            View.getInstance().processOnMouseReleased();

            // Reset scale
            this.setScaleX(1.0);
            this.setScaleY(1.0);

            // Calculate the distance between current layout and original starting position
            double targetX = startingPos.getX() - getScaledWidth() / 2;
            double targetY = startingPos.getY() - getScaledHeight() / 2;
            
            double deltaX = targetX - this.getLayoutX();
            double deltaY = targetY - this.getLayoutY();

            // Create a smooth return transition
            TranslateTransition tt = new TranslateTransition(Duration.millis(ANIMATION_TIME_MILLIS), this);
            tt.setFromX(0);
            tt.setFromY(0);
            tt.setToX(deltaX);
            tt.setToY(deltaY);
            
            // EASE_OUT makes the movement start fast and slow down at the end
            tt.setInterpolator(Interpolator.EASE_OUT);
            
            tt.setOnFinished(ev -> {
                // Once the animation finishes, reset translate properties
                // and commit the final position to the layout properties.
                this.setTranslateX(0);
                this.setTranslateY(0);
                this.setLayoutX(targetX);
                this.setLayoutY(targetY);
            });
            
            tt.play();
        });
    }

    public void render(Image icon, Image outline) {
        if (!isVisible()) return;

        // Calculate the current visual center by summing Layout + Translate
        double centerX = this.getLayoutX() + this.getTranslateX() + getWidth() / 2;
        double centerY = this.getLayoutY() + this.getTranslateY() + getWidth() / 2;

        IView v = View.getInstance();
        

        // 1. Draw Icon
        v.renderScreenImage(icon, centerX, centerY, getScaledWidth(), getScaledHeight());
        
        // 2. Draw Outline/Frame
        v.renderScreenImage(outline, centerX, centerY, getScaledWidth(), getScaledHeight());

        // Debug: Red rect should perfectly align with the card's interactive area
        //v.fillScreenRoundedRect(centerX, centerY, getScaledWidth(), getScaledHeight(), 0, 0, 0.5, Color.RED);
    }

    private double getScaledWidth() {
        return getWidth() * getScaleX();
    }

    private double getScaledHeight() {
        return getHeight() * getScaleY();
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        if (type != this.type) this.type = type;
    }

    

}