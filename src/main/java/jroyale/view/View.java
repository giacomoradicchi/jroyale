package jroyale.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class View implements IView {
    private GraphicsContext gc;
    private Image img_arena, reference;
    private double width, height;

    private final static String ARENA_RELATIVE_PATH = "arena.png";
    
    // Coordinates determined empirically through testing
    private final static double ARENA_IMG_SCALE = 0.72;
    private final static int ARENA_Y_OFFSET = -72;

    private final static int MARGIN_X = 32; 
    private final static int TOP_MARGIN_Y = 135 + ARENA_Y_OFFSET;
    private final static int BOTTOM_MARGIN_Y = 685 + ARENA_Y_OFFSET;

    public View(Canvas canvas) {
        this.gc = canvas.getGraphicsContext2D();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();

        this.img_arena = new Image(getClass().getResourceAsStream("/jroyale/images/" + ARENA_RELATIVE_PATH));
        this.reference = new Image(getClass().getResourceAsStream("/jroyale/images/reference.jpg"));
    }

    public void render(long millisecs) {
        // clears canvas
        gc.clearRect(0, 0, width, height);  

        // Draws arena
        drawArena();

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

        // for debug: draws poligon
        // gc.fillPolygon(new double[]{0, 40, 40, 0}, new double[]{0, 0, 40, 40}, 4);

        gc.setGlobalAlpha(0.25);
        gc.setFill(Color.RED);
        gc.fillPolygon(new double[]{MARGIN_X, width - MARGIN_X, width - MARGIN_X, MARGIN_X}, new double[]{TOP_MARGIN_Y, TOP_MARGIN_Y, BOTTOM_MARGIN_Y, BOTTOM_MARGIN_Y}, 4);
    }

    private void drawArena() {
        gc.drawImage(
            img_arena, 
            (width - img_arena.getWidth() * ARENA_IMG_SCALE)/2, 
            (height - img_arena.getHeight() * ARENA_IMG_SCALE)/2 + ARENA_Y_OFFSET, 
            img_arena.getWidth() * ARENA_IMG_SCALE, 
            img_arena.getHeight() * ARENA_IMG_SCALE
        );
    }

}
