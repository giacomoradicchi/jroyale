package jroyale.view;

import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MainGUI implements IMainGUI {

    private static MainGUI instance = null;

    private static final double WH_RATIO = 607.0 / 1080;

    private static final int CANVAS_HEIGHT = 800;
    private static final int CANVAS_WIDTH = (int) (CANVAS_HEIGHT * WH_RATIO);
    private static final ColorAdjust MONOCHROME_EFFECT = new ColorAdjust();
    private static final double PROGRESS_CIRCLE_START_ARC = 90;
    private static final double PROGRESS_CIRCLE_MAX_EXTENT = -360;
    private static final double PROGRESS_CIRCLE_ALPHA = 0.5;
    private static final Color PROGRESS_CIRCLE_COLOR = Color.WHITE;
    
    
    private GraphicsContext gc;
    private Stage stage;
    private Pane root;

    // current view
    private IView view;

    // scale of the entire scene
    protected double globalScale = 1.0;

    private MainGUI() {
        MONOCHROME_EFFECT.setSaturation(-1); // if applied, image will be rendered in black and white
        MONOCHROME_EFFECT.setContrast(-0.2); 
        MONOCHROME_EFFECT.setBrightness(-0.4); // if applied, image will be rendered in black and white
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

        if (!stage.isShowing()) stage.show();
        handleMouseEvents();
    }

    @Override
    public void setView(IView view) {
        this.view = view;
    }

    @Override
    public void closeWindow() {
        stage.close();
    }

    private void handleMouseEvents() {
        System.out.println("handleMouseEvents chiamato da: " + this.getClass().getSimpleName());
    System.out.println("stage: " + stage);
    System.out.println("scene: " + (stage != null ? stage.getScene() : "stage null"));

        stage.getScene().setOnMousePressed(event -> {
            view.processOnMousePressed(
                event.getSceneX(), 
                event.getSceneY()
            );
            System.out.println("choir");
        });

        stage.getScene().setOnMouseDragged(event -> {
            view.processOnMouseDragged(
                event.getSceneX(), 
                event.getSceneY()
            );
        });

        stage.getScene().setOnMouseReleased(event -> {
            view.processOnMouseReleased();
        });
    }

    @Override
    public void addToRoot(Node node) {
        root.getChildren().add(node);
        //root.getChildren().forEach(n -> System.out.println(n.getClass().getSimpleName() + " mouseTransparent: " + n.isMouseTransparent()));
        //System.out.println("\n");
    }

    @Override
    public void resetNodes() {
        // removes every node except canvas (first one)
        Node canvas = this.root.getChildren().getFirst();
        this.root.getChildren().clear();
        root.getChildren().add(canvas);
    }

    /* @Override
    public void init() {
        //buildUI();
    } */

    /* private void renderLoadingScreen() {
       
        double height = getCanvasHeight();
        double width = LOADING_BACKGROUND.getWidth() * height / LOADING_BACKGROUND.getHeight(); 
        renderScreenImage(LOADING_BACKGROUND, getCanvasWidth()/2, getCanvasHeight()/2, width, height, 1);
        
        width = getCanvasWidth() * NORMALIZED_LOGO_WIDTH;
        height = LOGO.getHeight() / LOGO.getWidth() * width;
        renderScreenImage(LOGO, getCanvasWidth() * NORMALIZED_LOGO_X, getCanvasHeight() * NORMALIZED_LOGO_Y, width, height, 1);
        
    } */

    // Restituisce il Task così chi chiama può sapere quando ha finito
    /* public Task<Void> loadAsync() {
        renderLoadingScreen();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                load();
                return null;
            }
        };
        new Thread(task, "sprite-loading-thread").start();
        return task;
    } */

    @Override
    public int getCanvasWidth() {
        return CANVAS_WIDTH;
    }

    @Override
    public int getCanvasHeight() {
        return CANVAS_HEIGHT;
    }

    @Override
    public double getGlobalScale() {
        return globalScale;
    }

    @Override
    public void setGlobalScale(double globalScale) {
        this.globalScale = globalScale;
    }

    @Override
    public void clearWindow() {
        gc.clearRect(0, 0, getCanvasWidth(), getCanvasHeight());
    }

    //
    // begin renderWorldImage methods:
    //

    @Override
    public void renderWorldImage(Image image, double centerX, double centerY, double width, double height, boolean monochrome, double alpha) {
        renderScreenImage(
            image, 
            fromWorldToScreenX(centerX), 
            fromWorldToScreenY(centerY), 
            width * globalScale, 
            height * globalScale, 
            monochrome,
            alpha
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

    @Override
    public double fromWorldToScreenX(double coordX) {
        return centerToTopLeftCanvasX(globalScale * topLeftToCenterCanvasX(coordX));
    }

    @Override
    public double fromWorldToScreenY(double coordY) {
        return centerToTopLeftCanvasY(globalScale * topLeftToCenterCanvasY(coordY));
    }

    protected double topLeftToCenterCanvasX(double coordX) {
        return coordX - getCanvasWidth()/2;
    }

    protected double topLeftToCenterCanvasY(double coordY) {
        return coordY - getCanvasHeight()/2;
    }

    protected double centerToTopLeftCanvasX(double coordX) {
        return coordX + getCanvasWidth()/2;
    }

    protected double centerToTopLeftCanvasY(double coordY) {
        return coordY + getCanvasHeight()/2;
    }

    //
    // end renderWorldImage methods:
    //

    @Override
    public void renderScreenImage(Image image, double centerX, double centerY, double width, double height, boolean monochrome, double alpha) {
        gc.save();
        gc.setGlobalAlpha(alpha);

        if (monochrome) gc.setEffect(MONOCHROME_EFFECT);

        gc.drawImage(
            image, 
            centerX - width / 2, 
            centerY - height / 2,
            width, 
            height
        );

        gc.restore();
    }

    @Override
    public void renderWorldShadow(double centerX, double centerY, double shadowRadius) {
        renderScreenShadow(
            fromWorldToScreenX(centerX), 
            fromWorldToScreenY(centerY), 
            shadowRadius * globalScale
        );
    }

    @Override
    public void renderScreenShadow(double centreX, double centreY, double shadowRadius) {
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
        gc.fillOval(centreX - shadowRadius, centreY - shadowRadius, 2 * shadowRadius, 2 * shadowRadius); 
        gc.restore();
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
    public void fillScreenTextFromCenter(String text, double centerX, double centerY, Font font, Color color, double alpha) {
        gc.save();
        gc.setFont(font);
        gc.setFill(color);
        gc.setGlobalAlpha(alpha);

        // alligned in center
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.fillText(text, centerX, centerY);
        gc.restore();
    }

    @Override
    public void strokeScreenTextFromCenter(String text, double centerX, double centerY, Font font, Color color, double lineWidth, double alpha) {
        gc.save();
        gc.setFont(font);
        gc.setStroke(color);
        gc.setLineWidth(lineWidth);
        gc.setGlobalAlpha(alpha);

        // alligned in center
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.strokeText(text, centerX, centerY);
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

    @Override
    public void fillScreenCircularProgress(double centreX, double centreY, double radius, double progress) {
        
        double diameter = radius * 2;
        double arcExtent = progress * PROGRESS_CIRCLE_MAX_EXTENT; 

        gc.save();
        gc.setGlobalAlpha(PROGRESS_CIRCLE_ALPHA);
        gc.setFill(PROGRESS_CIRCLE_COLOR);
        gc.fillArc(centreX - radius, centreY - radius, diameter, diameter, PROGRESS_CIRCLE_START_ARC, arcExtent, ArcType.ROUND);
        gc.restore();
    }

    // static methods
    public static IMainGUI getInstance() {
        if (instance == null) {
            instance = new MainGUI();
        }

        return instance;
    }    
}
