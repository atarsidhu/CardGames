package theoatar.cardgames.cardframework;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    ArrayList<Card> cards;

    /**
     * Creates an empty deck.
     */
    public Deck() {
        cards = new ArrayList<Card>();
    }

    /**
     * Creates a deck which contains <code>noOfDecks</code> deck's worth of cards.
     *
     * @param noOfDecks Number of standard decks to include within the deck.
     */
    public Deck(int noOfDecks) {
        cards = new ArrayList<Card>();
        for (int i = 0; i < noOfDecks; i++) {
            generateStandard();
        }
        shuffleDeck();
    }

    public void batchAdd(ArrayList<Card> newCards) {
        cards.addAll(newCards);
    }

    /**
     * Shuffles the deck<br><br><br><br>duh
     */
    public void shuffleDeck() {
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
     * Returns the top card of the deck, and then removes that card from the deck.
     *
     * @return The top card of the deck
     */
    public Card drawTop() {
        Card temp = cards.get(0);
        cards.remove(0);
        return temp;
    }

    /**
     * Generates a set of 52 cards and adds them to the deck
     */
    private void generateStandard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 14; j++) {
                cards.add(new Card(j, i));
            }
        }
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public ArrayList<Card> getAllCards() {
        return cards;
    }

    public String toString() {
        return "Current Size: " + size();
    }

}