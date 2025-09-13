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

                // if it's transparent we continue;
                if (opacity == 0) continue;

                if (x < minX) {
                    minX = x;
                } else if (x > maxX) {
                    maxX = x;
                }
                if (y < minY) {
                    minY = y;
                } else if (y > maxY) {
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
        // TODO
        return null;
    }
}
