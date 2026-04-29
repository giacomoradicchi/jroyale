package jroyale.view.game_view.deck;

import javafx.util.Duration;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jroyale.utils.Enums.EntityType;
import jroyale.view.IMainGUI;
import jroyale.view.game_view.GameView;
import jroyale.utils.Point;

public class CardView extends StackPane {
    
    private final Point startingPos;
    private final Point dragOffset = new Point(0, 0);
    private EntityType type;
    private byte elixirCost;

    // animation and feedback constants
    private final static int ANIMATION_TIME_MILLIS = 150;
    private final static double SCALE_ON_CLICK = 1.05;
    private final static double SCALE_DEFAULT = 1.0;
    private final static double NORMALIZED_CIRCULAR_PROGRESS_SIZE = 0.25;
    
    // coordinate and state constants
    private final static double CENTER_DIVISOR = 2.0;
    private final static double RESET_TRANSLATE = 0.0;
    private final static double MIN_PROGRESS = 0.0;
    private final static double MAX_PROGRESS = 1.0;

    public CardView(double x, double y, double width, double height) {
        this.startingPos = new Point(x, y);

        // initial positioning (centered on the provided coordinates)
        this.setLayoutX(x - width / CENTER_DIVISOR);
        this.setLayoutY(y - height / CENTER_DIVISOR);

        // interactive hitbox: transparent rectangle to capture mouse events
        Rectangle hitbox = new Rectangle(width, height, Color.TRANSPARENT);
        this.getChildren().add(hitbox);
        
        // ensure the pane itself is clickable even where transparent
        this.setPickOnBounds(true);

        setupEvents();
    }

    private void setupEvents() {
        this.setOnMousePressed(e -> {
            DeckView.getInstance().setSelectedCard(this);

            // store the click location relative to the card's top-left corner
            dragOffset.setPoint(e.getX(), e.getY());
            
            this.toFront();
            
            // visual feedback
            this.setScaleX(SCALE_ON_CLICK);
            this.setScaleY(SCALE_ON_CLICK);

            GameView.getInstance().processOnMousePressed(e.getSceneX(), e.getSceneY());
            e.consume();
        });

        this.setOnMouseDragged(e -> {
            this.setLayoutX(e.getSceneX() - dragOffset.getX());
            this.setLayoutY(e.getSceneY() - dragOffset.getY());
            
            GameView.getInstance().processOnMouseDragged(e.getSceneX(), e.getSceneY());
            e.consume();
        });

        this.setOnMouseReleased(e -> {
            GameView.getInstance().processOnMouseReleased();

            // reset scale
            this.setScaleX(SCALE_DEFAULT);
            this.setScaleY(SCALE_DEFAULT);

            // calculate the target position (original starting position)
            double targetX = startingPos.getX() - getScaledWidth() / CENTER_DIVISOR;
            double targetY = startingPos.getY() - getScaledHeight() / CENTER_DIVISOR;
            
            double deltaX = targetX - this.getLayoutX();
            double deltaY = targetY - this.getLayoutY();

            // smooth return transition
            TranslateTransition tt = new TranslateTransition(Duration.millis(ANIMATION_TIME_MILLIS), this);
            tt.setFromX(RESET_TRANSLATE);
            tt.setFromY(RESET_TRANSLATE);
            tt.setToX(deltaX);
            tt.setToY(deltaY);
            tt.setInterpolator(Interpolator.EASE_OUT);
            
            tt.setOnFinished(ev -> {
                this.setTranslateX(RESET_TRANSLATE);
                this.setTranslateY(RESET_TRANSLATE);
                this.setLayoutX(targetX);
                this.setLayoutY(targetY);
            });
            
            tt.play();
        });
    }

    public double getTopLeftX() {
        return this.getLayoutX() + this.getTranslateX();
    }

    public double getTopLeftY() {
        return this.getLayoutY() + this.getTranslateY();
    }

    public void render(Image icon, Image outline, double progress, double alpha) {
        if (!isVisible()) return;

        // calculate the current visual center
        double centerX = getTopLeftX() + getWidth() / CENTER_DIVISOR;
        double centerY = getTopLeftY() + getHeight() / CENTER_DIVISOR;

        IMainGUI gui = GameView.getInstance().getGUI();
        
        // ensure progress is within bounds
        double currentProgress = Math.max(MIN_PROGRESS, progress);

        // 1. draw icon
        boolean isLocked = currentProgress < MAX_PROGRESS;
        gui.renderScreenImage(icon, centerX, centerY, getScaledWidth(), getScaledHeight(), isLocked, alpha);

        // 2. draw progress overlay if not ready
        if (isLocked) {
            gui.fillScreenCircularProgress(
                centerX, 
                centerY, 
                getWidth() * NORMALIZED_CIRCULAR_PROGRESS_SIZE,
                currentProgress
            );
        }
        
        // 3. draw frame
        gui.renderScreenImage(outline, centerX, centerY, getScaledWidth(), getScaledHeight(), false, alpha);
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

    public CardView setType(EntityType type) {
        if (type != this.type) {
            this.type = type;
        }
        return this;
    }

    public byte getElixirCost() {
        return elixirCost;
    }

    public CardView setElixirCost(byte elixirCost) {
        if (elixirCost != this.elixirCost) {
            this.elixirCost = elixirCost;
        }
        return this;
    }
}