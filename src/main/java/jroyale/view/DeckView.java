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

    private static final Color PLAYER_STROKE_COLOR = Color.rgb(250, 199, 250);
    private static final Color PLAYER_FILL_DARK = Color.rgb(81, 30, 81); 
    private static final Color PLAYER_FILL_PROGRESS = Color.rgb(161, 60, 161); 
    private static final Color PLAYER_FILL_LIGHT = Color.rgb(255, 106, 255);

    private DeckView() {} // private methods

    public void init() {
        initCardsView();

        View.getInstance().addToRoot(card1);
        View.getInstance().addToRoot(card2);
        View.getInstance().addToRoot(card3);
        View.getInstance().addToRoot(card4);
    }
    
    public void renderPlayerDeck(EntityType type1, EntityType type2, EntityType type3, EntityType type4, byte elixirLeft, double elixirChargeTimeProgress, byte maxElixir) {
        renderBackDeck();
        
        card1.setType(type1);
        card2.setType(type2);
        card3.setType(type3);
        card4.setType(type4);

        card1.render(EntityViewBinder.getInstance().getViewInstance(card1.getType()).getSpellIcon(), OUTLINE_DEFAULT_SPELL);
        card2.render(EntityViewBinder.getInstance().getViewInstance(card2.getType()).getSpellIcon(), OUTLINE_DEFAULT_SPELL);
        card3.render(EntityViewBinder.getInstance().getViewInstance(card3.getType()).getSpellIcon(), OUTLINE_DEFAULT_SPELL);
        card4.render(EntityViewBinder.getInstance().getViewInstance(card4.getType()).getSpellIcon(), OUTLINE_DEFAULT_SPELL);  

        renderElixirBar(elixirLeft, elixirChargeTimeProgress, maxElixir);
        
    }


    private void renderElixirBar(byte elixirLeft, double elixirChargeTimeProgress, byte maxElixir) {
        // elixir
        final double CANVAS_WIDTH = View.getInstance().getCanvasWidth();
        final double CANVAS_HEIGHT = View.getInstance().getCanvasHeight();

        final double DECK_WIDTH = NORMALIZED_DECK_WIDTH * CANVAS_WIDTH ;
        final double DECK_HEIGHT = DECK_WIDTH * DECK_IMAGE.getHeight() / DECK_IMAGE.getWidth();

        final double centerX = CANVAS_WIDTH/2 + (1.0 - DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH)*DECK_WIDTH/2;
        final double centerY = CANVAS_HEIGHT - DECK_HEIGHT * 0.15;
        final double barWidth = DECK_WIDTH * DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH * 0.9;
        final double barHeight = DECK_HEIGHT * 0.10;

        IView view = View.getInstance();

        /* View.getInstance().strokeScreenRoundedRect(
            centerX, 
            centerY,
            DECK_WIDTH * DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH * 0.9, 
            DECK_HEIGHT * 0.2,
            0, 0, 1, 1, Color.BLACK
        ); */

        Color cFill, cStroke;
        Color cFillDark;
        cStroke   = PLAYER_STROKE_COLOR;
        cFill     = PLAYER_FILL_LIGHT;
        cFillDark = PLAYER_FILL_DARK;

        // Draw the empty (background) health bar
        view.fillScreenRoundedRect(centerX, centerY, barWidth, barHeight,
                barHeight / 2, barHeight / 2, 1, cFillDark);

        // Calculate the filled portion width based on current health percentage
        double percentage   = (double) elixirLeft / maxElixir;
        double elixirWidth  = barWidth * percentage;

        // Align the filled bar to the left edge of the background bar
        double ElixirBarCenterX = centerX - barWidth / 2 + elixirWidth / 2;
        
        double dx = 1.0 / maxElixir * barWidth;
        double elixirProgressWidth = dx * elixirChargeTimeProgress;
        view.fillScreenRoundedRect(centerX - barWidth / 2 + elixirWidth + elixirProgressWidth/2, centerY, elixirProgressWidth, barHeight, 0, 0, 1, PLAYER_FILL_PROGRESS);

        // Draw the filled (current elixir) portion of the bar
        view.fillScreenRoundedRect(ElixirBarCenterX, centerY, elixirWidth, barHeight,
                barHeight / 2, barHeight / 2, 1, cFill);

        // Draw the border around the full elixir bar
        view.strokeScreenRoundedRect(centerX, centerY, barWidth, barHeight,
                barHeight / 2, barHeight / 2, 1.5, 1, cStroke);


        for (int i = 1; i < maxElixir; i++) {
            double x = centerX - barWidth / 2 + i * dx;
            view.strokeScreenLine(
                x, 
                centerY - barHeight/2, 
                x, 
                centerY + barHeight/2, 
                1, 
                cStroke, 
                1.5
            );
        }
    }

    private void renderBackDeck() {

        final double CANVAS_WIDTH = View.getInstance().getCanvasWidth();
        final double CANVAS_HEIGHT = View.getInstance().getCanvasHeight();

        final double DECK_WIDTH = NORMALIZED_DECK_WIDTH * CANVAS_WIDTH ;
        final double DECK_HEIGHT = DECK_WIDTH * DECK_IMAGE.getHeight() / DECK_IMAGE.getWidth();

        View.getInstance().renderScreenImage(
            DECK_IMAGE, 
            CANVAS_WIDTH/2, 
            CANVAS_HEIGHT - DECK_HEIGHT/2, 
            DECK_WIDTH, 
            DECK_HEIGHT
        );

        /* View.getInstance().strokeScreenRoundedRect(
            CANVAS_WIDTH/2 + (1.0 - DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH)*DECK_WIDTH/2, 
            CANVAS_HEIGHT - DECK_HEIGHT/2, 
            DECK_WIDTH * DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH, 
            DECK_HEIGHT,
            0, 0, 1, 1, Color.BLACK
        ); */
    }

    private void initCardsView() {

        final double CANVAS_WIDTH = View.getInstance().getCanvasWidth();
        final double CANVAS_HEIGHT = View.getInstance().getCanvasHeight();

        final double DECK_WIDTH = NORMALIZED_DECK_WIDTH * View.getInstance().getCanvasWidth();
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
        View.getInstance().setSelectedCard(
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
