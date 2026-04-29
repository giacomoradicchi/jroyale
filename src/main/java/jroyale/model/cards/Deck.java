package jroyale.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jroyale.utils.Enums.Side;

public class Deck {
    
    public static final int AVAILABLE_CARDS_SIZE = 4;

    private static final int MAX_NUM_CARDS = 8;
    private static final int MAX_ELIXIR = 10; 
    // initial amount of elixir when the deck is created
    private static final byte INITIAL_ELIXIR = 3;
    // factor to convert seconds to nanoseconds
    private static final long NANOS_IN_SECOND = 1_000_000_000L;
    // default speed for elixir charging
    private static final double DEFAULT_SPEED = 1.0;
    // range limits for progress clamping
    private static final double MIN_PROGRESS = 0.0;
    private static final double MAX_PROGRESS = 1.0;
    // value used to reset the accumulator
    private static final long RESET_ACCUMULATOR_VALUE = 0;
    // index constants for specific card positions
    private static final int FIRST_CARD_INDEX = 0;
    private static final int SECOND_CARD_INDEX = 1;
    private static final int THIRD_CARD_INDEX = 2;
    private static final int FOURTH_CARD_INDEX = 3;
    // index used to pick a card from the shuffled buffer
    private static final int PICK_FROM_BUFFER_INDEX = 0;

    private final List<Card> cardBuffer = new ArrayList<>(); // to avoid new constructor whenever replaceCard() and initAvailableCards() is called.
    
    private final Card[] availableCards = new Card[AVAILABLE_CARDS_SIZE]; // player/opponent will choose between those cards.
    private Set<Card> deck = new HashSet<>(); // using Set data structure to avoid duplicates.
    private byte elixirLeft = INITIAL_ELIXIR; 
    private int selectedCardIndex;

    private long accumulator; // it will increase for each frame by elapsed
    private static final double DEFAULT_ELISIR_CHARGE_TIME_SEC = 3;
    private static final double DEFAULT_ELISIR_CHARGE_TIME_NANOSEC = DEFAULT_ELISIR_CHARGE_TIME_SEC * NANOS_IN_SECOND;
    private static double chargeTimeSpeed = DEFAULT_SPEED;

    public Deck(Card[] deckCards) {
        int numCards = deckCards.length;
        if (numCards > MAX_NUM_CARDS) {
            throw new IllegalArgumentException("The number of cards on deck cannot be grather than " + MAX_NUM_CARDS + ".");
        }

        if (numCards <= AVAILABLE_CARDS_SIZE) {
            throw new IllegalArgumentException("The number of cards on deck has to be grather than " + AVAILABLE_CARDS_SIZE + ".");
        }

        
        for (Card card : deckCards) {
            // check for null cards.
            if (card == null) throw new IllegalArgumentException("Deck cannot contain null.");
            deck.add(card);
        }

        // final check for eventual duplicates (i.e: numCards > AVAILABLE_CARDS_SIZE, but the array has the same object for each index):
        if (deck.size() <= AVAILABLE_CARDS_SIZE) {
            throw new IllegalArgumentException("The number of cards on deck has to be grather than " + AVAILABLE_CARDS_SIZE + ".");
        }

        // in this way, we're sure that the availableCards array won't have any null value.
        initAvailableCards();
        
    }

    public void update(long elapsed) {
        if (elixirLeft >= MAX_ELIXIR) return;

        accumulator += elapsed;
        
        if (shouldIncrement()) {
            elixirLeft++;
            resetAccumulator();
        }
    }

    private boolean shouldIncrement() {
        return accumulator >= getChargeTimeNanoSec();
    }

    private long getChargeTimeNanoSec() {
        return (long) (DEFAULT_ELISIR_CHARGE_TIME_NANOSEC * chargeTimeSpeed);
    }

    private void resetAccumulator() {
        if (elixirLeft == MAX_ELIXIR) {
            accumulator = RESET_ACCUMULATOR_VALUE;
        } else {
            accumulator -= getChargeTimeNanoSec();
        }
    }

    public byte getElixir() {
        return elixirLeft;
    }

    public double getChargeTimeProgress() {
        return Math.clamp((double) (accumulator) / getChargeTimeNanoSec(), MIN_PROGRESS, MAX_PROGRESS); // between 0 and 1
    }

    public void selectCard(int index) {
        if (index < 0 || index >= AVAILABLE_CARDS_SIZE) throw new IllegalArgumentException("Index value " + index + " invalid.");

        selectedCardIndex = index;
    }

    public void dropSelectedCard(int rowIndex, int columnIndex, Side side) {

        Card selectedCard = availableCards[selectedCardIndex];
        byte elixirCost = selectedCard.getCardStats().getElixirCost();
        // updating elixirLeft
        elixirLeft -= elixirCost;

        // replacing selected card with a random card on the deck which is not inside available card array.
        replaceCard();

        selectedCard.dropCardIntoModel(rowIndex, columnIndex, side);

        // reset selected card
        selectedCard = null;
    }

    public boolean isSelectedCardDroppable() {
        Card selectedCard = availableCards[selectedCardIndex];
        return selectedCard != null && selectedCard.getCardStats().getElixirCost() <= elixirLeft;
    }

    public Card getCurrentFirstCard() {
        return availableCards[FIRST_CARD_INDEX];
    }

    public Card getCurrentSecondCard() {
        return availableCards[SECOND_CARD_INDEX];
    }

    public Card getCurrentThirdCard() {
        return availableCards[THIRD_CARD_INDEX];
    }

    public Card getCurrentFourthCard() {
        return availableCards[FOURTH_CARD_INDEX];
    }

    public Card getCurrentCard(int index) {
        if (index < 0 || index >= MAX_NUM_CARDS) throw new IllegalArgumentException("Index invalid.");

        return availableCards[index];
    }

    private void replaceCard() {
        cardBuffer.clear();
        cardBuffer.addAll(deck);

        for (Card card : availableCards) {
            cardBuffer.remove(card);
        }
        Collections.shuffle(cardBuffer);

        availableCards[selectedCardIndex] = cardBuffer.get(PICK_FROM_BUFFER_INDEX);
    }

    private void initAvailableCards() {
        cardBuffer.clear();
        cardBuffer.addAll(deck);
        Collections.shuffle(cardBuffer);

        for (int i = 0; i < AVAILABLE_CARDS_SIZE; i++) {
            availableCards[i] = cardBuffer.get(i);
        }
    }
    
    // static methods

    public static byte getMaxElixir() {
        return MAX_ELIXIR;
    }

}