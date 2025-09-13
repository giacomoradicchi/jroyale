package jroyale.view;

import jroyale.utils.ImageUtils;

import java.awt.geom.Rectangle2D;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class View implements IView {
    private GraphicsContext gc;
    private double width, height;
    private int rowCount, colsCount;
    private float dx, dy;

    // images attribs
    private Image imgArena, reference;
    private Image imgPlayerKingTower;

    // images path
    private final static String ARENA_RELATIVE_PATH = "arena.png";
    private final static String PLAYER_KING_TOWER_RELATIVE_PATH = "towers/player/king_tower.png";
    
    // Coordinates determined empirically through testing
    private final static double ARENA_IMG_SCALE = 0.72;
    private final static int ARENA_Y_OFFSET = -72;

    private final static int MARGIN_X = 32; 
    private final static int TOP_MARGIN_Y = 135 + ARENA_Y_OFFSET;
    private final static int BOTTOM_MARGIN_Y = 687 + ARENA_Y_OFFSET;

    // bounding box of tower:
    private Rectangle2D bbKingTower;

    public View(Canvas canvas, int rowCount, int colsCount) {
        this.gc = canvas.getGraphicsContext2D();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.rowCount = rowCount;
        this.colsCount = colsCount;
        calculateDxDy();

        this.imgArena = new Image(getClass().getResourceAsStream("/jroyale/images/" + ARENA_RELATIVE_PATH));
        this.reference = new Image(getClass().getResourceAsStream("/jroyale/images/reference.jpg"));
        this.imgPlayerKingTower = new Image(getClass().getResourceAsStream("/jroyale/images/" + PLAYER_KING_TOWER_RELATIVE_PATH));

        this.bbKingTower = ImageUtils.getAlphaBoundingBox(imgPlayerKingTower);
    }

    @Override
    public void initializeRendering() {
        // clears canvas
        gc.clearRect(0, 0, width, height);  

        // set opacity to 100%
        gc.setGlobalAlpha(1);

        // update dx and dy factor
        calculateDxDy();
    }

    private void calculateDxDy() {
        this.dx = (float) Math.abs(width - 2*MARGIN_X)/colsCount;
        this.dy = (float) Math.abs(BOTTOM_MARGIN_Y - TOP_MARGIN_Y)/rowCount;
    }

    public void renderCells(boolean[][] cells) {

        renderGrid(rowCount, colsCount, dx, dy);

        // drawing only reachable tiles:
        gc.setFill(Color.GREEN);
        gc.setGlobalAlpha(0.25);
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colsCount; j++) {
                if (cells[i][j]) {
                    gc.fillRect(MARGIN_X + j*dx, TOP_MARGIN_Y + i*dy, dx, dy);
                }
            }
        }
        gc.setGlobalAlpha(1);
    }

    private void renderGrid(int num_rows, int num_cols, double dx, double dy) {
        gc.setGlobalAlpha(1);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        for (int j = 0; j <= num_cols; j++) {
            gc.strokeLine(MARGIN_X + j*dx, TOP_MARGIN_Y, MARGIN_X + j*dx, BOTTOM_MARGIN_Y);
        }

        for (int i = 0; i <= num_rows; i++) {
            gc.strokeLine(MARGIN_X, TOP_MARGIN_Y + i*dy, width - MARGIN_X, TOP_MARGIN_Y + i*dy);
        }
    }

    public void render(long millisecs) {
        // Draws arena
        drawArena();

        // temporary: sets the alpha value (opacity) based on how many millisecs have passed
        double durata = 8000; // millisec 
        int resto = (int) (millisecs % durata);
        
        double alpha = resto/(durata/2);
        if (resto >= durata/2) {
            alpha = 2 - alpha; 
        }

        gc.setGlobalAlpha(alpha*0);

        // draws the reference arena
        double referenceScale = width / reference.getWidth();
        
        gc.drawImage(reference, 0, 0, reference.getWidth() * referenceScale, reference.getHeight() * referenceScale);
        gc.setGlobalAlpha(1);
    }

    private void drawArena() {
        gc.drawImage(
            imgArena, 
            (width - imgArena.getWidth() * ARENA_IMG_SCALE)/2, 
            (height - imgArena.getHeight() * ARENA_IMG_SCALE)/2 + ARENA_Y_OFFSET, 
            imgArena.getWidth() * ARENA_IMG_SCALE, 
            imgArena.getHeight() * ARENA_IMG_SCALE
        );
    }

    @Override
    public void renderPlayerKingTower(float centreLogicX, float centreLogicY) {
        
        fillPoint(
            logic2GraphicX(centreLogicX), 
            logic2GraphicY(centreLogicY)
        );

        

        gc.drawImage(ImageUtils.enhanceOpacity(imgPlayerKingTower), 
        width/2 - imgPlayerKingTower.getWidth()/2, 
        height/2 - imgPlayerKingTower.getHeight()/2);
    }

    private void fillPoint(float centreX, float centreY) {
        int defaultSize = 10;
        fillPoint(centreX, centreY, defaultSize);
    }

    private void fillPoint(float centreX, float centreY, int size) {
        Paint previousColor = gc.getFill();
        double previousAlpha = gc.getGlobalAlpha();

        gc.setFill(Color.BLACK);
        gc.setGlobalAlpha(1);

        gc.fillOval(
            centreX - size/2, 
            centreY - size/2,
            10, 
            10
        );

        // restoring previous settings
        gc.setFill(previousColor);
        gc.setGlobalAlpha(previousAlpha);
    }

    private float logic2GraphicX(float logicCoordX) {
        return MARGIN_X + logicCoordX*dx;
    }

    private float logic2GraphicY(float logicCoordY) {
        return TOP_MARGIN_Y + logicCoordY*dy;
    }
}
