package jroyale.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import jroyale.controller.ControllerForView;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public class View2 implements IView2 {

    private static IView2 instance = null;

    private static final double WH_RATIO = 607.0 / 1080;

    private static final double CANVAS_HEIGHT = 800;
    private static final double CANVAS_WIDTH = CANVAS_HEIGHT * WH_RATIO;

    private GraphicsContext gc;
    private Stage stage;
    private Pane root;

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

        //loadSprites();
        EntityViewBinder.getInstance().init();
        MouseManager.getInstance().init(stage.getScene());
        DeckView.getInstance().init();
        handleMouseEvents();
    }

    @Override
    public void openWindow(Stage stage) {
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        Pane root = new Pane(canvas);
        gc = canvas.getGraphicsContext2D();

        this.stage = stage;
        this.root = root;

        stage.setScene(new Scene(root));
        stage.setTitle("JRoyale");
        stage.show();

    }

    @Override
    public void addToRoot(Node node) {
        root.getChildren().add(node);
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

        DragPlacementPreview.getInstance().update(now);

        //globalScale -= 0.0001;
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
        renderScreenImage(
            image, 
            fromWorldToScreenX(centerX), 
            fromWorldToScreenY(centerY), 
            width * globalScale, 
            height * globalScale
        );
    }

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

    private double fromWorldToScreenX(double coordX) {
        return centerToTopLeftCanvasX(globalScale * topLeftToCenterCanvasX(coordX));
    }

    private double fromWorldToScreenY(double coordY) {
        return centerToTopLeftCanvasY(globalScale * topLeftToCenterCanvasY(coordY));
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
    public void renderEntity(double centreX, double centreY, int currentHealth, int maxHealth, double shadowRadius, double angleDirection, int currentFrame, State state, Side side, EntityType type) {
        EntityViewBinder.getInstance().getViewInstance(type).render(centreX, centreY, currentHealth, maxHealth, shadowRadius, angleDirection, currentFrame, state, side);
    }

    @Override
    public void renderWorldShadow(double centerX, double centerY, double shadowRadius) {
        renderScreenShadow(
            fromWorldToScreenX(centerX), 
            fromWorldToScreenY(centerY), 
            shadowRadius * globalScale
        );
    }

    private void renderScreenShadow(double centreX, double centreY, double shadowRadius) {
        // Definiamo il gradiente radiale
        gc.save();
        RadialGradient gradient = new RadialGradient(
            0,      // focusAngle
            0,      // focusDistance
            centreX, // centerX (coordinata assoluta sul canvas)
            centreY, // centerY (coordinata assoluta sul canvas)
            shadowRadius, // radius (metà del diametro)
            false,  // proportional: false perché usiamo i pixel esatti
            null,   // cycleMethod
            new Stop(0, new Color(0, 0, 0, 0.7)),              // Centro opaco
            new Stop(1, Color.TRANSPARENT)       // Bordo trasparente
        );

        gc.setFill(gradient);
        gc.fillOval(centreX - shadowRadius, centreY - shadowRadius, shadowRadius*2, shadowRadius*2); // Disegna il cerchio
        gc.restore();
    }

    /* private void loadSprites() {
        // forcing loading of all troop sprites
        TroopType.values();
    } */


    // mouse Events

    private void handleMouseEvents() {
        stage.getScene().setOnMousePressed(event -> {
            ControllerForView.getInstance().handleMouseSelectedTile(
                getRowFromMouseY(event.getSceneY()),
                getColFromMouseX(event.getSceneX())
            );
        });

        stage.getScene().setOnMouseDragged(event -> {
            ControllerForView.getInstance().handleMouseSelectedTile(
                getRowFromMouseY(event.getSceneY()),
                getColFromMouseX(event.getSceneX())
            );

            //System.out.println(getRowFromMouseY(event.getSceneY()) + ", " + getColFromMouseX(event.getSceneX()));
            
        });

        stage.getScene().setOnMouseReleased(event -> {
            ControllerForView.getInstance().dropSelectedPlayerCardOnLastMousePos();

            ControllerForView.getInstance().handleMouseReleased();
        });
    }

    private int getColFromMouseX(double mouseX) {
        return (int) Math.floor(
            (mouseX - fromWorldToScreenX(getMapTopLeftCornerX())) / (getDx() * globalScale)
        ); 
    }

    private int getRowFromMouseY(double mouseY) {
        return (int) Math.floor(
            (mouseY - fromWorldToScreenY(getMapTopLeftCornerY())) / (getDy() * globalScale)
        ); 
    }

    @Override
    public void startDragPlacementPreview() {
        DragPlacementPreview.getInstance().startAnimation();
    }

    @Override
    public void renderDragPlacementPreview(double centreX, double centreY) {
        DragPlacementPreview.getInstance().render(centreX, centreY);
    }

    @Override
    public void stopDragPlacementPreview() {
        DragPlacementPreview.getInstance().stopAnimation();
    }

    @Override
    public void fillWorldRoundedRect(double centerX, double centerY, double width, double height, double arcWidth, double arcHeight, double alpha, Color color) {
        fillScreenRoundedRect(
            fromWorldToScreenX(centerX), 
            fromWorldToScreenY(centerY), 
            width * globalScale, 
            height * globalScale, 
            arcWidth * globalScale, 
            arcHeight * globalScale, 
            alpha, 
            color
        );
    }

    @Override
    public void strokeWorldRoundedRect(double centerX, double centerY, double width, double height, double arcWidth, double arcHeight, double lineWidth, double alpha, Color color) {
        strokeScreenRoundedRect(
            fromWorldToScreenX(centerX), 
            fromWorldToScreenY(centerY), 
            width * globalScale, 
            height * globalScale, 
            arcWidth * globalScale, 
            arcHeight * globalScale, 
            lineWidth * globalScale, 
            alpha, 
            color
        );
    }

    @Override
    public void fillScreenRoundedRect(double centerX, double centerY, double width, double height, double arcWidth, double arcHeight, double alpha, Color color) {
        gc.save();
        gc.setGlobalAlpha(alpha);
        gc.setFill(color);
        gc.fillRoundRect(
            centerX - width/2, 
            centerY - height/2, 
            width, 
            height, 
            arcWidth, 
            arcHeight
        );
        gc.restore();
    }

    @Override
    public void strokeScreenRoundedRect(double centerX, double centerY, double width, double height, double arcWidth, double arcHeight, double lineWidth, double alpha, Color color) {
        gc.save();
        gc.setGlobalAlpha(alpha);
        gc.setStroke(color);
        gc.setLineWidth(lineWidth * globalScale);
        gc.strokeRoundRect(
            centerX - width/2, 
            centerY - height/2, 
            width, 
            height, 
            arcWidth, 
            arcHeight
        );
        gc.restore();
    }

    @Override
    public void renderPlayerDeck(EntityType card1, EntityType card2, EntityType card3, EntityType card4) {
        DeckView.getInstance().renderPlayerDeck(card1, card2, card3, card4);
    }

    @Override
    public void setSelectedCard(int cardIndex) {
        ControllerForView.getInstance().setSelectedPlayerCard(cardIndex);
    }

    private void renderHealth(GraphicsContext gc, double centreX, double centreY, int currentHealth, int maxHealth, Side side) {
        double rectWidth = 0.1 * getCanvasWidth() * globalScale;
        double rectHeight = 0.01 * getCanvasHeight() * globalScale;
        double shiftY = -70 * globalScale;

        gc.save();

        if (side == Side.PLAYER) {
            gc.setStroke(Color.rgb(20, 20, 150));
            gc.setFill(Color.rgb(100, 100, 255));
        } else {
            gc.setStroke(Color.rgb(150, 20, 20));
            gc.setFill(Color.rgb(255, 100, 100));  
        }

        gc.setLineWidth(2);

        gc.fillRect(
            centreX - rectWidth / 2, 
            centreY - rectHeight / 2 + shiftY, 
            rectWidth,
            rectHeight
        );

        if (side == Side.PLAYER) {
            gc.setFill(Color.rgb(50, 50, 150));
        } else {
            gc.setFill(Color.rgb(150, 50, 50));
        }

        double percentage = (double) currentHealth / maxHealth;

        gc.fillRect(
            centreX - rectWidth / 2, 
            centreY - rectHeight / 2 + shiftY, 
            rectWidth * percentage,
            rectHeight
        );

        gc.strokeRoundRect(
            centreX - rectWidth / 2, 
            centreY - rectHeight / 2 + shiftY, 
            rectWidth,
            rectHeight,
            rectHeight/2,
            rectHeight/2
        );

        

        gc.restore();
    }

    @Override
    public void fillPoint(double centreX, double centreY, int size, Color color) {
        gc.save();

        gc.setFill(color);
        gc.setGlobalAlpha(1);

        gc.fillOval(
            centreX - size/2, 
            centreY - size/2,
            size, 
            size
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
