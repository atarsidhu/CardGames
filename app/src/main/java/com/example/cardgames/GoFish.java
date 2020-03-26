package com.example.cardgames;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import com.example.cardgames.cardframework.*;

import java.util.ArrayList;

public class GoFish extends AppCompatActivity {
    // UI Components
    ConstraintLayout mainLayout;
    LinearLayout playerHand;
    LinearLayout aiHand;
    TextView humanScore;
    TextView aiScore;

    // Utilities
    Random rand;

    // Game Properties
    Deck deck;
    Player human;
    Player AI;

    // Constants
    int STARTING_HAND_SIZE = 7;

    private void playGame() {
        Log.i("Go Fish", "Playing Game");
        updateUI();
    }

    private void setupGame() {

        rand = new Random();

        deck = new Deck();

        ArrayList<Card> hand1 = new ArrayList<Card>();
        ArrayList<Card> hand2 = new ArrayList<Card>();

        human = new Player(true);
        AI = new Player(false);

        for (int i = 0; i < STARTING_HAND_SIZE; i++) {
            hand1.add(deck.drawTop());
            hand2.add(deck.drawTop());
        }

        if (rand.nextInt(2) == 0) {
            human.assignHand(hand1);
            AI.assignHand(hand2);
        } else {
            human.assignHand(hand2);
            AI.assignHand(hand1);
        }

        Log.i("Go Fish", "Setup Complete");
        Log.i("Go Fish", human.toString());
        Log.i("Go Fish", AI.toString());
        Log.i("Go Fish", deck.toString());
    }

    private void setupUI() {
        setContentView(R.layout.activity_go_fish);
        mainLayout = findViewById(R.id.go_fish_main_layout);
        playerHand = findViewById(R.id.playerHand);
        aiHand = findViewById(R.id.aiHand);
        humanScore = findViewById(R.id.humanScore);
        aiScore = findViewById(R.id.aiScore);
    }

    private void updateUI() {

        // Player Hand
        for (Card temp : human.getHand()) {
            TextView tempText = new TextView(this);

            // Rank
            tempText.setText(temp.getRankStr());
            if(temp.getRankStr().contains("11"))
                tempText.setText("J");
            else if(temp.getRankStr().contains("12"))
                tempText.setText("Q");
            else if(temp.getRankStr().contains("13"))
                tempText.setText("K");
            else if(temp.getRankStr().contains("14"))
                tempText.setText("A");

            tempText.setTextSize(30);
            tempText.setTextColor(getResources().getColor(R.color.colorPrimary));

            // Suit
            if(temp.getSuit() == 0)
                tempText.setBackgroundResource(R.drawable.card_spade);
            else if(temp.getSuit() == 1)
                tempText.setBackgroundResource(R.drawable.card_heart);
            else if(temp.getSuit() == 2)
                tempText.setBackgroundResource(R.drawable.card_club);
            else
                tempText.setBackgroundResource(R.drawable.card_diamond);

            tempText.setWidth(300);
            playerHand.addView(tempText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }

        // AI Hand
        for (Card temp : AI.getHand()) {
            TextView tempText = new TextView(this);
            tempText.setText(temp.getRankStr());
            if(temp.getSuit() == 0)
                tempText.setBackgroundResource(R.drawable.card_spade);
            else if(temp.getSuit() == 1)
                tempText.setBackgroundResource(R.drawable.card_heart);
            else if(temp.getSuit() == 2)
                tempText.setBackgroundResource(R.drawable.card_club);
            else
                tempText.setBackgroundResource(R.drawable.card_diamond);
            tempText.setWidth(300);
            aiHand.addView(tempText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }

        // Scores
        humanScore.setText(String.valueOf(human.getScore()));
        aiScore.setText(String.valueOf(AI.getScore()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setupGame();
        playGame();
    }
}