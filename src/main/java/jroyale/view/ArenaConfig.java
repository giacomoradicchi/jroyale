package jroyale.view;

import java.awt.geom.Rectangle2D;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

class ArenaConfig {
    final static String ARENA_RELATIVE_PATH = "/jroyale/images/arena.png";

    // Coordinates determined empirically through testing
    final static double ARENA_IMG_SCALE = 0.72;
    final static int ARENA_X_OFFSET = 0;
    final static int ARENA_Y_OFFSET = -72;

    private final static String ARENA_TEXTURE_0_RELATIVE_PATH = "arena_texture/0.png";
    private final static String ARENA_TEXTURE_1_RELATIVE_PATH = "arena_texture/1.png";

    private final static boolean GRID_ON = false;

    public class MapConfig{
        // Coordinates determined empirically through testing

        final static double NORMALIZED_TOPLEFT_CORNER_X = 32 / (800 * 607.0 / 1080); 
        final static double NORMALIZED_TOPLEFT_CORNER_Y = 63.0 / 800; 
        final static double NORMALIZED_WIDTH = 1 - 2*NORMALIZED_TOPLEFT_CORNER_X;
        final static double NORMALIZED_HEIGHT = 552.0 / 800;
    }

    public static Image createArenaImage(double width, double height, int rowsCount, int colsCount) {
        Canvas canvas = new Canvas(width, height);

        // getting mapBoundingBox based on given dimensions
        Rectangle2D mapBoundingBox = new Rectangle2D.Double(
            MapConfig.NORMALIZED_TOPLEFT_CORNER_X * width,
            MapConfig.NORMALIZED_TOPLEFT_CORNER_Y * height,
            MapConfig.NORMALIZED_WIDTH * width,
            MapConfig.NORMALIZED_HEIGHT * height
        );

        // Texture atlas attribs (they contain all the texture to be drawn)
        TextureAtlas arena0 = new TextureAtlas(new Image(ArenaConfig.class.getResourceAsStream("/jroyale/images/" + ARENA_TEXTURE_0_RELATIVE_PATH)));
        TextureAtlas arena1 = new TextureAtlas(new Image(ArenaConfig.class.getResourceAsStream("/jroyale/images/" + ARENA_TEXTURE_1_RELATIVE_PATH)));

        // setting all bounds of subTextures inside each textureAtlas 
        arena0.addSubTexture(0, 0, 800, 520);
        arena0.addSubTexture(0, 520, 800, 507);
        arena0.addSubTexture(800, 0, arena0.getFullImage().getWidth() - 800, 800);

        arena1.addSubTexture(0, 0, 800, 60);
        arena1.addSubTexture(0, 60, 800, 60);

        // every texture is drawn in the canvas

        //
        // draws subtexture that fits inside the map rect
        // 

        // to adjust each subTexture position (due to misaligned images), is it possible
        // to work on marginX value (remove a lateral margin from the drawing of the texture)
        // and offsetY value (shift the texture up or down based on offset)
        // BEST ADJUSTMENT :
        final double NORMALIZED_MARGIN_X = 5.0 / (800 * 607.0 / 1080);
        final double NORMALIZED_OFFSET_Y_0 = -7.0 / 800;
        final double NORMALIZED_OFFSET_Y_1 = 2.0 / 800;
        final double NORMALIZED_OFFSET_Y_2 = -5.0 / 800;

        // upper part
        
        double margin = NORMALIZED_MARGIN_X * width;
        drawSubTextureInsideMapBounds(
            canvas, arena1, mapBoundingBox, 
            1, 
            0, 
            margin, 
            NORMALIZED_OFFSET_Y_0 * height, 
            rowsCount, colsCount
        );
        drawSubTextureInsideMapBounds(
            canvas, arena0, mapBoundingBox, 
            0, 
            1, 
            margin, 
            NORMALIZED_OFFSET_Y_1 * height,
            rowsCount, colsCount);
        
        // bottom part

        drawSubTextureInsideMapBounds(
            canvas, arena0, mapBoundingBox, 
            1, 
            17, 
            margin, 
            0, 
            rowsCount, colsCount);

        drawSubTextureInsideMapBounds(
            canvas, arena1, mapBoundingBox, 
            0, 
            31, 
            margin, 
            NORMALIZED_OFFSET_Y_2 * height, 
            rowsCount, colsCount);

        //drawSubTextureInsideMapBounds(canvas, arena0, mapBoundingBox, 2, 15, rowsCount, colsCount);

        if (GRID_ON) {
            drawGrid(mapBoundingBox, colsCount, rowsCount, canvas.getGraphicsContext2D());
        }

        // Takes a snapshot of the canvas and returns a new WritableImage that can 
        // be drawn by the View class.
        return canvas.snapshot(null, null);
    }

    private static void drawGrid(Rectangle2D mapBoundingBox, int colsCount, int rowsCount, GraphicsContext gc) {
        double dx = mapBoundingBox.getWidth() / colsCount;
        double dy = mapBoundingBox.getHeight() / rowsCount; 

        gc.setGlobalAlpha(0.25);
        gc.setLineWidth(1);

        for (int j = 0; j <= colsCount; j++) {
            gc.strokeLine(
                mapBoundingBox.getMinX() + j*dx, 
                mapBoundingBox.getMinY(), 
                mapBoundingBox.getMinX() + j*dx, 
                mapBoundingBox.getMaxY()
            );
        }

        for (int i = 0; i <= rowsCount; i++) {
            gc.strokeLine(
                mapBoundingBox.getMinX(), 
                mapBoundingBox.getMinY() + i*dy, 
                mapBoundingBox.getMaxX(), 
                mapBoundingBox.getMinY() + i*dy
            );
        } 
    }

    private static void drawSubTextureInsideMapBounds(Canvas canvas, TextureAtlas texAtl, Rectangle2D mapBoundingBox, int subTextureIndex, int j, double marginX, double offsetY, int rowsCount, int colsCount) {
        double totalWidth = mapBoundingBox.getWidth() - 2*marginX;
        double scale = totalWidth / texAtl.getWidth(subTextureIndex);
        double dy = mapBoundingBox.getHeight() / rowsCount; 

        drawSubTexture(
            canvas.getGraphicsContext2D(),
            texAtl, subTextureIndex, 
            mapBoundingBox.getX() + marginX,  
            mapBoundingBox.getY() + j * dy + offsetY, // setting y position based on row j
            totalWidth, // fixing width to mapWidth
            texAtl.getHeight(subTextureIndex) * scale // just to mantain proportion
        );
    }

    private static void drawSubTexture(GraphicsContext gc, TextureAtlas texAtl, int subTextureIndex, double x, double y, double w, double h) {
        double[] bounds = texAtl.getSubTexture(subTextureIndex);

        gc.drawImage(
            texAtl.getFullImage(), 
            bounds[0],
            bounds[1],
            bounds[2],
            bounds[3],
            x, y, w, h
        );
    }

}
