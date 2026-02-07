package jroyale.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;

public class DeckView {

    private static final String UI_PATH_RELATIVE_TO_RESOURCE = "/jroyale/images/ui/";
    private static final Image OUTLINE_DEFAULT_SPELL = ImageUtils.cropToBoundingBox(new Image(DeckView.class.getResourceAsStream(UI_PATH_RELATIVE_TO_RESOURCE + "outline_default.png")));
    private static final int NUM_CARDS = 4;
    private static final double NORMALIZED_RECT_HEIGHT = 0.2; 
    private static final double RECT_OPACITY = 0.5; // alpha in [0, 1]
    private static final double NORMALIZED_CARD_WIDTH = 1.0 / (NUM_CARDS + 2); // arbitrary width decision
    private static final double NORMALIZED_CARD_HEIGHT = 0.9; // 90% of height

    private static CardView card1, card2, card3, card4;

    public static void init(GraphicsContext gc) {
        initCardsView(gc);
    }
    
    public static void renderPlayerDeck(GraphicsContext gc, Image icon1, Image icon2, Image icon3, Image icon4) {
        renderBackDeck(gc);
        
        card1.render(gc, icon1, OUTLINE_DEFAULT_SPELL);
        card2.render(gc, icon2, OUTLINE_DEFAULT_SPELL);
        card3.render(gc, icon3, OUTLINE_DEFAULT_SPELL);
        card4.render(gc, /* icon4, */ OUTLINE_DEFAULT_SPELL);
    }


    // private methods

    private static void renderBackDeck(GraphicsContext gc) {
        // black background
        gc.save();
        gc.setGlobalAlpha(RECT_OPACITY); 

        final double CANVAS_WIDTH = gc.getCanvas().getWidth();
        final double CANVAS_HEIGHT = gc.getCanvas().getHeight();

        final double RECT_HEIGHT = NORMALIZED_RECT_HEIGHT * CANVAS_HEIGHT;

        gc.fillRect(0, CANVAS_HEIGHT - RECT_HEIGHT, CANVAS_WIDTH, RECT_HEIGHT);

        // reset alpha
        gc.restore();
    }

    private static void initCardsView(GraphicsContext gc) {
        final double Y_CENTER = gc.getCanvas().getHeight()* NORMALIZED_CARD_HEIGHT; 

        final double WIDTH = gc.getCanvas().getWidth();

        final double X_CENTER_1 = WIDTH / (NUM_CARDS + 1) * 1; // center of first card
        final double X_CENTER_2 = WIDTH / (NUM_CARDS + 1) * 2; // center of second card
        final double X_CENTER_3 = WIDTH / (NUM_CARDS + 1) * 3; // center of third card
        final double X_CENTER_4 = WIDTH / (NUM_CARDS + 1) * 4; // center of fourth card

        // based on arbitrary choice: card width has to be 1/6 of width 
        double cardWidth = gc.getCanvas().getWidth() * NORMALIZED_CARD_WIDTH; 
        // setting the same ratio of OUTLINE_SPELL:
        double cardHeight = cardWidth / OUTLINE_DEFAULT_SPELL.getWidth() * OUTLINE_DEFAULT_SPELL.getHeight();

        card1 = new CardView(X_CENTER_1, Y_CENTER, cardWidth, cardHeight);
        card2 = new CardView(X_CENTER_2, Y_CENTER, cardWidth, cardHeight);
        card3 = new CardView(X_CENTER_3, Y_CENTER, cardWidth, cardHeight);
        card4 = new CardView(X_CENTER_4, Y_CENTER, cardWidth, cardHeight);

    }
}
