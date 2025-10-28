package jroyale.view;

import jroyale.shared.Side;
import jroyale.shared.TowerIndex;
import jroyale.utils.ImageUtils;

import java.awt.geom.Rectangle2D;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class View implements IView {

    private final static boolean DEBUG_MODE = false;

    private GraphicsContext gc;
    private double width, height;
    private double dx, dy;

    // images attribs
    private Image reference;
    private Image imgPlayerKingTower;

    // images path
    private final static String PLAYER_KING_TOWER_RELATIVE_PATH = "towers/player/king_tower.png";

    // Entities attribs
    private Arena arena;
    private static final int NUM_TOWERS = 6;
    private Tower[] towers = new Tower[NUM_TOWERS];
    
    private Rectangle2D mapBoundingBox;

    // scale of the entire scene
    private double scale = 1.0;

    public View(Canvas canvas, int rowsCount, int colsCount) {
        this.gc = canvas.getGraphicsContext2D();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        
        this.arena = new Arena(width, height, scale, rowsCount, colsCount);
        this.mapBoundingBox = arena.getMapBounds();
        updateDxDy();

        // initializing towers
        towers[TowerIndex.PLAYER_KING_TOWER] = new KingTower(Side.PLAYER);
        towers[TowerIndex.PLAYER_LEFT_TOWER] = new ArcherTower(Side.PLAYER);
        towers[TowerIndex.PLAYER_RIGHT_TOWER] = new ArcherTower(Side.PLAYER);

        towers[TowerIndex.OPPONENT_KING_TOWER] = new KingTower(Side.OPPONENT);
        towers[TowerIndex.OPPONENT_LEFT_TOWER] = new ArcherTower(Side.OPPONENT);
        towers[TowerIndex.OPPONENT_RIGHT_TOWER] = new ArcherTower(Side.OPPONENT);
        
        this.reference = new Image(getClass().getResourceAsStream("/jroyale/images/reference2.png"));
        this.imgPlayerKingTower = new Image(getClass().getResourceAsStream("/jroyale/images/" + PLAYER_KING_TOWER_RELATIVE_PATH));
        

        // adjusting tower image
        this.imgPlayerKingTower = ImageUtils.enhanceOpacity(imgPlayerKingTower);
    }

    

    @Override
    public void initializeRendering(long millisec, double newWidth, double newHeight) {
        // clears canvas
        gc.clearRect(0, 0, width, height);  

        // set opacity to 100%
        gc.setGlobalAlpha(1);

        // update width and height:
        width = newWidth;
        height = newHeight;

        //scale = 1 - millisec/10000.0;

        // update arena and dx dy
        arena.update(width, height, scale);
        updateDxDy();
        mapBoundingBox = arena.getMapBounds();
    }

    private void updateDxDy() {
        dx = arena.getDx();
        dy = arena.getDy();
    }

    @Override
    public void renderArena() {
        arena.renderArena(gc, width, height, scale, DEBUG_MODE);
    }

    public void renderCells(boolean[][] cells) {
        arena.renderCells(gc, cells);
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
    public void renderTower(int towerType, double centreX, double centreY) {
        if (towerType < 0 || towerType >= NUM_TOWERS) {
            throw new IllegalArgumentException("Invalid towerType: " + towerType);
        }
        towers[towerType].drawTower(gc, centreX, centreY, scale);
        if (!DEBUG_MODE) return;

        // debug mode
        fillPoint(
            centreX, 
            centreY
        );
    }

    @Override
    public void renderTroop(double centreX, double centreY, int side) {
        Color color = Color.BLUE;
        if (side == Side.OPPONENT) {
            color = Color.RED;
        }
        fillPoint(
            centreX, 
            centreY,
            color
        );
    }

    @Override
    public void renderDragPlacementPreview(double centreX, double centreY) {
        gc.save();

        // fill
        double alpha = 0.5;
        gc.setGlobalAlpha(alpha);
        gc.setFill(Color.TEAL);
        gc.fillRoundRect(centreX - dx/2, centreY - dy/2, dx, dy, dx/2, dy/2);

        // outline
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);
        gc.setGlobalAlpha(1);
        gc.strokeRoundRect(centreX - dx/2, centreY - dy/2, dx, dy, dx/2, dy/2);

        gc.restore();
    }

    private void fillPoint(double centreX, double centreY) {
        int defaultSize = 10;
        fillPoint(centreX, centreY, defaultSize, Color.BLACK);
    }

    private void fillPoint(double centreX, double centreY, Color color) {
        int defaultSize = 10;
        fillPoint(centreX, centreY, defaultSize, color);
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

    @Override
    public double getMapTopLeftCornerX() {
        return mapBoundingBox.getMinX();
    }

    @Override
    public double getMapTopLeftCornerY() {
        return mapBoundingBox.getMinY();
    }

    @Override
    public double getMapWidth() {
        return mapBoundingBox.getWidth();
    }

    @Override
    public double getMapHeight() {
        return mapBoundingBox.getHeight();
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
    public void setCanvasWidth(double newWidth) {
        /* double aspectRatio = width/height;
        width = aspectRatio * newHeight; */
        width = newWidth;
    }

    @Override
    public void setCanvasHeight(double newHeight) {
        /* double aspectRatio = width/height;
        width = aspectRatio * newHeight; */
        height = newHeight;
    }

    @Override
    public void renderPoint(double centreX, double centreY) {
        double size = 16;
        gc.fillOval(centreX - size/2, centreY - size/2, size, size);
    }
}
