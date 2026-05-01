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

    // canvas settings
    private static final double WH_RATIO = 607.0 / 1080;
    private static final int CANVAS_HEIGHT = 800;
    private static final int CANVAS_WIDTH = (int) (CANVAS_HEIGHT * WH_RATIO);

    // coordinate system
    private static final double WORLD_CENTER_OFFSET = 0.5;

    // image effects
    private static final ColorAdjust MONOCHROME_EFFECT = new ColorAdjust();

    // opacity / effects
    private static final double PROGRESS_CIRCLE_ALPHA = 0.5;

    // progress circle
    private static final double PROGRESS_START_ANGLE = 90;
    private static final double PROGRESS_FULL_ARC = -360;
    private static final Color PROGRESS_COLOR = Color.WHITE;

    // shadow
    private static final double SHADOW_OPACITY = 0.7;
    private static final Color GRADIENT_COLOR = new Color(0, 0, 0, SHADOW_OPACITY);
    private static final Stop FIRST_STOP = new Stop(0, GRADIENT_COLOR);
    private static final Stop LAST_STOP = new Stop(1, Color.TRANSPARENT);

    private GraphicsContext gc;
    private Stage stage;
    private Pane root;

    private IView view;

    protected double globalScale = 1.0;

    private MainGUI() {
        MONOCHROME_EFFECT.setSaturation(-1);
        MONOCHROME_EFFECT.setContrast(-0.2);
        MONOCHROME_EFFECT.setBrightness(-0.4);
    }

    @Override
    public void openWindow(Stage stage) {
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.root = new Pane(canvas);
        this.gc = canvas.getGraphicsContext2D();

        this.stage = stage;

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
        stage.getScene().setOnMousePressed(e ->
            view.processOnMousePressed(e.getSceneX(), e.getSceneY())
        );

        stage.getScene().setOnMouseDragged(e ->
            view.processOnMouseDragged(e.getSceneX(), e.getSceneY())
        );

        stage.getScene().setOnMouseReleased(e ->
            view.processOnMouseReleased()
        );
    }

    @Override
    public void addToRoot(Node node) {
        root.getChildren().add(node);
    }

    @Override
    public void resetNodes() {
        Node canvas = root.getChildren().getFirst();
        root.getChildren().clear();
        root.getChildren().add(canvas);
    }

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
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    // ---------------------------
    // world transform utilities
    // ---------------------------

    @Override
    public double fromWorldToScreenX(double x) {
        return centerToTopLeftCanvasX(globalScale * topLeftToCenterCanvasX(x));
    }

    @Override
    public double fromWorldToScreenY(double y) {
        return centerToTopLeftCanvasY(globalScale * topLeftToCenterCanvasY(y));
    }

    private double topLeftToCenterCanvasX(double x) {
        return x - CANVAS_WIDTH * WORLD_CENTER_OFFSET;
    }

    private double topLeftToCenterCanvasY(double y) {
        return y - CANVAS_HEIGHT * WORLD_CENTER_OFFSET;
    }

    private double centerToTopLeftCanvasX(double x) {
        return x + CANVAS_WIDTH * WORLD_CENTER_OFFSET;
    }

    private double centerToTopLeftCanvasY(double y) {
        return y + CANVAS_HEIGHT * WORLD_CENTER_OFFSET;
    }

    // ---------------------------
    // rendering
    // ---------------------------

    @Override
    public void renderWorldImage(
            Image image,
            double centerX,
            double centerY,
            double width,
            double height,
            boolean monochrome,
            double alpha
    ) {
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

    @Override
    public void renderScreenImage(
            Image image,
            double centerX,
            double centerY,
            double width,
            double height,
            boolean monochrome,
            double alpha
    ) {
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
    public void renderWorldShadow(double x, double y, double radius) {
        renderScreenShadow(
            fromWorldToScreenX(x),
            fromWorldToScreenY(y),
            radius * globalScale
        );
    }

    @Override
    public void renderScreenShadow(double x, double y, double radius) {
        gc.save();

        RadialGradient gradient = new RadialGradient(
            0,
            0,
            x,
            y,
            radius,
            false,
            null,
            FIRST_STOP,
            LAST_STOP
        );
        
        gc.setFill(gradient);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        gc.restore();
    }

    @Override
    public void fillWorldRoundedRect(
            double x, double y,
            double w, double h,
            double aw, double ah,
            double alpha,
            Color color
    ) {
        fillScreenRoundedRect(
            fromWorldToScreenX(x),
            fromWorldToScreenY(y),
            w * globalScale,
            h * globalScale,
            aw * globalScale,
            ah * globalScale,
            alpha,
            color
        );
    }

    @Override
    public void strokeWorldRoundedRect(
            double x, double y,
            double w, double h,
            double aw, double ah,
            double lw,
            double alpha,
            Color color
    ) {
        strokeScreenRoundedRect(
            fromWorldToScreenX(x),
            fromWorldToScreenY(y),
            w * globalScale,
            h * globalScale,
            aw * globalScale,
            ah * globalScale,
            lw * globalScale,
            alpha,
            color
        );
    }

    @Override
    public void fillScreenRoundedRect(
            double x, double y,
            double w, double h,
            double aw, double ah,
            double alpha,
            Color color
    ) {
        gc.save();
        gc.setGlobalAlpha(alpha);
        gc.setFill(color);

        gc.fillRoundRect(
            x - w / 2,
            y - h / 2,
            w,
            h,
            aw,
            ah
        );

        gc.restore();
    }

    @Override
    public void strokeScreenRoundedRect(
            double x, double y,
            double w, double h,
            double aw, double ah,
            double lw,
            double alpha,
            Color color
    ) {
        gc.save();
        gc.setGlobalAlpha(alpha);
        gc.setStroke(color);
        gc.setLineWidth(lw);

        gc.strokeRoundRect(
            x - w / 2,
            y - h / 2,
            w,
            h,
            aw,
            ah
        );

        gc.restore();
    }

    @Override
    public void strokeWorldLine(
            double x1, double y1,
            double x2, double y2,
            double alpha,
            Color color,
            double width
    ) {
        strokeScreenLine(
            fromWorldToScreenX(x1),
            fromWorldToScreenY(y1),
            fromWorldToScreenX(x2),
            fromWorldToScreenY(y2),
            alpha,
            color,
            width * globalScale
        );
    }

    @Override
    public void strokeScreenLine(
            double x1, double y1,
            double x2, double y2,
            double alpha,
            Color color,
            double width
    ) {
        gc.save();
        gc.setGlobalAlpha(alpha);
        gc.setStroke(color);
        gc.setLineWidth(width);
        gc.strokeLine(x1, y1, x2, y2);
        gc.restore();
    }

    @Override
    public void fillScreenTextFromCenter(
            String text,
            double x,
            double y,
            Font font,
            Color color,
            double alpha
    ) {
        gc.save();
        gc.setFont(font);
        gc.setFill(color);
        gc.setGlobalAlpha(alpha);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.fillText(text, x, y);
        gc.restore();
    }

    @Override
    public void strokeScreenTextFromCenter(
            String text,
            double x,
            double y,
            Font font,
            Color color,
            double width,
            double alpha
    ) {
        gc.save();
        gc.setFont(font);
        gc.setStroke(color);
        gc.setLineWidth(width);
        gc.setGlobalAlpha(alpha);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.strokeText(text, x, y);
        gc.restore();
    }

    @Override
    public void fillPoint(double x, double y, int size, Color color) {
        gc.save();
        gc.setFill(color);

        gc.fillOval(
            x - size / 2,
            y - size / 2,
            size,
            size
        );

        gc.restore();
    }

    @Override
    public void fillScreenCircularProgress(
            double x,
            double y,
            double radius,
            double progress
    ) {
        double extent = progress * PROGRESS_FULL_ARC;

        gc.save();
        gc.setGlobalAlpha(PROGRESS_CIRCLE_ALPHA);
        gc.setFill(PROGRESS_COLOR);

        gc.fillArc(
            x - radius,
            y - radius,
            radius * 2,
            radius * 2,
            PROGRESS_START_ANGLE,
            extent,
            ArcType.ROUND
        );

        gc.restore();
    }

    // static methods
    public static IMainGUI getInstance() {
        if (instance == null) instance = new MainGUI();
        return instance;
    }
}