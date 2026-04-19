package jroyale.view;

import java.awt.geom.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ArenaView {

    private static ArenaView instance = null;

    private final static String ARENA_RELATIVE_PATH = "/jroyale/images/arenas/IMG_6164.PNG";

    // Arena Config values found empirically through testing
    private final static double SCALE = 0.417; //0.417 // initial scale factor of the image
    private final static double NORMALIZED_MAP_WIDTH = 1 - 2*(32 / (800 * 607.0 / 1080)); 
    private final static double NORMALIZED_MAP_HEIGHT = 552.0 / 800;
    private final static double NORMALIZED_SHIFT_Y = -72.0 / 800;

    private Image arenaImage;
    private Rectangle2D mapBoundingBox;
    private double globalShiftY;
    private double dx, dy;
    private int rows, cols;
    

    private ArenaView() {}

    public void init(int numRows, int numCols) {
        arenaImage = new Image(ArenaView.class.getResourceAsStream(ARENA_RELATIVE_PATH));
        initMapBoundingBox(numRows, numCols);
    }

    public Image getArenaImage() {
        return arenaImage;
    }

    public double getWidth() {
        return arenaImage.getWidth();
    }

    public double getHeight() {
        return arenaImage.getHeight();
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void update() {
        calculateMapBoundingBox();
        calculateDxDy();
    }

    private void initMapBoundingBox(int numRows, int numCols) {
        this.rows = numRows;
        this.cols = numCols;
        update();
    }

    private void calculateMapBoundingBox() {
        double canvasWidth = View.getInstance().getCanvasWidth();
        double canvasHeight = View.getInstance().getCanvasHeight();

        double mapWidth = NORMALIZED_MAP_WIDTH * canvasWidth;
        double mapHeight = NORMALIZED_MAP_HEIGHT * canvasHeight;
        globalShiftY = NORMALIZED_SHIFT_Y*canvasHeight; // map is not centered.

        mapBoundingBox = new Rectangle2D.Double(
            (canvasWidth - mapWidth) / 2,
            (canvasHeight - mapHeight) / 2 + globalShiftY,
            mapWidth,
            mapHeight
        );
    }

    private void calculateDxDy() {
        this.dx = (float) mapBoundingBox.getWidth() / cols;
        this.dy = (float) mapBoundingBox.getHeight() / rows;
    }

    public void renderArena() {
        renderArena(false);
    } 

    public void renderArena(boolean debugMode) {

        double canvasWidth = View.getInstance().getCanvasWidth();
        double canvasHeight = View.getInstance().getCanvasHeight();

        View.getInstance().renderWorldImage(
            arenaImage, 
            canvasWidth * 0.5, 
            canvasHeight * 0.5 - 108 * (View.getInstance().getCanvasHeight() / 800), 
            getWidth() * SCALE * (View.getInstance().getCanvasWidth() / 449.6296296296296), 
            getHeight() * SCALE * (View.getInstance().getCanvasHeight() / 800)
        );  

        if (!debugMode) return;
        System.out.println("Width " + canvasWidth);
        System.out.println("Height " + canvasHeight);
        

        //renderGrid(gc);
    } 

    public void renderCells(GraphicsContext gc, boolean[][] cells) {

        /* renderGrid(gc);

        // drawing only reachable tiles:
        gc.save();
        gc.setFill(Color.GREEN);
        gc.setGlobalAlpha(0.25);
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (cells[i][j]) {
                    gc.fillRect(
                        mapBoundingBox.getMinX() + j*dx, 
                        mapBoundingBox.getMinY() + i*dy, 
                        dx, 
                        dy
                    );
                }
            }
        }
        gc.restore(); */
    }

    public Rectangle2D getMapBounds() {
        return mapBoundingBox;
    }

    private void renderGrid() {
        /*
        gc.save();

        gc.setGlobalAlpha(1);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        for (int j = 0; j <= View.getInstance().getNNUM_COLS; j++) {
            gc.strokeLine(
                mapBoundingBox.getMinX() + j*dx, 
                mapBoundingBox.getMinY(), 
                mapBoundingBox.getMinX() + j*dx, 
                mapBoundingBox.getMaxY()
            );
        }

        for (int i = 0; i <= NUM_ROWS; i++) {
            gc.strokeLine(
                mapBoundingBox.getMinX(), 
                mapBoundingBox.getMinY() + i*dy, 
                mapBoundingBox.getMaxX(), 
                mapBoundingBox.getMinY() + i*dy
            );
        }

        gc.restore(); */
    }

    // static methods

    public static ArenaView getInstance() {
        if (instance == null) {
            instance = new ArenaView();
        }

        return instance;
    }

}
