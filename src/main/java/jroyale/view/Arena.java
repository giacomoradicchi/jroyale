package jroyale.view;

import java.awt.geom.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Arena {
    private final static String ARENA_RELATIVE_PATH = "/jroyale/images/arenas/IMG_6164.PNG";

    // Arena Config values found empirically through testing
    private final static double SCALE = 0.417; // initial scale factor of the image
    private final static double NORMALIZED_MAP_WIDTH = 1 - 2*(32 / (800 * 607.0 / 1080)); 
    private final static double NORMALIZED_MAP_HEIGHT = 552.0 / 800;
    private final static double NORMALIZED_FIXED_Y = 0.5315; 
    private final static double NORMALIZED_SHIFT_Y = -72.0 / 800;

    private Image arenaImage;
    private double width, height;
    private Rectangle2D mapBoundingBox;
    private double globalShiftY;
    private double dx, dy;

    private final int NUM_ROWS, NUM_COLS;
    private final double INITIAL_CANVAS_WIDTH, INITIAL_CANVAS_HEIGHT;
    

    Arena(double canvasWidth, double canvasHeight, double globalScale, int num_rows, int num_cols) {
        arenaImage = new Image(Arena.class.getResourceAsStream(ARENA_RELATIVE_PATH));
        width = arenaImage.getWidth();
        height = arenaImage.getHeight();
        mapBoundingBox = new Rectangle2D.Double(0, 0, 0, 0);
        NUM_ROWS = num_rows;
        NUM_COLS = num_cols;
        INITIAL_CANVAS_WIDTH = canvasWidth;
        INITIAL_CANVAS_HEIGHT = canvasHeight;
        update(canvasWidth, canvasHeight, globalScale);
    }

    public Image getArenaImage() {
        return arenaImage;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void update(double canvasWidth, double canvasHeight, double globalScale) {
        double mapWidth = NORMALIZED_MAP_WIDTH*canvasWidth*globalScale;
        double mapHeight = NORMALIZED_MAP_HEIGHT*canvasHeight*globalScale;
        globalShiftY = NORMALIZED_SHIFT_Y*canvasHeight*globalScale; // la mappa è alzata verso l'alto, non è centrata.

        mapBoundingBox.setRect(
            (canvasWidth - mapWidth)/2,
            (canvasHeight - mapHeight)/2 + globalShiftY,
            mapWidth,
            mapHeight
        );

        calculateDxDy();
    }

    private void calculateDxDy() {
        this.dx = (float) mapBoundingBox.getWidth()/NUM_COLS;
        this.dy = (float) mapBoundingBox.getHeight()/NUM_ROWS;
    }

    public void renderArena(GraphicsContext gc, double canvasWidth, double canvasHeight, double globalScale) {
        renderArena(gc, canvasWidth, canvasHeight, globalScale, false);
    } 

    public void renderArena(GraphicsContext gc, double canvasWidth, double canvasHeight, double globalScale, boolean debugMode) {
        double scaleX = canvasWidth / INITIAL_CANVAS_WIDTH;
        double scaleY = canvasHeight / INITIAL_CANVAS_HEIGHT;

        gc.drawImage(
            arenaImage, 
            canvasWidth * 0.5 - width * SCALE * globalScale * 0.5 * scaleX, 
            canvasHeight * 0.5 - height * SCALE * globalScale * NORMALIZED_FIXED_Y * scaleY + globalShiftY , 
            width * SCALE * globalScale * scaleX,
            height * SCALE * globalScale * scaleY
        );

        if (!debugMode) return;
        System.out.println("Width " + canvasWidth);
        System.out.println("Height " + canvasHeight);
        

        renderGrid(gc);
    } 

    public void renderCells(GraphicsContext gc, boolean[][] cells) {

        renderGrid(gc);

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
        gc.restore();
    }

    public Rectangle2D getMapBounds() {
        return mapBoundingBox;
    }

    private void renderGrid(GraphicsContext gc) {
        gc.save();

        gc.setGlobalAlpha(1);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        for (int j = 0; j <= NUM_COLS; j++) {
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

        gc.restore();
    }

}
