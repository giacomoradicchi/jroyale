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
    private final static int BOTTOM_MARGIN_Y = 687 + ARENA_Y_OFFSET;

    public View(Canvas canvas) {
        this.gc = canvas.getGraphicsContext2D();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();

        this.img_arena = new Image(getClass().getResourceAsStream("/jroyale/images/" + ARENA_RELATIVE_PATH));
        this.reference = new Image(getClass().getResourceAsStream("/jroyale/images/reference.jpg"));
    }

    public void renderCells(boolean[][] cells) {
        int num_cols = cells[0].length;
        int num_rows = cells.length;
        double dx = (double) Math.abs(width - 2*MARGIN_X)/num_cols;
        double dy = (double) Math.abs(BOTTOM_MARGIN_Y - TOP_MARGIN_Y)/num_rows;

        renderGrid(num_rows, num_cols, dx, dy);

        // drawing only reachable tiles:
        gc.setFill(Color.GREEN);
        gc.setGlobalAlpha(0.25);
        for (int i = 0; i < num_rows; i++) {
            for (int j = 0; j < num_cols; j++) {
                if (cells[i][j]) {
                    gc.fillRect(MARGIN_X + j*dx, TOP_MARGIN_Y + i*dy, dx, dy);
                }
            }
        }
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

        gc.setGlobalAlpha(alpha*0);

        // draws the reference arena
        double referenceScale = width / reference.getWidth();
        
        gc.drawImage(reference, 0, 0, reference.getWidth() * referenceScale, reference.getHeight() * referenceScale);
        gc.setGlobalAlpha(1);
    }

    private void drawArena() {
        gc.setGlobalAlpha(1);
        gc.drawImage(
            img_arena, 
            (width - img_arena.getWidth() * ARENA_IMG_SCALE)/2, 
            (height - img_arena.getHeight() * ARENA_IMG_SCALE)/2 + ARENA_Y_OFFSET, 
            img_arena.getWidth() * ARENA_IMG_SCALE, 
            img_arena.getHeight() * ARENA_IMG_SCALE
        );
    }

}
