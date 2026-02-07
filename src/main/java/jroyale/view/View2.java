package jroyale.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jroyale.controller.ControllerForView;
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
    public void renderArena() {
        arena.renderArena(gc, false);
    }

    @Override
    public void renderTroop(double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side, TroopType type) {

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

        type.getViewInstance().render(gc, centreX, centreY, angleDirection, currentFrame, state, side, globalScale);
        
    }

    @Override
    public int getNumFramesPerDirection(TroopView troopView, State state) {
        return troopView.getNumFramesPerDirection(state);
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

    // static methods
    public static IView2 getInstance() {
        if (instance == null) {
            instance = new View2();
        }

        return instance;
    }
    
}
