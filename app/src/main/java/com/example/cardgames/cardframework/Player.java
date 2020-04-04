package com.example.cardgames.cardframework;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Player {

    private boolean isHuman;
    private int score = 0;
    private ArrayList<Card> hand;

    public Player(boolean setIsHuman) {
        hand = new ArrayList<>();
        isHuman = setIsHuman;
    }

    public ArrayList<Card> getCardsWithRank(int rank) {
        ArrayList<Card> temp = new ArrayList<>();
        for (int i = hand.size() - 1; i >= 0; i--) {
            if (hand.get(i).getRank() == rank) {
                temp.add(hand.get(i));
                hand.remove(i);
            }
        }
        return temp;
    }

    public boolean hasCardsWithRank(int rank) {
        for (int i = hand.size() - 1; i >= 0; i--) {
            if (hand.get(i).getRank() == rank) {
                return true;
            }
        }
        return false;
    }

    public void pickUpCard(Card c) {
        hand.add(c);
    }

    public void addToHand(ArrayList<Card> newCards) {
        hand.addAll(newCards);
    }

    public void assignHand(ArrayList<Card> newHand) {
        hand = newHand;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void incrementScore(int inc) {
        score += inc;
    }

    public int getScore() {
        return score;
    }

    public int getHandSize() {
        return hand.size();
    }

    public boolean hasEmptyHand() {
        return hand.isEmpty();
    }

    public boolean isHuman() {
        return isHuman;
    }

    @Override
    @NonNull
    public String toString() {
        String result = "Human: " + isHuman;
        result += " Hand Size: " + hand.size();
        result += "\nHand: ";
        for (int i = 0; i < hand.size(); i++) {
            result += hand.get(i).getStyleId() + " | ";
        }
        return result;
    }

}