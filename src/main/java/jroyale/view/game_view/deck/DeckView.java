package jroyale.view.game_view.deck;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import jroyale.utils.Enums.EntityType;
import jroyale.view.FontManager;
import jroyale.view.game_view.GameView;
import jroyale.view.game_view.IGameView;
import jroyale.controller.binders.EntityViewBinder;
import jroyale.utils.ImageUtils;

public class DeckView {

    private static DeckView instance = null;

    private CardView[] cards;

    private int selectedCardIndex = -1;

    private static final String UI_PATH_RELATIVE_TO_RESOURCE = "/jroyale/images/ui/";
    private static final Image OUTLINE_DEFAULT_SPELL = ImageUtils.cropToBoundingBox(new Image(DeckView.class.getResourceAsStream(UI_PATH_RELATIVE_TO_RESOURCE + "outline_default.png")));
    private static final Image DECK_IMAGE = ImageUtils.cropToBoundingBox(new Image(DeckView.class.getResourceAsStream(UI_PATH_RELATIVE_TO_RESOURCE + "deck.png")));
    private static final Image ELIXIR_DROP_IMAGE = ImageUtils.cropToBoundingBox(new Image(DeckView.class.getResourceAsStream(UI_PATH_RELATIVE_TO_RESOURCE + "elixir_drop.png")));
    private static final double DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH = 0.82; // right side of deck is 82% of deck's width (based on experiments)
    private static final int NUM_CARDS = 4;
    private static final double NORMALIZED_DECK_WIDTH = 0.9;
    private static final double NORMALIZED_CARD_WIDTH = 1.0 / (NUM_CARDS + 1); // arbitrary width decision
    private static final double NORMALIZED_CARD_HEIGHT = 0.45; // 45% of deck height

    private static final Color PLAYER_STROKE_COLOR = Color.rgb(250, 199, 250);
    private static final Color PLAYER_FILL_DARK = Color.rgb(81, 30, 81); 
    private static final Color PLAYER_FILL_PROGRESS = Color.rgb(161, 60, 161); 
    private static final Color PLAYER_FILL_LIGHT = Color.rgb(255, 106, 255);

    private DeckView() {} // private methods

    public void init(int numCards) {
        if (numCards < 0) throw new IllegalArgumentException("Invalid parameter numCards: must be positive.\n");

        cards = new CardView[numCards];

        initCardsView();

        for (int i = 0; i < numCards; i++) {
            GameView.getInstance().addToRoot(cards[i]);
        }
        
    }
    
    public void renderPlayerDeck(EntityType type1, byte elixirCost1, EntityType type2, byte elixirCost2, EntityType type3, byte elixirCost3, EntityType type4, byte elixirCost4, byte elixirLeft, double elixirChargeTimeProgress, byte maxElixir, double alpha) {
        renderBackDeck(alpha);
        
        cards[0].setType(type1).setElixirCost(elixirCost1);
        cards[1].setType(type2).setElixirCost(elixirCost2);
        cards[2].setType(type3).setElixirCost(elixirCost3);
        cards[3].setType(type4).setElixirCost(elixirCost4);

        renderElixirBar(elixirLeft, elixirChargeTimeProgress, maxElixir, alpha);

        // first render non selected card 
        for (int i = 0; i < cards.length; i++) {
            if (i != selectedCardIndex) {
                cards[i].render(EntityViewBinder.getInstance().getViewInstance(cards[i].getType()).getSpellIcon(), OUTLINE_DEFAULT_SPELL, alpha);

                
                double dropWidth = cards[0].getWidth() * 0.35;
                double dropHeight = ELIXIR_DROP_IMAGE.getHeight() * dropWidth / ELIXIR_DROP_IMAGE.getWidth();
                double dropCenterX = cards[i].getLayoutX() + cards[i].getWidth()/2;
                double dropCenterY = cards[i].getLayoutY() + cards[i].getHeight() - dropHeight/2;

                renderElixirDrop(dropCenterX, dropCenterY, dropWidth, dropHeight, cards[i].getElixirCost(), alpha);

                

            }
        }

        // then render selected card (has to be on top of the others)
        if (selectedCardIndex != -1 && cards[selectedCardIndex].isVisible()) {

            cards[selectedCardIndex].render(EntityViewBinder.getInstance().getViewInstance(cards[selectedCardIndex].getType()).getSpellIcon(), OUTLINE_DEFAULT_SPELL, alpha);

            double dropWidth = cards[0].getWidth() * 0.35;
            double dropHeight = ELIXIR_DROP_IMAGE.getHeight() * dropWidth / ELIXIR_DROP_IMAGE.getWidth();
            double dropCenterX = cards[selectedCardIndex].getLayoutX() + cards[selectedCardIndex].getTranslateX() + cards[selectedCardIndex].getWidth()/2;
            double dropCenterY = cards[selectedCardIndex].getLayoutY() + cards[selectedCardIndex].getTranslateY() + cards[selectedCardIndex].getHeight() - dropHeight/2;

            renderElixirDrop(dropCenterX, dropCenterY, dropWidth, dropHeight, cards[selectedCardIndex].getElixirCost(), alpha);

            
        }

        
    }

