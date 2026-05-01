package jroyale.view.game_view.arena;

import java.awt.geom.Rectangle2D;
import javafx.scene.image.Image;
import jroyale.view.IMainGUI;
import jroyale.view.game_view.IGameView;

public class ArenaView {

    private static ArenaView instance = null;

    private final static String ARENA_RELATIVE_PATH = "/jroyale/images/arenas/IMG_6164.PNG";

    // base reference values for normalization
    private final static double REF_CANVAS_WIDTH = 449.6296296296296;
    private final static double REF_CANVAS_HEIGHT = 800.0;
    private final static double REF_IMG_WIDTH = 1080.0;
    
    // empirical arena config values
    private final static double BASE_SCALE = 0.417; 
    private final static double MAP_PADDING_OFFSET = 32.0;
    private final static double CENTER_DIVISOR = 2.0;
    private final static double ARENA_Y_OFFSET_FACTOR = 108.0;
    private final static double FULL_OPACITY = 1.0;

    // normalized map constants
    private final static double NORMALIZED_MAP_WIDTH = 1.0 - 2.0 * (MAP_PADDING_OFFSET / (REF_CANVAS_HEIGHT * 607.0 / REF_IMG_WIDTH)); 
    private final static double NORMALIZED_MAP_HEIGHT = 552.0 / REF_CANVAS_HEIGHT;
    private final static double NORMALIZED_SHIFT_Y = -72.0 / REF_CANVAS_HEIGHT;

    private Image arenaImage;
    private Rectangle2D mapBoundingBox;
    private double globalShiftY;
    private double dx, dy;
    private int rows, cols;

    private final IGameView gameView = IGameView.getInstance();

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
        IMainGUI gui = gameView.getGUI();

        double canvasWidth = gui.getCanvasWidth();
        double canvasHeight = gui.getCanvasHeight();

        double mapWidth = NORMALIZED_MAP_WIDTH * canvasWidth;
        double mapHeight = NORMALIZED_MAP_HEIGHT * canvasHeight;
        globalShiftY = NORMALIZED_SHIFT_Y * canvasHeight; // map is not centered vertically

        mapBoundingBox = new Rectangle2D.Double(
            (canvasWidth - mapWidth) / CENTER_DIVISOR,
            (canvasHeight - mapHeight) / CENTER_DIVISOR + globalShiftY,
            mapWidth,
            mapHeight
        );
    }

    private void calculateDxDy() {
        this.dx = mapBoundingBox.getWidth() / cols;
        this.dy = mapBoundingBox.getHeight() / rows;
    }

    public void renderArena() {
        renderArena(false);
    } 

    public void renderArena(boolean debugMode) {
        IMainGUI gui = gameView.getGUI();

        double canvasWidth = gui.getCanvasWidth();
        double canvasHeight = gui.getCanvasHeight();

        // calculate dynamic scaling based on current canvas size compared to reference
        double widthScale = canvasWidth / REF_CANVAS_WIDTH;
        double heightScale = canvasHeight / REF_CANVAS_HEIGHT;
        double verticalOffset = ARENA_Y_OFFSET_FACTOR * heightScale;

        gui.renderWorldImage(
            arenaImage, 
            canvasWidth / CENTER_DIVISOR, 
            (canvasHeight / CENTER_DIVISOR) - verticalOffset, 
            getWidth() * BASE_SCALE * widthScale, 
            getHeight() * BASE_SCALE * heightScale,
            false,
            FULL_OPACITY
        );  

        if (debugMode) {
            System.out.println("Debug -> Canvas size: " + canvasWidth + "x" + canvasHeight);
        }
    } 

    public Rectangle2D getMapBounds() {
        return mapBoundingBox;
    }

    // static methods
    public static ArenaView getInstance() {
        if (instance == null) {
            instance = new ArenaView();
        }
        return instance;
    }
}