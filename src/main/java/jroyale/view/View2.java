package jroyale.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jroyale.controller.ControllerForView;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.View.TroopType;

public class View2 implements IView2 {

    private static IView2 instance = null;

    private static final double WH_RATIO = 607.0 / 1080;

    private static final double CANVAS_HEIGHT = 800;
    private static final double CANVAS_WIDTH = CANVAS_HEIGHT * WH_RATIO;

    private GraphicsContext gc;

    // scale of the entire scene
    private double globalScale = 1.0;

    private View2() {}

    // instance methods
    @Override
    public void init() {
        
        ArenaView.getInstance().init(
            CANVAS_WIDTH,
            CANVAS_HEIGHT,
            ControllerForView.getInstance().getNumRowsArena(),
            ControllerForView.getInstance().getNumColsArena()
        );

        loadSprites();
        EntityViewBinder.getInstance().init();
    }

    @Override
    public void openWindow(Stage stage) {
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        Pane root = new Pane(canvas);
        gc = canvas.getGraphicsContext2D();

        
        
        stage.setScene(new Scene(root));
        stage.setTitle("JRoyale");
        stage.show();

    }

    @Override
    public double getCanvasWidth() {
        return CANVAS_WIDTH;
    }

    @Override
    public double getCanvasHeight() {
        return CANVAS_HEIGHT;
    }
    
    @Override
    public void update(long now) {
        // clears canvas
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);  

        //globalScale -= 0.001;

    }

    @Override
    public double getDx() {
        return ArenaView.getInstance().getDx();
    }

    @Override
    public double getDy() {
        return ArenaView.getInstance().getDy();
    }

    @Override
    public double getMapTopLeftCornerX() {
        return ArenaView.getInstance().getMapBounds().getMinX();
    }

    @Override
    public double getMapTopLeftCornerY() {
        return ArenaView.getInstance().getMapBounds().getMinY();
    }

    //
    // begin renderWorldImage methods:
    //

    @Override
    public void renderWorldImage(Image image, double centerX, double centerY, double width, double height) {

        /* 
        The transformed Center C' is computated by doing this operations in sequence:

        1) The point C = (centerX, centerY) is related to canvas top left corner, 
            so it has to be transformed on a new Point C' related to canvas center.
            C' = C - (WIDTH/2, HEIGHT/2)
        2) C' can now be scaled based on globalScale:
            C' = C' * globalScale
        3) Now it's necessary to go back to canvas top left corner system to render correctly
            the image:
            C' = C' + (WIDTH/2, HEIGHT/2)
        4) Simply use the renderScreenImage methon with C' as a center and 
            (width, height) * globalScale as the dimension of the image.
         */

        double transformedCenterX = centerToTopLeftCanvasX(globalScale * topLeftToCenterCanvasX(centerX));
        double transformedCenterY = centerToTopLeftCanvasY(globalScale * topLeftToCenterCanvasY(centerY));

        renderScreenImage(image, transformedCenterX, transformedCenterY, width * globalScale, height * globalScale);
    }

    private double topLeftToCenterCanvasX(double coordX) {
        return coordX - CANVAS_WIDTH/2;
    }

    private double topLeftToCenterCanvasY(double coordY) {
        return coordY - CANVAS_HEIGHT/2;
    }

    private double centerToTopLeftCanvasX(double coordX) {
        return coordX + CANVAS_WIDTH/2;
    }

    private double centerToTopLeftCanvasY(double coordY) {
        return coordY + CANVAS_HEIGHT/2;
    }

    //
    // end renderWorldImage methods:
    //

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
        ArenaView.getInstance().renderArena(false);
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

        /* Color color = Color.BLUE;
        if (side == Side.OPPONENT) {
            color = Color.RED;
        }

        fillPoint(
            centreX, 
            centreY,
            10,
            color
        ); */

        EntityViewBinder.getInstance().getViewInstance(type).render(centreX, centreY, angleDirection, currentFrame, state, side);
        
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