    private void renderElixirDrop(double dropCenterX, double dropCenterY, double dropWidth, double dropHeight, byte elixir, double alpha) {
        IGameView view = GameView.getInstance();

        view.renderScreenImage(
            ELIXIR_DROP_IMAGE, 
            dropCenterX, 
            dropCenterY, 
            dropWidth, 
            dropHeight, 
            alpha
        ); 

        // render counter elixir
        view.fillScreenTextFromCenter(
            String.valueOf(elixir), 
            dropCenterX, 
            dropCenterY, 
            FontManager.getInstance().getRegularFont(dropWidth * 0.6), 
            Color.WHITE, 
            alpha
        );

        view.strokeScreenTextFromCenter(
            String.valueOf(elixir), 
            dropCenterX, 
            dropCenterY, 
            FontManager.getInstance().getRegularFont(dropWidth * 0.6), 
            Color.BLACK,
            1,
            alpha
        );
    }


    private void renderElixirBar(byte elixirLeft, double elixirChargeTimeProgress, byte maxElixir, double alpha) {

        IGameView view = GameView.getInstance();
        // elixir
        final double CANVAS_WIDTH = view.getCanvasWidth();
        final double CANVAS_HEIGHT = view.getCanvasHeight();

        final double DECK_WIDTH = NORMALIZED_DECK_WIDTH * CANVAS_WIDTH ;
        final double DECK_HEIGHT = DECK_WIDTH * DECK_IMAGE.getHeight() / DECK_IMAGE.getWidth();

        final double centerX = CANVAS_WIDTH/2 + (1.0 - DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH)*DECK_WIDTH/2;
        final double centerY = CANVAS_HEIGHT - DECK_HEIGHT * 0.15;
        final double barWidth = DECK_WIDTH * DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH * 0.85;
        final double barHeight = DECK_HEIGHT * 0.10;

        
        Color cFill, cStroke;
        Color cFillDark;
        cStroke   = PLAYER_STROKE_COLOR;
        cFill     = PLAYER_FILL_LIGHT;
        cFillDark = PLAYER_FILL_DARK;

        // Draw the empty (background) health bar
        view.fillScreenRoundedRect(centerX, centerY, barWidth, barHeight,
                barHeight / 2, barHeight / 2, alpha, cFillDark);

        // Calculate the filled portion width based on current health percentage
        double percentage   = (double) elixirLeft / maxElixir;
        double elixirWidth  = barWidth * percentage;

        // Align the filled bar to the left edge of the background bar
        double ElixirBarCenterX = centerX - barWidth / 2 + elixirWidth / 2;
        
        double dx = 1.0 / maxElixir * barWidth;
        double elixirProgressWidth = dx * elixirChargeTimeProgress;
        view.fillScreenRoundedRect(centerX - barWidth / 2 + elixirWidth + elixirProgressWidth/2, centerY, elixirProgressWidth, barHeight, 0, 0, alpha, PLAYER_FILL_PROGRESS);

        // Draw the filled (current elixir) portion of the bar
        view.fillScreenRoundedRect(ElixirBarCenterX, centerY, elixirWidth, barHeight,
                barHeight / 2, barHeight / 2, alpha, cFill);

        // Draw the border around the full elixir bar
        view.strokeScreenRoundedRect(centerX, centerY, barWidth, barHeight,
                barHeight / 2, barHeight / 2, 1.5, alpha, cStroke);


        for (int i = 1; i < maxElixir; i++) {
            double x = centerX - barWidth / 2 + i * dx;
            view.strokeScreenLine(
                x, 
                centerY - barHeight/2, 
                x, 
                centerY + barHeight/2, 
                alpha, 
                cStroke, 
                1.5
            );
        }

        // render drop elixir 
        double dropWidth = barHeight * 2.5;
        double dropHeight = ELIXIR_DROP_IMAGE.getHeight() * dropWidth / ELIXIR_DROP_IMAGE.getWidth();
        double dropCenterX = centerX - barWidth/2;
        double dropCenterY = centerY;

        renderElixirDrop(dropCenterX, dropCenterY, dropWidth, dropHeight, elixirLeft, alpha);
    }

