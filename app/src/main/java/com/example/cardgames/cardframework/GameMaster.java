package com.example.cardgames.cardframework;

import java.util.ArrayList;

public class GameMaster {

    Player human;
    Player AI;
    ArrayList<Deck> decks;

    public GameMaster() {
        human = new Player();
        AI = new Player();
        decks = new ArrayList<Deck>();
        decks.add(new Deck());
    }

}