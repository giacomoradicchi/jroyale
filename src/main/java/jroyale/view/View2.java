package jroyale.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jroyale.controller.ControllerForView;
import jroyale.shared.Enums.EntityType;
import jroyale.shared.Enums.Side;
import jroyale.shared.Enums.State;
import jroyale.view.View.TroopType;
import jroyale.view.troops.TroopView;

public class View2 implements IView2 {

    private static IView2 instance = null;
    private Stage stage;


    private static final double WH_RATIO = 607.0 / 1080;

    private static final double HEIGHT = 800;
    private static final double WIDTH = HEIGHT * WH_RATIO;

    private GraphicsContext gc;
    private double dx, dy;

    // scale of the entire scene
    private Arena arena;
    private double globalScale = 1.0;

    // The timestamp of the current frame given in nanoseconds
    private long now;

    private View2() {}

    // instance methods
    @Override
    public void init() {
        arena = new Arena(
            WIDTH, 
            HEIGHT, 
            globalScale, 
            ControllerForView.getInstance().getNumRowsArena(),
            ControllerForView.getInstance().getNumColsArena()
        );

        loadSprites();
        EntityViewBinder.getInstance().init();
    }

    @Override
    public void openWindow(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Pane root = new Pane(canvas);
        gc = canvas.getGraphicsContext2D();

        
        this.stage = stage;
        
        stage.setScene(new Scene(root));
        stage.setTitle("JRoyale");
        stage.show();

    }
    
    @Override
    public void update(long now) {
        // clears canvas
        gc.clearRect(0, 0, WIDTH, HEIGHT);  

        this.now = now;

        //globalScale -= 0.001;

        arena.update(globalScale);
        updateDxDy();
    }

    @Override
    public double getDx() {
        return arena.getDx();
    }

    @Override
    public double getDy() {
        return arena.getDy();
    }

    @Override
    public double getMapTopLeftCornerX() {
        return arena.getMapBounds().getMinX();
    }

    @Override
    public double getMapTopLeftCornerY() {
        return arena.getMapBounds().getMinY();
    }

    @Override
    public void renderWorldImage(Image image, double centerX, double centerY, double width, double height) {
        gc.drawImage(
            image, 
            centerX - width / 2 * globalScale, 
            centerY - height / 2 * globalScale,
            width * globalScale, 
            height * globalScale
        );
    }

    @Override
    public void renderScreenImage(Image image, double centerX, double centerY, double width, double height) {
        gc.drawImage(
            image, 
            centerX - width / 2, 
            centerY - height / 2,
            width, 
            height
        );
    }

    @Override
    public void renderArena() {
        arena.renderArena(gc, false);
    }

    @Override
    public void renderEntity(double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side, EntityType type) {

        /* renderVector(centreX, centreY, angleDirection);

        Color color = Color.BLUE;
        if (side == Side.OPPONENT) {
            color = Color.RED;
        }

        fillPoint(
            centreX, 
            centreY,
            color
        );  */

        Color color = Color.BLUE;
        if (side == Side.OPPONENT) {
            color = Color.RED;
        }

        fillPoint(
            centreX, 
            centreY,
            10,
            color
        );

        EntityViewBinder.getInstance().getViewInstance(type).render(centreX, centreY, angleDirection, currentFrame, state, side);
        
    }

    // private methods
    private void updateDxDy() {
        dx = arena.getDx();
        dy = arena.getDy();
    }

    private void loadSprites() {
        // forcing loading of all troop sprites
        TroopType.values();
    }

    private void fillPoint(double centreX, double centreY, int size, Color color) {
        gc.save();

        gc.setFill(color);
        gc.setGlobalAlpha(1);

        gc.fillOval(
            centreX - size/2, 
            centreY - size/2,
            10, 
            10
        );

        // restoring previous settings
        gc.restore();
    }


    // static methods
    public static IView2 getInstance() {
        if (instance == null) {
            instance = new View2();
        }

        return instance;
    }
    
}
