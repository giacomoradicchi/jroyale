package jroyale.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jroyale.model.troops.Troop;
import jroyale.shared.Enums.Side;

public class Deck {
    
    private static final int AVAILABLE_CARDS_SIZE = 1; // in general has to be 4, but since we've not enough cards will be using numCardsAvailable -1
    private static final int MAX_NUM_CARDS = 8;
    private static final int MAX_ELIXIR = 10;

    private final List<Card> cardBuffer = new ArrayList<>(); // to avoid new constructor whenever replaceCard() and initAvailableCards() is called.
    
    private final Card[] availableCards = new Card[AVAILABLE_CARDS_SIZE]; // player/opponent will choose between those cards.
    private Set<Card> deck = new HashSet<>(); // using Set data structure to avoid duplicates.
    private byte elixirLeft;
    private int selectedCardIndex;


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

    public void update(long now) {
        // TODO
    }

    public void addElixir() {
        if (elixirLeft < MAX_ELIXIR) {
            elixirLeft++;
        }
    }

    public byte getElixir() {
        return elixirLeft;
    }

    public void selectCard(int index) {
        if (index < 0 || index >= AVAILABLE_CARDS_SIZE) throw new IllegalArgumentException("Index value " + index + " invalid.");

        selectedCardIndex = index;
    }

    public Troop dropSelectedCard(int rowIndex, int columnIndex, Side side) {
        if (!isSelectedCardDroppable()) {
            throw new IllegalStateException("Selected card is not droppable.");
        }

        Card selectedCard = availableCards[selectedCardIndex];
        // updating elixirLeft
        elixirLeft -= selectedCard.getElixirCost();

        // replacing selected card with a random card on the deck which is not inside available card array.
        replaceCard();

        
        return selectedCard.generateNewTroop(rowIndex, columnIndex, side);
    }

    public boolean isSelectedCardDroppable() {
        Card selectedCard = availableCards[selectedCardIndex];
        return selectedCard != null && selectedCard.getElixirCost() <= elixirLeft;
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


}
