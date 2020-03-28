package com.example.cardgames;

import android.content.res.Resources;
import android.view.View;

import android.widget.Button;
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
    View uiCompletedPairs;
    TextView humanScore;
    TextView aiScore;
    TextView humanCardsInHand;
    TextView aiCardsInHand;
    View btnAce;
    View btnTwo;
    View btnThree;
    View btnFour;
    View btnFive;
    View btnSix;
    View btnSeven;
    View btnEight;
    View btnNine;
    View btnTen;
    View btnJack;
    View btnQueen;
    View btnKing;

    // Utilities
    Random rand;

    // Game Properties
    Deck deck;
    Deck completedPairs;
    Player human;
    Player AI;
    boolean currentlyPlayerTurn;

    // Constants
    int STARTING_HAND_SIZE = 7;

    private void playRound() {

        if (deck.isEmpty() || AI.hasEmptyHand() || human.hasEmptyHand()) {
            //endGame();
        }

        Log.i("Go Fish", "Playing Round");
        updateUI();

        // AI's Turn
        if (!currentlyPlayerTurn) {
            // rankSelector.setVisibility(View.GONE);
            askPlayerForRank(AI, human, rand.nextInt(12) + 1);
        }

        // Player's Turn
        // Just make the rank selector visible and wait for player input
        else {
            rankSelector.setVisibility(View.VISIBLE);
        }
    }

    public void handleRankSelectionFromPlayer(View v) {
        String id = getResources().getResourceEntryName(v.getId());
        int rank;
        switch (id) {
            case "button_aces":
                rank = 1;
                break;
            case "button_twos":
                rank = 2;
                break;
            case "button_threes":
                rank = 3;
                break;
            case "button_fours":
                rank = 4;
                break;
            case "button_fives":
                rank = 5;
                break;
            case "button_sixes":
                rank = 6;
                break;
            case "button_sevens":
                rank = 7;
                break;
            case "button_eights":
                rank = 8;
                break;
            case "button_nines":
                rank = 9;
                break;
            case "button_tens":
                rank = 10;
                break;
            case "button_jacks":
                rank = 11;
                break;
            case "button_queens":
                rank = 12;
                break;
            case "button_kings":
                rank = 13;
                break;
            default:
                rank = 0;
                break;
        }
        askPlayerForRank(human, AI, rank);
    }

    private void askPlayerForRank(Player asker, Player asked, int rank) {
        String askerType = (asker.isHuman()) ? "human" : "nonhuman";
        String askedType = (asked.isHuman()) ? "human" : "nonhuman";
        Log.i("Go Fish", "A " + askerType + " has asked for " + rank + "s from a " + askedType);

        ArrayList<Card> returnedCards = asked.getCardsWithRank(rank);
        if (returnedCards.isEmpty()) {
            currentlyPlayerTurn = !currentlyPlayerTurn;
            asker.pickUpCard(deck.drawTop());
            Log.i("Go Fish", "Go Fish!");
        } else {
            asker.addToHand(returnedCards);
            Log.i("Go Fish", "Cards of rank " + rank + " found!");
        }
        completedPairs.batchAdd(scorePoints(asker));
        playRound();
    }

    private ArrayList<Card> scorePoints(Player p) {
        ArrayList<Card> newCompletedPairs = new ArrayList<Card>();
        ArrayList<Card> tempHand = p.getHand();
        boolean changesMade = true;
        while (changesMade) {
            changesMade = false;
            for (int i = 0; i < tempHand.size(); i++) {
                for (int j = i + 1; j < tempHand.size(); j++) {
                    if (tempHand.get(j).getRank() == tempHand.get(i).getRank()) {
                        p.incrementScore(1);
                        newCompletedPairs.add(tempHand.get(j));
                        newCompletedPairs.add(tempHand.get(i));
                        Log.i("Go Fish", "Removing " + tempHand.get(i).getStyleId() + " and " + tempHand.get(j).getStyleId());
                        tempHand.remove(j);
                        tempHand.remove(i);
                        changesMade = true;
                    }
                }
            }
        }
        p.assignHand(tempHand);
        return newCompletedPairs;
    }

    private void setupGame() {

        rand = new Random();

        deck = new Deck(1);
        completedPairs = new Deck();

        ArrayList<Card> hand1 = new ArrayList<Card>();
        ArrayList<Card> hand2 = new ArrayList<Card>();

        human = new Player(true);
        AI = new Player(false);

        // Generates two starting hands
        for (int i = 0; i < STARTING_HAND_SIZE; i++) {
            hand1.add(deck.drawTop());
            hand2.add(deck.drawTop());
        }

        // Randomly chooses either the AI or the Human as the dealer.
        // Non-dealer receives the deck containing the first-drawn card and plays first.
        if (rand.nextInt(2) == 0) {
            currentlyPlayerTurn = true;
            human.assignHand(hand1);
            AI.assignHand(hand2);
        } else {
            currentlyPlayerTurn = false;
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
        uiCompletedPairs = findViewById(R.id.completedPairs);
        humanScore = findViewById(R.id.humanScore);
        aiScore = findViewById(R.id.aiScore);
        humanCardsInHand = findViewById(R.id.playerCardsInHand);
        aiCardsInHand = findViewById(R.id.aiCardsInHand);
        btnAce = findViewById(R.id.button_aces);
        btnTwo = findViewById(R.id.button_twos);
        btnThree = findViewById(R.id.button_threes);
        btnFour = findViewById(R.id.button_fours);
        btnFive = findViewById(R.id.button_fives);
        btnSix = findViewById(R.id.button_sixes);
        btnSeven = findViewById(R.id.button_sevens);
        btnEight = findViewById(R.id.button_eights);
        btnNine = findViewById(R.id.button_nines);
        btnTen = findViewById(R.id.button_tens);
        btnJack = findViewById(R.id.button_jacks);
        btnQueen = findViewById(R.id.button_queens);
        btnKing = findViewById(R.id.button_kings);
    }

    private void updateUI() {

        playerHand.removeAllViews();
        aiHand.removeAllViews();

        // Player Hand
        for (final Card temp : human.getHand()) {
            final TextView tempText = new TextView(this);
            tempText.setTag(temp.getRank());

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

            // Show/Hide "Do you have any" buttons based on ranks in hand
            if(temp.getRankStr().contains(btnAce.getTag().toString())) btnAce.setVisibility(View.VISIBLE);
            if(temp.getRankStr().contains(btnTwo.getTag().toString())) btnTwo.setVisibility(View.VISIBLE);
            if(temp.getRankStr().contains(btnThree.getTag().toString())) btnThree.setVisibility(View.VISIBLE);
            if(temp.getRankStr().contains(btnFour.getTag().toString())) btnFour.setVisibility(View.VISIBLE);
            if(temp.getRankStr().contains(btnFive.getTag().toString())) btnFive.setVisibility(View.VISIBLE);
            if(temp.getRankStr().contains(btnSix.getTag().toString())) btnSix.setVisibility(View.VISIBLE);
            if(temp.getRankStr().contains(btnSeven.getTag().toString())) btnSeven.setVisibility(View.VISIBLE);
            if(temp.getRankStr().contains(btnEight.getTag().toString())) btnEight.setVisibility(View.VISIBLE);
            if(temp.getRankStr().contains(btnNine.getTag().toString())) btnNine.setVisibility(View.VISIBLE);
            if(temp.getRankStr().contains(btnTen.getTag().toString())) btnTen.setVisibility(View.VISIBLE);
            if(temp.getRankStr().contains(btnJack.getTag().toString())) btnJack.setVisibility(View.VISIBLE);
            if(temp.getRankStr().contains(btnQueen.getTag().toString())) btnQueen.setVisibility(View.VISIBLE);
            if(temp.getRankStr().contains(btnKing.getTag().toString())) btnKing.setVisibility(View.VISIBLE);
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

        // Cards in Hand
        humanCardsInHand.setText("Cards in hand: " + human.getHandSize());
        aiCardsInHand.setText("Cards in hand: " + AI.getHandSize());
    }

    public void hideRankSelector(View v) {
        if (uiCompletedPairs.getVisibility() == View.GONE) {
            uiCompletedPairs.setVisibility(View.VISIBLE);
            rankSelector.setVisibility(View.GONE);
        } else {
            uiCompletedPairs.setVisibility(View.GONE);
            rankSelector.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setupGame();
        playRound();
    }
}