package theoatar.cardgames.cardframework;

import android.util.Log;

/**
 * A single playing card.<br>
 * Ranks are represented 1-13.<br>
 * Suits are represented as either 0, 1, 2, or 3.<br>
 * The <code>value</code> attribute is a numerical representation of both rank and suit.<br>
 * Rank is multiplied by 16 and then added to the suit value.
 */
public class Card {

    private int rank;
    private int suit;
    private int value;

    /**
     * Create a new card, and specify its Rank and Suit
     *
     * @param newRank: The rank of the new card
     * @param newSuit: The Suit of the new card
     */
    public Card(int newRank, int newSuit) {
        rank = newRank;
        suit = newSuit;
        if (newRank > 10) {
            value = 10;
        } else {
            value = newRank;
        }
        Log.println(Log.INFO, "Card.java", "Card Created as " + getValue());
    }

    public void setRank(int newRank) {
        if (newRank >= 2 && newRank <= 14) rank = newRank;
    }

    public int getRank() {
        return rank;
    }

    public void setSuit(int newSuit) {
        if (newSuit >= 0 && newSuit < 4) suit = newSuit;
    }

    public int getSuit() {
        return suit;
    }

    public int getValue() {
        return (16 * rank) + suit;
    }

    public String getStyleId() {
        return rank + "_of_" + suit;
    }

    public String getRankStr() { return String.valueOf(rank); };
}