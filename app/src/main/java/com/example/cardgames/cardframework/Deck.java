package com.example.cardgames.cardframework;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>();
        populate();
    }

    // Shuffles the deck of cards randomly
    public void shuffle() {
        ArrayList<Card> tempDeck = new ArrayList<Card>();
        Random rand = new Random();

        while (!cards.isEmpty()) {
            int index = rand.nextInt(cards.size());
            tempDeck.add(cards.get(index));
            cards.remove(index);
        }

        cards = tempDeck;

    }

    /**
     * Generates a set of 52 cards, then shuffles the deck.
     */
    private void populate() {
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 14; j++) {
                cards.add(new Card(j, i));
            }
        }
        shuffle();
    }

}
