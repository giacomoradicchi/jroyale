package jroyale.view;

import jroyale.shared.Enums.Side;
import jroyale.shared.Enums.State;
import jroyale.shared.TowerIndex;
import jroyale.utils.ImageUtils;

import jroyale.view.troops.GiantView;
import jroyale.view.troops.MiniPekkaView;
import jroyale.view.troops.TroopView;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class View implements IView {

    private static View istance;

    // public attribs for controller class
    public enum TroopType {
        MINI_PEKKA(MiniPekkaView.getInstance()),
        GIANT(GiantView.getInstance());

        private final TroopView troopView;

        TroopType(TroopView troopView) {
            this.troopView = troopView;
        }

        public int getId() {
            return this.ordinal();
        }

        public TroopView getViewIstance() {
            return troopView;
        }
    };

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
    private Tower[] towers = new Tower[TowerIndex.NUM_TOWERS];

    // scale of the entire scene
    private double globalScale = 1.0;

    // The timestamp of the current frame given in nanoseconds
    private long now;

    private View(Canvas canvas, int rowsCount, int colsCount) {
        this.gc = canvas.getGraphicsContext2D();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        
        this.arena = new Arena(width, height, globalScale, rowsCount, colsCount);
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

    public static IView getIstance(Canvas canvas, int rowsCount, int colsCount) {
        if (istance == null) {
            istance = new View(canvas, rowsCount, colsCount);
        }
        return istance;
    }

    
    
    @Override
    public void initializeRendering(long now, double newWidth, double newHeight) {
        // clears canvas
        gc.clearRect(0, 0, width, height);  

        // set opacity to 100%
        gc.setGlobalAlpha(1);

        // update width and height:
        width = newWidth;
        height = newHeight;
        this.now = now;

        //scale -= 0.001;

        // update arena and dx dy
        arena.update(width, height, globalScale);
        updateDxDy();
    }

    @Override
    public void loadSprites() {
        // forcing loading of all troop sprites
        TroopType.values();
    }

    private void updateDxDy() {
        dx = arena.getDx();
        dy = arena.getDy();
    }

    @Override
    public void renderArena() {
        arena.renderArena(gc, width, height, globalScale, DEBUG_MODE);
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
    public void renderTower(int towerType, double centreX, double centreY, int currentHealth, int maxHealth, Side side) {
        if (towerType < 0 || towerType >= TowerIndex.NUM_TOWERS) {
            throw new IllegalArgumentException("Invalid towerType: " + towerType);
        }
        towers[towerType].drawTower(gc, centreX, centreY, globalScale);

        if (currentHealth < maxHealth) {
            renderHealth(gc, centreX, centreY, currentHealth, maxHealth, side);
        }
        


        if (!DEBUG_MODE) return;

        // debug mode
        fillPoint(
            centreX, 
            centreY
        );

        
    }

    @Override
    public void renderTroop(double centreX, double centreY, double angleDirection, TroopType type, int currentFrame, State state, Side side) {

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

        type.troopView.render(gc, centreX, centreY, angleDirection, currentFrame, state, side, globalScale);
        
    }

    private void renderHealth(GraphicsContext gc, double centreX, double centreY, int currentHealth, int maxHealth, Side side) {
        double rectWidth = 0.1 * width * globalScale;
        double rectHeight = 0.01 * height * globalScale;
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
    public void renderDragPlacementPreview(double centreX, double centreY) {
        DragPlacementPreview.render(gc, centreX, centreY, dx, dy, globalScale, now);
    }

    @Override
    public void resetDragPlacementPreviewAnimation() {
        DragPlacementPreview.resetAnimation();
    }

    @Override
    public void renderPlayerDeck(TroopType card1) {
        DeckView.renderPlayerDeck(gc, card1.troopView.getSpellIcon(), globalScale);
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
        return arena.getMapBounds().getMinX();
    }

    @Override
    public double getMapTopLeftCornerY() {
        return arena.getMapBounds().getMinY();
    }

    @Override
    public double getMapWidth() {
        return arena.getMapBounds().getWidth();
    }

    @Override
    public double getMapHeight() {
        return arena.getMapBounds().getHeight();
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

    @Override
    public void renderOval(double centreX, double centreY, double width, double height, double opacity) {
        gc.save();
        gc.setGlobalAlpha(opacity);
        gc.setFill(Color.RED);
        gc.fillOval(centreX - width/2, centreY - height/2, width, height);
        gc.restore();
    }

    @Override
    public void renderVector(double startX, double startY, double angle) {
        final double LINE_LENGTH = dx;
        final double LINE_WIDTH = 2;
        gc.save();
        gc.setFill(Color.BLACK);
        gc.setLineWidth(LINE_WIDTH * globalScale);
        
        gc.strokeLine(
            startX, 
            startY, 
            startX + LINE_LENGTH * globalScale * Math.cos(angle), 
            startY + LINE_LENGTH * globalScale * Math.sin(angle)
        );
        gc.restore();
    }
}
