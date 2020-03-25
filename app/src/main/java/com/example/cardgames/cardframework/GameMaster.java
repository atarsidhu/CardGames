package com.example.cardgames.cardframework;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class GameMaster {

    private static final int STARTING_HAND_SIZE = 7;
    private ArrayList<Deck> decks;
    private ArrayList<Player> players;

    private int dealerIndex = 0;

    private Random rand;

    public void setup(int decksToPrepare, int humanPlayers, int aiPlayers) {
        rand = new Random();
        decks = new ArrayList<Deck>();
        players = new ArrayList<Player>();

        // Prepare decks
        for (int i = 0; i < decksToPrepare; i++) {
            decks.add(new Deck());
        }

        // Add human players
        for (int i = 0; i < humanPlayers; i++) {
            players.add(new Player(true));
        }

        // Add AI players
        for (int i = 0; i < aiPlayers; i++) {
            players.add(new Player(false));
        }

        // Sets a random player as the dealer
        dealerIndex = rand.nextInt(players.size());
        Log.i("Go Fish", "Player " + (dealerIndex + 1) + " of " + players.size() + " is the dealer");

        // Deal out the cards, starting with the player after the dealer
        for (int i = 0; i < STARTING_HAND_SIZE; i++) {
            int currentIndex = dealerIndex + 1;
            for (int j = 0; j < players.size(); j++) {
                if (currentIndex >= players.size()) currentIndex = 0;
                Log.i("Go Fish", "Dealing card " + i + " to player " + currentIndex);
                players.get(currentIndex).pickUpCard(decks.get(0).drawTop());
                currentIndex++;
            }
        }

        // Just some debugging info
        for (Player p : players) {
            Log.i("Go Fish", p.toString());
        }
        Log.i("Go Fish", "Deck has " + decks.get(0).size() + " cards remaining");
    }

}