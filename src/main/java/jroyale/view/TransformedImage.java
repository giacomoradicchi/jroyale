package jroyale.view;

import javafx.scene.image.Image;

public class TransformedImage {
    private Image img;
    private double scale = 1.0;
    private double shiftX = 0;
    private double shiftY = 0;

    public TransformedImage(Image img) {
        this.img = img;
    }

    public TransformedImage(Image img, double scale, double shiftX, double shiftY) {
        this.img = img;
        this.scale = scale;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    public double getWidth() {
        return img.getWidth() * scale;
    }

    public double getHeight() {
        return img.getHeight() * scale;
    }

    public Image getImage() {
        return img;
    }

    public double getScale() {
        return scale;
    }

    public void scale(double scale) {
        this.scale *= scale;
    }

    public void shiftX(double shiftX) {
        this.shiftX += shiftX;
    }

    public void shiftY(double shiftY) {
        this.shiftY += shiftY;
    }

    public double getShiftX() {
        return shiftX;
    }

    public double getShiftY() {
        return shiftY;
    }
}
