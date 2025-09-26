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
    private int rowsCount, colsCount;
    private float dx, dy;

    // images attribs
    private Image reference;
    private Image imgPlayerKingTower;
    private TransformedImage tImgArena;

    // images path
    private final static String PLAYER_KING_TOWER_RELATIVE_PATH = "towers/player/king_tower.png";

    private Image arenaImage;
    
    private Rectangle2D mapBoundingBox;

    // bounding box of tower
    private Rectangle2D bbKingTower;

    // scale of the entire scene
    private double scale = 1.0;

    public View(Canvas canvas, int rowsCount, int colsCount) {
        this.gc = canvas.getGraphicsContext2D();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.rowsCount = rowsCount;
        this.colsCount = colsCount;
        this.mapBoundingBox = new Rectangle2D.Double();

        this.arenaImage = ArenaConfig.createArenaImage(width, height, rowsCount, colsCount);
        
        this.reference = new Image(getClass().getResourceAsStream("/jroyale/images/reference2.png"));
        this.imgPlayerKingTower = new Image(getClass().getResourceAsStream("/jroyale/images/" + PLAYER_KING_TOWER_RELATIVE_PATH));
        

        this.tImgArena = new TransformedImage(
            new Image(getClass().getResourceAsStream(ArenaConfig.ARENA_RELATIVE_PATH)),
            ArenaConfig.ARENA_IMG_SCALE,
            ArenaConfig.ARENA_X_OFFSET,
            ArenaConfig.ARENA_Y_OFFSET
        );

        

        calculateDxDy();
        calculateMapBoundingBox();

        // adjusting tower image
        this.imgPlayerKingTower = ImageUtils.enhanceOpacity(imgPlayerKingTower);
    }

    private void calculateMapBoundingBox() {
        mapBoundingBox.setRect(
            ArenaConfig.MapConfig.NORMALIZED_TOPLEFT_CORNER_X * width,
            ArenaConfig.MapConfig.NORMALIZED_TOPLEFT_CORNER_Y * height,
            ArenaConfig.MapConfig.NORMALIZED_WIDTH * width,
            ArenaConfig.MapConfig.NORMALIZED_HEIGHT * height
        );
    }

    @Override
    public void initializeRendering(long millisec) {
        // clears canvas
        gc.clearRect(0, 0, width, height);  

        // set opacity to 100%
        gc.setGlobalAlpha(1);
        
        // update dx and dy factor
        calculateDxDy();
        calculateMapBoundingBox();
    }

    // draws a TransformedImage (image with proper scale attribs) from its centre
    private void drawTransformedImage(TransformedImage transfImg, double centreX, double centreY, double scale) {
        gc.drawImage(
            transfImg.getImage(), 
            centreX - transfImg.getWidth()/2 * scale + transfImg.getShiftX()* scale, 
            centreY - transfImg.getHeight()/2 * scale + transfImg.getShiftY() * scale, 
            transfImg.getWidth() * scale, 
            transfImg.getHeight() * scale
        );
    }

    private void drawArena(long millisec) {
        /* gc.drawImage(
            imgArena, 
            (width - imgArena.getWidth() * ARENA_IMG_SCALE)/2, 
            (height - imgArena.getHeight() * ARENA_IMG_SCALE)/2 + ARENA_Y_OFFSET, 
            imgArena.getWidth() * ARENA_IMG_SCALE, 
            imgArena.getHeight() * ARENA_IMG_SCALE
        );  */ 
        drawTransformedImage(tImgArena, width/2, height/2, scale);
    }

    private void calculateDxDy() {
        this.dx = (float) mapBoundingBox.getWidth()/colsCount;
        this.dy = (float) mapBoundingBox.getHeight()/rowsCount;
    }

    public void renderCells(boolean[][] cells) {

        renderGrid(rowsCount, colsCount, dx, dy);

        // drawing only reachable tiles:
        gc.setFill(Color.GREEN);
        gc.setGlobalAlpha(0.25);
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < colsCount; j++) {
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
        gc.setGlobalAlpha(1);
    }

    private void renderGrid(int num_rows, int num_cols, double dx, double dy) {
        gc.setGlobalAlpha(1);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        for (int j = 0; j <= num_cols; j++) {
            gc.strokeLine(
                mapBoundingBox.getMinX() + j*dx, 
                mapBoundingBox.getMinY(), 
                mapBoundingBox.getMinX() + j*dx, 
                mapBoundingBox.getMaxY()
            );
        }

        for (int i = 0; i <= num_rows; i++) {
            gc.strokeLine(
                mapBoundingBox.getMinX(), 
                mapBoundingBox.getMinY() + i*dy, 
                mapBoundingBox.getMaxX(), 
                mapBoundingBox.getMinY() + i*dy
            );
        }
    }

    public void render(long millisecs) {
        // Draws arena
        //drawArena(millisecs);

        // temporary: sets the alpha value (opacity) based on how many millisecs have passed
        double durata = 8000; // millisec 
        int resto = (int) (millisecs % durata);
        
        double alpha = resto/(durata/2);
        if (resto >= durata/2) {
            alpha = 2 - alpha; 
        }

        gc.setGlobalAlpha(alpha);

        // draws the reference arena
        double referenceScale = width / reference.getWidth();
        
        gc.drawImage(reference, 0, 0, reference.getWidth() * referenceScale, reference.getHeight() * referenceScale);

        gc.setGlobalAlpha(1);

                
    }

    @Override
    public void renderTexture() {
        gc.drawImage(arenaImage, 0, 0, width, height);
    }


    @Override
    public void renderPlayerKingTower(float centreLogicX, float centreLogicY) {
        
        fillPoint(
            logic2GraphicX(centreLogicX), 
            logic2GraphicY(centreLogicY)
        );

        double scale = 0.35;
        gc.drawImage(
            imgPlayerKingTower, 
            logic2GraphicX(centreLogicX) - imgPlayerKingTower.getWidth()*scale/2, 
            logic2GraphicY(centreLogicY) - imgPlayerKingTower.getHeight()*scale/2,
            imgPlayerKingTower.getWidth()*scale,
            imgPlayerKingTower.getHeight()*scale
        );
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
        return (float) mapBoundingBox.getMinX() + logicCoordX*dx;
    }

    private float logic2GraphicY(float logicCoordY) {
        return (float) mapBoundingBox.getMinY() + logicCoordY*dy;
    }

    @Override
    public double getCanvasWidth() {
        return width;
    }

    @Override
    public double getCanvasHeight() {
        return height;
    }

    @Override
    public void resizeCanvas(double newHeight) {
        double aspectRatio = width/height;
        width = aspectRatio * newHeight;
        height = newHeight;
    }
}
