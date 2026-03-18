package jroyale.view;

import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import jroyale.controller.ControllerForView;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public class View implements IView {

    private static IView instance = null;

    private static final double WH_RATIO = 607.0 / 1080;

    private static final double CANVAS_HEIGHT = 800;
    private static final double CANVAS_WIDTH = CANVAS_HEIGHT * WH_RATIO;

    private GraphicsContext gc;
    private Stage stage;
    private Pane root;

    // scale of the entire scene
    private double globalScale = 1.0;

    private View() {}

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
        DeckView.getInstance().init(ControllerForView.getInstance().getAvailableDeckCards());
        FontManager.getInstance().init();
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
            processOnMousePressed(
                event.getSceneX(), 
                event.getSceneY()
            );
        });

        stage.getScene().setOnMouseDragged(event -> {
            processOnMouseDragged(
                event.getSceneX(), 
                event.getSceneY()
            );
        });

        stage.getScene().setOnMouseReleased(event -> {
            processOnMouseReleased();
        });
    }

    @Override
    public void processOnMousePressed(double x, double y) {
        ControllerForView.getInstance().handleMouseSelectedTile(
            getRowFromMouseY(y),
            getColFromMouseX(x)
        );
    }

    @Override
    public void processOnMouseDragged(double x, double y) {
        ControllerForView.getInstance().handleMouseSelectedTile(
            getRowFromMouseY(y),
            getColFromMouseX(x)
        );
    }

    @Override
    public void processOnMouseReleased() {
        ControllerForView.getInstance().dropSelectedPlayerCardOnLastMousePos();

        ControllerForView.getInstance().handleMouseReleased();
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
        DeckView.getInstance().setVisibleSelectedCard(false);
        DragPlacementPreview.getInstance().startAnimation();
    }

    @Override
    public void renderDragPlacementPreview(double centreX, double centreY) {
        DragPlacementPreview.getInstance().render(centreX, centreY);
    }

    @Override
    public void stopDragPlacementPreview() {
        DeckView.getInstance().setVisibleSelectedCard(true);
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
        gc.setLineWidth(lineWidth);
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
    public void strokeWorldLine(double x1, double y1, double x2, double y2, double alpha, Color color, double lineWidth) {
        strokeScreenLine(
            fromWorldToScreenX(x1), 
            fromWorldToScreenY(y1),
            fromWorldToScreenX(x2), 
            fromWorldToScreenY(y2),
            alpha, 
            color, 
            lineWidth * globalScale
        );
    }

    @Override
    public void strokeScreenLine(double x1, double y1, double x2, double y2, double alpha, Color color, double lineWidth) {
        gc.save();
        gc.setGlobalAlpha(alpha);
        gc.setStroke(color);
        gc.setLineWidth(lineWidth);
        gc.strokeLine(x1, y1, x2, y2);
        gc.restore();
    }

    @Override
    public void fillScreenTextFromCenter(String text, double centerX, double centerY, Font font, Color color) {
        gc.save();
        gc.setFont(font);
        gc.setFill(color);

        // alligned in center
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.fillText(text, centerX, centerY);
        gc.restore();
    }

    @Override
    public void strokeScreenTextFromCenter(String text, double centerX, double centerY, Font font, Color color, double lineWidth) {
        gc.save();
        gc.setFont(font);
        gc.setStroke(color);
        gc.setLineWidth(lineWidth);

        // alligned in center
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.strokeText(text, centerX, centerY);
        gc.restore();
    }

    @Override
    public void renderPlayerDeck(EntityType card1, byte elixirCost1, EntityType card2, byte elixirCost2, EntityType card3, byte elixirCost3, EntityType card4, byte elixirCost4, byte elixirLeft, double elixirChargeTimeProgress, byte maxElixir) {
        DeckView.getInstance().renderPlayerDeck(card1, elixirCost1, card2, elixirCost2, card3, elixirCost3, card4, elixirCost4, elixirLeft, elixirChargeTimeProgress, maxElixir);
    }

    @Override
    public void setSelectedCard(int cardIndex) {
        ControllerForView.getInstance().setSelectedPlayerCard(cardIndex);
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
    public static IView getInstance() {
        if (instance == null) {
            instance = new View();
        }

        return instance;
    }
    
}
