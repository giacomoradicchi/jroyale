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

    private final List<Card> cardBuffer = new ArrayList<>(); // to avoid new constructor whenever replaceCard() and initAvailableCards() is called.
    
    private final Card[] availableCards = new Card[AVAILABLE_CARDS_SIZE]; // player/opponent will choose between those cards.
    private Set<Card> deck = new HashSet<>(); // using Set data structure to avoid duplicates.
    private byte elixirLeft = 0; 
    private int selectedCardIndex;

    private long accumulator; // it will increase for each frame by elapsed
    private static final double DEFAULT_ELISIR_CHARGE_TIME_SEC = 3;
    private static final double DEFAULT_ELISIR_CHARGE_TIME_NANOSEC = DEFAULT_ELISIR_CHARGE_TIME_SEC * 1_000_000_000L;
    private static double chargeTimeSpeed = 1.0;

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
        accumulator -= getChargeTimeNanoSec();
    }

    public byte getElixir() {
        return elixirLeft;
    }

    public double getChargeTimeProgress() {
        return Math.clamp((double) (accumulator) / getChargeTimeNanoSec(), 0, 1); // between 0 and 1
    }

    public void selectCard(int index) {
        if (index < 0 || index >= AVAILABLE_CARDS_SIZE) throw new IllegalArgumentException("Index value " + index + " invalid.");

        selectedCardIndex = index;
    }

    public void dropSelectedCard(int rowIndex, int columnIndex, Side side) {

        Card selectedCard = availableCards[selectedCardIndex];
        // updating elixirLeft
        elixirLeft -= selectedCard.getElixirCost();

        // replacing selected card with a random card on the deck which is not inside available card array.
        replaceCard();

        selectedCard.dropCardIntoModel(rowIndex, columnIndex, side);

        // reset selected card
        selectedCard = null;
    }

    public boolean isSelectedCardDroppable() {
        Card selectedCard = availableCards[selectedCardIndex];
        return selectedCard != null && selectedCard.getElixirCost() <= elixirLeft;
    }

    public Card getCurrentFirstCard() {
        return availableCards[0];
    }

    public Card getCurrentSecondCard() {
        return availableCards[1];
    }

    public Card getCurrentThirdCard() {
        return availableCards[2];
    }

    public Card getCurrentFourthCard() {
        return availableCards[3];
    }

    private void replaceCard() {
        cardBuffer.clear();
        cardBuffer.addAll(deck);

        for (Card card : availableCards) {
            cardBuffer.remove(card);
        }
        Collections.shuffle(cardBuffer);

        availableCards[selectedCardIndex] = cardBuffer.get(0);
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
