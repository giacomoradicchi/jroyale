package jroyale.view;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.ImageUtils;

public class DeckView {

    private static DeckView instance = null;

    private static final String UI_PATH_RELATIVE_TO_RESOURCE = "/jroyale/images/ui/";
    private static final Image OUTLINE_DEFAULT_SPELL = ImageUtils.cropToBoundingBox(new Image(DeckView.class.getResourceAsStream(UI_PATH_RELATIVE_TO_RESOURCE + "outline_default.png")));
    private static final Image DECK_IMAGE = ImageUtils.cropToBoundingBox(new Image(DeckView.class.getResourceAsStream(UI_PATH_RELATIVE_TO_RESOURCE + "deck.png")));
    private static final double DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH = 0.82; // right side of deck is 82% of deck's width (based on experiments)
    private static final int NUM_CARDS = 4;
    private static final double NORMALIZED_DECK_WIDTH = 0.9;
    private static final double NORMALIZED_CARD_WIDTH = 1.0 / (NUM_CARDS + 1); // arbitrary width decision
    private static final double NORMALIZED_CARD_HEIGHT = 0.45; // 45% of deck height

    private CardView card1, card2, card3, card4;

    private DeckView() {} // private methods

    public void init() {
        initCardsView();

        View2.getInstance().addToRoot(card1);
        View2.getInstance().addToRoot(card2);
        View2.getInstance().addToRoot(card3);
        View2.getInstance().addToRoot(card4);
    }
    
    public void renderPlayerDeck(EntityType type1, EntityType type2, EntityType type3, EntityType type4) {
        renderBackDeck();
        
        card1.setType(type1);
        card2.setType(type2);
        card3.setType(type3);
        card4.setType(type4);

        card1.render(EntityViewBinder.getInstance().getViewInstance(type1).getSpellIcon(), OUTLINE_DEFAULT_SPELL);
        card2.render(EntityViewBinder.getInstance().getViewInstance(type2).getSpellIcon(), OUTLINE_DEFAULT_SPELL);
        card3.render(EntityViewBinder.getInstance().getViewInstance(type3).getSpellIcon(), OUTLINE_DEFAULT_SPELL);
        card4.render(EntityViewBinder.getInstance().getViewInstance(type4).getSpellIcon(), OUTLINE_DEFAULT_SPELL);  
    }



    // private methods

    private void renderBackDeck() {

        final double CANVAS_WIDTH = View2.getInstance().getCanvasWidth();
        final double CANVAS_HEIGHT = View2.getInstance().getCanvasHeight();

        final double DECK_WIDTH = NORMALIZED_DECK_WIDTH * CANVAS_WIDTH ;
        final double DECK_HEIGHT = DECK_WIDTH * DECK_IMAGE.getHeight() / DECK_IMAGE.getWidth();

        View2.getInstance().renderScreenImage(
            DECK_IMAGE, 
            CANVAS_WIDTH/2, 
            CANVAS_HEIGHT - DECK_HEIGHT/2, 
            DECK_WIDTH, 
            DECK_HEIGHT
        );

        View2.getInstance().strokeScreenRoundedRect(
            CANVAS_WIDTH/2 + (1.0 - DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH)*DECK_WIDTH/2, 
            CANVAS_HEIGHT - DECK_HEIGHT/2, 
            DECK_WIDTH * DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH, 
            DECK_HEIGHT,
            0, 0, 1, 1, Color.BLACK
        );
    }

    private void initCardsView() {

        final double CANVAS_WIDTH = View2.getInstance().getCanvasWidth();
        final double CANVAS_HEIGHT = View2.getInstance().getCanvasHeight();

        final double DECK_WIDTH = NORMALIZED_DECK_WIDTH * View2.getInstance().getCanvasWidth();
        final double RIGHT_SIDE_DECK_WIDTH = DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH * DECK_WIDTH;

        final double DECK_HEIGHT = DECK_WIDTH * DECK_IMAGE.getHeight() / DECK_IMAGE.getWidth();
        final double Y_CENTER = CANVAS_HEIGHT - (1 - NORMALIZED_CARD_HEIGHT) * DECK_HEIGHT; 

        final double OFFSET_X = CANVAS_WIDTH/2 + (0.5 - DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH) * DECK_WIDTH ; 

        final double X_CENTER_1 = RIGHT_SIDE_DECK_WIDTH / (NUM_CARDS + 2) * (1 + 4.0/3 * 0) + OFFSET_X; // center of first card
        final double X_CENTER_2 = RIGHT_SIDE_DECK_WIDTH / (NUM_CARDS + 2) * (1 + 4.0/3 * 1) + OFFSET_X; // center of second card
        final double X_CENTER_3 = RIGHT_SIDE_DECK_WIDTH / (NUM_CARDS + 2) * (1 + 4.0/3 * 2) + OFFSET_X; // center of third card
        final double X_CENTER_4 = RIGHT_SIDE_DECK_WIDTH / (NUM_CARDS + 2) * (1 + 4.0/3 * 3) + OFFSET_X; // center of fourth card

        // based on arbitrary choice: card width has to be 1/6 of width 
        double cardWidth = RIGHT_SIDE_DECK_WIDTH * NORMALIZED_CARD_WIDTH; 
        // setting the same ratio of OUTLINE_SPELL:
        double cardHeight = cardWidth / OUTLINE_DEFAULT_SPELL.getWidth() * OUTLINE_DEFAULT_SPELL.getHeight();

        card1 = new CardView(X_CENTER_1, Y_CENTER, cardWidth, cardHeight);
        card2 = new CardView(X_CENTER_2, Y_CENTER, cardWidth, cardHeight);
        card3 = new CardView(X_CENTER_3, Y_CENTER, cardWidth, cardHeight);
        card4 = new CardView(X_CENTER_4, Y_CENTER, cardWidth, cardHeight);

    }

    private int getSelectedCardIndex(CardView selectedCard) {
        if (selectedCard == card1) {
            return 0;
        } else if (selectedCard == card2) {
            return 1;
        } else if (selectedCard == card3) {
            return 2;
        } else if (selectedCard == card4) {
            return 3;
        }
        return -1;
    }

    public void setSelectedCard(CardView card) {
        View2.getInstance().setSelectedCard(
            getSelectedCardIndex(card)
        );
    }

    // static methods

    public static DeckView getInstance() {
        if (instance == null) {
            instance = new DeckView();
        }

        return instance;
    }
}
