package com.example.cardgames;

import android.view.View;
import android.widget.Toast;

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
    LinearLayout rankSelector;
    View completedPairs;
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
    boolean anySelected = false;
    boolean spadeSelected = false;
    boolean heartSelected = false;
    boolean clubSelected = false;
    boolean diamondSelected = false;

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
        rankSelector = findViewById(R.id.rankSelector);
        completedPairs = findViewById(R.id.completedPairs);
        humanScore = findViewById(R.id.humanScore);
        aiScore = findViewById(R.id.aiScore);
    }

    private void updateUI() {
        // Player Hand
        for (final Card temp : human.getHand()) {
            final TextView tempText = new TextView(this);
            //tempText.setTag(temp.getRank());

            // Rank
            tempText.setText(temp.getRankStr());
            if (temp.getRankStr().contains("11"))
                tempText.setText("J");
            else if (temp.getRankStr().contains("12"))
                tempText.setText("Q");
            else if (temp.getRankStr().contains("13"))
                tempText.setText("K");
            else if (temp.getRankStr().contains("14"))
                tempText.setText("A");

            tempText.setTextSize(30);
            tempText.setTextColor(getResources().getColor(R.color.colorPrimary));

            // Suit
            if (temp.getSuit() == 0)
                tempText.setBackgroundResource(R.drawable.card_spade);
            else if (temp.getSuit() == 1)
                tempText.setBackgroundResource(R.drawable.card_heart);
            else if (temp.getSuit() == 2)
                tempText.setBackgroundResource(R.drawable.card_club);
            else
                tempText.setBackgroundResource(R.drawable.card_diamond);

            tempText.setWidth(300);
            playerHand.addView(tempText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

            // Tap a card to "select" it. Changes selected card's trim to orange.
            // When a card is selected, other cards cannot be selected. To select a different card, tap the selected
            // card to de-select, then tap another card to select.
            // Problem: When a different card with the same suit as the selected card is tapped, any card can start to be selected.
            tempText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (temp.getSuit()) {
                        case 0:
                            if (!spadeSelected && !heartSelected && !clubSelected && !diamondSelected) {
                                tempText.setBackgroundResource(R.drawable.card_spade_selected);
                                spadeSelected = true;
                            } else {
                                tempText.setBackgroundResource(R.drawable.card_spade);
                                spadeSelected = false;
                            }
                            break;
                        case 1:
                            if (!spadeSelected && !heartSelected && !clubSelected && !diamondSelected) {
                                tempText.setBackgroundResource(R.drawable.card_heart_selected);
                                heartSelected = true;
                            } else {
                                tempText.setBackgroundResource(R.drawable.card_heart);
                                heartSelected = false;
                            }
                            break;
                        case 2:
                            if (!spadeSelected && !heartSelected && !clubSelected && !diamondSelected) {
                                tempText.setBackgroundResource(R.drawable.card_club_selected);
                                clubSelected = true;
                            } else {
                                tempText.setBackgroundResource(R.drawable.card_club);
                                clubSelected = false;
                            }
                            break;
                        case 3:
                            if (!spadeSelected && !heartSelected && !clubSelected && !diamondSelected) {
                                tempText.setBackgroundResource(R.drawable.card_diamond_selected);
                                diamondSelected = true;
                            } else {
                                tempText.setBackgroundResource(R.drawable.card_diamond);
                                diamondSelected = false;
                            }
                            break;
                    }
                }
            });
        }

        // AI Hand
        for (Card temp : AI.getHand()) {
            TextView tempText = new TextView(this);
            //tempText.setText(temp.getStyleId());
            tempText.setText(temp.getRankStr());
            tempText.setBackgroundResource(R.drawable.card_back);
            tempText.setWidth(300);
            aiHand.addView(tempText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }

        // Scores
        humanScore.setText(String.valueOf(human.getScore()));
        aiScore.setText(String.valueOf(AI.getScore()));
    }

    public void hideRankSelector(View v) {
        if (completedPairs.getVisibility() == View.GONE) {
            completedPairs.setVisibility(View.VISIBLE);
        } else {
            completedPairs.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setupGame();
        playGame();
    }
}