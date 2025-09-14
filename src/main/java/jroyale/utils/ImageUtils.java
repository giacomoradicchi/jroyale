package jroyale.utils;

import java.awt.geom.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageUtils {
    /**
     * Computes bounding box of visible pixels (alpha channel is not 0)
     * @param img original image (RGBA)
     * @return bounding box of the image
     */
    public static Rectangle2D getAlphaBoundingBox(Image img) {
        // default opacityTolerance is 0 
        return getAlphaBoundingBox(img, 0); 
    }

    /**
     * Computes bounding box of pixels whose opacity levels are higher than a certain value
     * @param img original image (RGBA)
     * @param opacityTolerance value between 0.0 - 1.0, pixels whose alpha level is <= opacityTolerance will be skipped.
     * @return bounding box of the image
     */
    public static Rectangle2D getAlphaBoundingBox(Image img, double opacityTolerance) {
        PixelReader reader = img.getPixelReader();
        int width = (int) img.getWidth();
        int height = (int) img.getHeight();

        int minX = width - 1;
        int minY = height - 1;
        int maxX = 0;
        int maxY = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double opacity = reader.getColor(x, y).getOpacity();

                // if it's transparent we continue
                if (opacity <= opacityTolerance) continue;

                if (x < minX) {
                    minX = x;
                } 
                if (x > maxX) {
                    maxX = x;
                }
                if (y < minY) {
                    minY = y;
                } 
                if (y > maxY) {
                    maxY = y;
                }
            }
        }

        return new Rectangle2D.Double(
            minX, 
            minY, 
            maxX - minX, 
            maxY - minY
        );
    }

    /**
     * Removes alpha channel from an image, making it all opaque.
     * @param img original image (RGBA)
     * @return new image RGB opaque
     */
    public static Image makeOpaque(Image img) {
        int width = (int) img.getWidth();
        int height = (int) img.getHeight();
        WritableImage rgbImage = new WritableImage(width, height);

        PixelReader reader = img.getPixelReader();
        PixelWriter writer = rgbImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = reader.getColor(x, y);
                Color opaque = new Color(c.getRed(), c.getGreen(), c.getBlue(), 1.0); // alpha = 1
                writer.setColor(x, y, opaque);
            }
        }

        return rgbImage;
    }

    public static Image enhanceOpacity(Image img) {
        int width = (int) img.getWidth();
        int height = (int) img.getHeight();
        WritableImage rgbImage = new WritableImage(width, height);

        PixelReader reader = img.getPixelReader();
        PixelWriter writer = rgbImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = reader.getColor(x, y);
                double opacity = (c.getBrightness() < 0.1) ? c.getOpacity() : 1.0;
                Color opaque = new Color(c.getRed(), c.getGreen(), c.getBlue(), opacity); // alpha = 1
                writer.setColor(x, y, opaque);
            }
        }

        return rgbImage;
    }

    public static Image clipToBoundingBox(Image img) {
        return clipToBoundingBox(img, 0);
    }

    public static Image clipToBoundingBox(Image img, double opacityTolerance) {
        Rectangle2D boundingBox = getAlphaBoundingBox(img, opacityTolerance);
        int width = (int) boundingBox.getWidth();
        int height = (int) boundingBox.getHeight();
        WritableImage croppedImage = new WritableImage(width, height);
        
        PixelReader reader = img.getPixelReader();
        PixelWriter writer = croppedImage.getPixelWriter();

        int x0 = (int) boundingBox.getMinX();
        int y0 = (int) boundingBox.getMinY();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = reader.getColor(x0 + x, y0 + y);
                writer.setColor(x, y, c);
            }
        }
        return croppedImage;
    }
}
