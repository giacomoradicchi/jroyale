package jroyale.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;

public class DeckView {

    private static final String UI_PATH_RELATIVE_TO_RESOURCE = "/jroyale/images/ui/";
    private static final Image OUTLINE_DEFAULT_SPELL = ImageUtils.cropToBoundingBox(new Image(DeckView.class.getResourceAsStream(UI_PATH_RELATIVE_TO_RESOURCE + "outline_default.png")));
    
    public static void renderPlayerDeck(GraphicsContext gc, Image icon1, double globalScale) {
        gc.setGlobalAlpha(0.5);
        gc.fillRoundRect(0, gc.getCanvas().getHeight() - 150, gc.getCanvas().getWidth(), 150, 0, 0);
        gc.setGlobalAlpha(1);

        double xCenter = gc.getCanvas().getWidth()/2;
        double yCenter = gc.getCanvas().getHeight()/2;
        double fixedWidth = OUTLINE_DEFAULT_SPELL.getWidth() * 0.5 * globalScale;
        double fixedHeight = OUTLINE_DEFAULT_SPELL.getHeight() * 0.5 * globalScale;

        gc.drawImage(icon1, xCenter - fixedWidth/2, yCenter - fixedHeight/2, fixedWidth, fixedHeight);
        gc.drawImage(OUTLINE_DEFAULT_SPELL, xCenter - fixedWidth/2, yCenter - fixedHeight/2, fixedWidth, fixedHeight);
        
    }
}
