package jroyale.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class View implements IView {
    private GraphicsContext gc;
    private Image img, reference;
    private double width, height;

    public View(Canvas canvas) {
        this.gc = canvas.getGraphicsContext2D();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();

        this.img = new Image(getClass().getResourceAsStream("/jroyale/images/arena.png"));
        this.reference = new Image(getClass().getResourceAsStream("/jroyale/images/reference.jpg"));
    }

    public void render() {
        gc.clearRect(0, 0, width, height);  // clears canvas
        double scale = 0.72;
        int yOffset = -72;
        gc.drawImage(img, (width - img.getWidth()*scale)/2, (height - img.getHeight()*scale)/2 + yOffset, img.getWidth() * scale, img.getHeight() * scale);

        gc.setGlobalAlpha(0.25);

        double referenceScale = width / reference.getWidth();
        gc.drawImage(reference, 0, 0, reference.getWidth() * referenceScale, reference.getHeight() * referenceScale);
        gc.setGlobalAlpha(1);
        gc.fillPolygon(new double[]{0, 40, 40, 0}, new double[]{0, 0, 40, 40}, 4);
    }

}