    private void renderBackDeck(double alpha) {

        IGameView view = GameView.getInstance();

        final double CANVAS_WIDTH = view.getCanvasWidth();
        final double CANVAS_HEIGHT = view.getCanvasHeight();

        final double DECK_WIDTH = NORMALIZED_DECK_WIDTH * CANVAS_WIDTH ;
        final double DECK_HEIGHT = DECK_WIDTH * DECK_IMAGE.getHeight() / DECK_IMAGE.getWidth();

        view.renderScreenImage(
            DECK_IMAGE, 
            CANVAS_WIDTH/2, 
            CANVAS_HEIGHT - DECK_HEIGHT/2, 
            DECK_WIDTH, 
            DECK_HEIGHT,
            alpha
        );
    }

    private void initCardsView() {

        IGameView view = GameView.getInstance();

        final double CANVAS_WIDTH = view.getCanvasWidth();
        final double CANVAS_HEIGHT = view.getCanvasHeight();

        final double DECK_WIDTH = NORMALIZED_DECK_WIDTH * view.getCanvasWidth();
        final double RIGHT_SIDE_DECK_WIDTH = DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH * DECK_WIDTH;

        final double DECK_HEIGHT = DECK_WIDTH * DECK_IMAGE.getHeight() / DECK_IMAGE.getWidth();
        final double Y_CENTER = CANVAS_HEIGHT - (1 - NORMALIZED_CARD_HEIGHT) * DECK_HEIGHT; 

        final double OFFSET_X = CANVAS_WIDTH/2 + (0.5 - DECKS_RIGHT_SIDE_PERCENTAGE_WIDTH) * DECK_WIDTH ; 

        final double X_CENTERS[] = new double[cards.length];

        for (int i = 0; i < cards.length; i++) {
            X_CENTERS[i] = RIGHT_SIDE_DECK_WIDTH / (NUM_CARDS + 2) * (1 + 4.0/3 * i) + OFFSET_X;
        }

        // based on arbitrary choice: card width has to be 1/6 of width 
        double cardWidth = RIGHT_SIDE_DECK_WIDTH * NORMALIZED_CARD_WIDTH; 
        // setting the same ratio of OUTLINE_SPELL:
        double cardHeight = cardWidth / OUTLINE_DEFAULT_SPELL.getWidth() * OUTLINE_DEFAULT_SPELL.getHeight();

        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CardView(X_CENTERS[i], Y_CENTER, cardWidth, cardHeight);
        }
    }

    private int getSelectedCardIndex(CardView selectedCard) {
        for (int i = 0; i < cards.length; i++) {
            if (selectedCard == cards[i]) return i;
        }
        
        return -1; // not found
    }

    public void setSelectedCard(CardView card) {
        selectedCardIndex = getSelectedCardIndex(card);

        GameView.getInstance().setSelectedCard(
            selectedCardIndex
        );
    } 

    public void setVisibleSelectedCard(boolean value) {
        if (selectedCardIndex != -1)
            cards[selectedCardIndex].setVisible(value);
    }

    // static methods

    public static DeckView getInstance() {
        if (instance == null) {
            instance = new DeckView();
        }

        return instance;
    }
}
