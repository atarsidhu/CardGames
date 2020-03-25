package com.example.cardgames.cardframework;

import java.util.ArrayList;

public class Player {

    boolean isHuman;
    int score = 0;
    ArrayList<Card> hand;

    public Player(boolean setIsHuman) {
        hand = new ArrayList<Card>();
        isHuman = setIsHuman;
    }

    void pickUpCard(Card c) {
        hand.add(c);
    }

    public void assignHand(ArrayList<Card> newHand) {
        hand = newHand;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public int getScore() {
        return score;
    }

    public String toString() {
        String result = "Human: " + isHuman;
        result += "\nHand: ";
        for (int i = 0; i < hand.size(); i++) {
            result += hand.get(i).getStyleId() + " | ";
        }
        return result;
    }

}