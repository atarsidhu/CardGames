package com.example.cardgames;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import com.example.cardgames.cardframework.*;

import java.util.ArrayList;

public class GoFish extends AppCompatActivity {
    // UI Components
    private LinearLayout playerHand;
    private LinearLayout aiHand;
    private LinearLayout rankSelector;
    private TextView humanScore;
    private TextView aiScore;
    private TextView cardsLeftInDeck;
    private TextView humanCardsInHand;
    private TextView aiCardsInHand;
    private TextView centreText;
    private TextView textViewThinking;
    private Button yesBtn;
    private Button noBtn;

    // Utilities
    private Random rand;

    // Game Properties
    private Deck deck;
    private Deck completedPairs;
    private Player human;
    private Player AI;
    private boolean currentlyPlayerTurn;
    private boolean lockPlayer = true;
    private boolean easyDifficulty = true;
    private boolean matchFoundFromGoFish = true;

    // Constants
    private int STARTING_HAND_SIZE = 7;
    private long POPUP_DISPLAY_DURATION = 1500;

    private void playRound() throws InterruptedException {

        updateUI();

        Log.i("Go Fish", "=======================================================\nPlaying Round");
        Log.i("Go Fish", human.toString());
        Log.i("Go Fish", AI.toString());

        // Game Over Condition
        if ((deck.isEmpty() && AI.getHand().isEmpty()) || (deck.isEmpty() && human.getHand().isEmpty())) {
            lockPlayer = true;
            rankSelector.setVisibility(View.GONE);

            String gameOverMessage = (AI.getScore() > human.getScore())
                    ? "Game Over!\nYou Lost " + AI.getScore() + " - " + human.getScore() + "\nPlay Again?"
                    : "Game Over!\nYou Won "  + human.getScore() + " - " + AI.getScore() + "\nPlay Again?";
            if(AI.getScore() == human.getScore())
                gameOverMessage = "Game Over!\nTie Game " + AI.getScore() + " - " + human.getScore() + "\nPlay Again?";

            centreText.setText(gameOverMessage);
            centreText.setVisibility(View.VISIBLE);
            humanScore.setVisibility(View.GONE);
            aiScore.setVisibility(View.GONE);
            aiCardsInHand.setVisibility(View.GONE);
            humanCardsInHand.setVisibility(View.GONE);
            playerHand.setVisibility(View.GONE);
            aiHand.setVisibility(View.GONE);
            yesBtn.setVisibility(View.VISIBLE);
            noBtn.setVisibility(View.VISIBLE);
            cardsLeftInDeck.setVisibility(View.GONE);
            Log.i("Go Fish", "Game Over!");

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });

            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startIntent = new Intent(getApplicationContext(), GoFishMaster.class);
                    startActivity(startIntent);
                }
            });
            return;
        }

        // AI's Turn
        if (!currentlyPlayerTurn) {

            rankSelector.setVisibility(View.GONE);

            if (easyDifficulty) {
                askPlayerForRank(AI, human, rand.nextInt(12) + 1);
            } else {
                ArrayList<Integer> notCompleted = new ArrayList<>();
                ArrayList<Integer> partiallyCompleted = new ArrayList<>();

                // Generates lists of integers to guess from, based on how many completed pairs of a certain rank
                // have been made so far.
                for (int i = 1; i < 14; i++) {
                    if (!AI.hasCardsWithRank(i)) continue;
                    int countOfRank = 0;
                    for (Card temp : deck.getAllCards()) {
                        if (temp.getRank() == i) {
                            countOfRank++;
                        }
                    }
                    if (countOfRank == 2) {
                        partiallyCompleted.add(i);
                    } else {
                        notCompleted.add(i);
                    }
                }

                // Now, make a guess based on the generated lists
                if (!notCompleted.isEmpty()) {
                    askPlayerForRank(AI, human, notCompleted.get(rand.nextInt(notCompleted.size())));
                } else if (!partiallyCompleted.isEmpty()) {
                    askPlayerForRank(AI, human, partiallyCompleted.get(rand.nextInt(partiallyCompleted.size())));
                } else {
                    askPlayerForRank(AI, human, AI.getHand().get(rand.nextInt(AI.getHandSize())).getRank());
                }
            }
        }

        // Player's Turn
        // Just make the rank selector visible and wait for player input
        else {
            lockPlayer = false;
            displayTurn(human);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rankSelector.setVisibility(View.VISIBLE);
                    centreText.setVisibility(View.GONE);
                }
            }, 2*POPUP_DISPLAY_DURATION);

            updateUI();
        }
    }

    public void handleRankSelectionFromPlayer2(View v) throws InterruptedException {
        if (lockPlayer) return;
        lockPlayer = true;
        int rank = v.getId();
        askPlayerForRank(human, AI, rank);
    }

    private void askPlayerForRank(final Player asker, final Player asked, final int rank) {
        String askerType = (asker.isHuman()) ? "human" : "nonhuman";
        String askedType = (asked.isHuman()) ? "human" : "nonhuman";
        Log.i("Go Fish", "A " + askerType + " has asked for " + rank + "s from a " + askedType);

        final boolean originalCurrentlyPlayerTurn = currentlyPlayerTurn;

        final ArrayList<Card> returnedCards = asked.getCardsWithRank(rank);
        if (returnedCards.isEmpty()) {
            currentlyPlayerTurn = !currentlyPlayerTurn;
            Card cardToPickUp = deck.drawTop();
            Log.i("Go Fish", "Go Fish!");
            Log.i("Go Fish", ((asker.isHuman()) ? "Human " : "Nonhuman ") + "Picking up " + cardToPickUp.getStyleId());
            asker.pickUpCard(cardToPickUp);

            // Search askers hand. If picked up card equals a card already in hand, display msg.
            // If AI receieves match, delay msg longer
            matchFoundFromGoFish = false;
            if(asker == AI){
                for(int i = 0; i < asker.getHand().size() - 1; i++){
                    if(asker.getHand().get(i).getRank() == cardToPickUp.getRank()){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                matchFoundFromGoFish = true;
                                centreText.setText(R.string.ai_match_found_from_draw);
                                centreText.setVisibility(View.VISIBLE);
                                humanScore.setVisibility(View.GONE);
                                aiScore.setVisibility(View.GONE);
                            }
                        }, 7 * POPUP_DISPLAY_DURATION);
                    }
                }
            } else{
                for(int i = 0; i < asker.getHand().size() - 1; i++){
                    if(asker.getHand().get(i).getRank() == cardToPickUp.getRank()){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                matchFoundFromGoFish = true;
                                centreText.setText(R.string.match_found_from_draw);
                                centreText.setVisibility(View.VISIBLE);
                                humanScore.setVisibility(View.GONE);
                                aiScore.setVisibility(View.GONE);
                            }
                        }, 4*POPUP_DISPLAY_DURATION);
                    }
                }
            }


        } else {
            asker.addToHand(returnedCards);
            Log.i("Go Fish", "Cards of rank " + rank + " found!");
        }

        completedPairs.batchAdd(scorePoints(asker));

        final String[] rank_names = getResources().getStringArray(R.array.rank_names_plural);

        // AI's turn
        if (!originalCurrentlyPlayerTurn) {

            displayTurn(AI);

            // I'm Thinking...
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewThinking.setVisibility(View.VISIBLE);
                    rankSelector.setVisibility(View.GONE);
                }
            }, POPUP_DISPLAY_DURATION);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Swap to Rank Question
                    textViewThinking.setVisibility(View.GONE);
                    centreText.setText(String.format(getResources().getString(R.string.do_you_have_rank), rank_names[rank-1]));
                    centreText.setVisibility(View.VISIBLE);
                }
            }, 3*POPUP_DISPLAY_DURATION);

            // Swap to "Go Fish" or "Match Found"
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    centreText.setVisibility(View.VISIBLE);
                    centreText.setText((returnedCards.isEmpty()) ? R.string.go_fish_description : R.string.match_found);
                }
            }, 5*POPUP_DISPLAY_DURATION);

            if(matchFoundFromGoFish){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        centreText.setVisibility(View.GONE);
                        humanScore.setVisibility(View.VISIBLE);
                        aiScore.setVisibility(View.VISIBLE);
                        try {
                            playRound();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 7*POPUP_DISPLAY_DURATION);
            } else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        centreText.setVisibility(View.GONE);
                        humanScore.setVisibility(View.VISIBLE);
                        aiScore.setVisibility(View.VISIBLE);
                        try {
                            playRound();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 9*POPUP_DISPLAY_DURATION);
            }
        }

        // Player's Turn
        else {
            displayTurn(human);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Swap to Rank Question
                    centreText.setText(String.format(getResources().getString(R.string.do_you_have_rank), rank_names[rank-1]));
                    centreText.setVisibility(View.VISIBLE);
                }
            }, 1);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Swap to "Go Fish" or "Match Found"
                    rankSelector.setVisibility(View.GONE);
                    centreText.setText((originalCurrentlyPlayerTurn == currentlyPlayerTurn) ? R.string.match_found : R.string.go_fish_description);
                    centreText.setVisibility(View.VISIBLE);
                }
            }, 2*POPUP_DISPLAY_DURATION);

            if(matchFoundFromGoFish){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        centreText.setVisibility(View.GONE);
                        humanScore.setVisibility(View.VISIBLE);
                        aiScore.setVisibility(View.VISIBLE);
                        try {
                            playRound();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 4*POPUP_DISPLAY_DURATION);
            }else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        centreText.setVisibility(View.GONE);
                        humanScore.setVisibility(View.VISIBLE);
                        aiScore.setVisibility(View.VISIBLE);
                        try {
                            playRound();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                },6*POPUP_DISPLAY_DURATION);
            }
        }
    }

    private ArrayList<Card> scorePoints(Player p) {
        ArrayList<Card> newCompletedPairs = new ArrayList<>();
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
                        Log.i("Go Fish", "Removing " + tempHand.get(i).getStyleId() + " and " + tempHand.get(j).getStyleId() + " From " + ((p.isHuman()) ? "human" : "nonhuman"));
                        tempHand.remove(j);
                        tempHand.remove(i);
                        changesMade = true;
                    }
                    if (changesMade) break;
                }
                if (changesMade) break;
            }
        }
        p.assignHand(tempHand);

        if (!deck.isEmpty() && AI.hasEmptyHand()){
            AI.pickUpCard(deck.drawTop());
        }
        if(!deck.isEmpty() && human.hasEmptyHand()){
            human.pickUpCard(deck.drawTop());
        }

        //displayButtons();
        return newCompletedPairs;
    }

    private void setupGame() {

        rand = new Random();

        deck = new Deck(1);
        completedPairs = new Deck();

        easyDifficulty = getSharedPreferences("sharedPrefs", MODE_PRIVATE).getBoolean("rbNormal", true);
        Log.i("Go Fish", (easyDifficulty) ? "Easy Mode" : "Normal Mode");

        ArrayList<Card> hand1 = new ArrayList<>();
        ArrayList<Card> hand2 = new ArrayList<>();

        human = new Player(true);
        AI = new Player(false);

        // Generates two starting hands
        for (int i = 0; i < STARTING_HAND_SIZE; i++) {
            hand1.add(deck.drawTop());
            hand2.add(deck.drawTop());
        }

        // Randomly chooses either the AI or the Human as the dealer.
        // Non-dealer receives the deck containing the first-drawn card and plays first.
        if (rand.nextInt(1) == 0) {
            Log.i("Go Fish", "Player Goes First");
            currentlyPlayerTurn = true;
            human.assignHand(hand1);
            AI.assignHand(hand2);
        } else {
            Log.i("Go Fish", "AI Goes First");
            currentlyPlayerTurn = false;
            human.assignHand(hand2);
            AI.assignHand(hand1);
        }

        Log.i("Go Fish", "Setup Complete");
        Log.i("Go Fish", human.toString());
        Log.i("Go Fish", AI.toString());

        completedPairs.batchAdd(scorePoints(human));
        if(human.getScore() > 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rankSelector.setVisibility(View.GONE);
                    centreText.setVisibility(View.VISIBLE);
                    if(human.getScore() >= 1)
                        centreText.setText(String.format(getResources().getString(R.string.you_started_pair), human.getScore()));
                }
            }, 1);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rankSelector.setVisibility(View.VISIBLE);
                centreText.setVisibility(View.GONE);
                try {
                    playRound();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 2*POPUP_DISPLAY_DURATION);

        completedPairs.batchAdd(scorePoints(AI));
    }

    private void displayTurn(Player player){
        if(!player.isHuman())
            centreText.setText(R.string.ai_turn);
        else {
            centreText.setText(R.string.your_turn);
            rankSelector.setVisibility(View.GONE);
        }
        centreText.setVisibility(View.VISIBLE);
    }

    private void setupUI() {
        setContentView(R.layout.activity_go_fish);
        playerHand = findViewById(R.id.playerHand);
        aiHand = findViewById(R.id.aiHand);
        rankSelector = findViewById(R.id.rankSelector);
        humanScore = findViewById(R.id.humanScore);
        aiScore = findViewById(R.id.aiScore);
        cardsLeftInDeck = findViewById(R.id.cards_left_in_deck);
        humanCardsInHand = findViewById(R.id.playerCardsInHand);
        aiCardsInHand = findViewById(R.id.aiCardsInHand);
        centreText = findViewById(R.id.centreText);
        textViewThinking = findViewById(R.id.textViewThinking);

        yesBtn = findViewById(R.id.yesBtn);
        noBtn = findViewById(R.id.noBtn);
    }

    private void updateUI() {

        playerHand.removeAllViews();
        aiHand.removeAllViews();

        // Player Hand
        for (final Card temp : human.getHand()) {
            final TextView tempText = new TextView(this);
            tempText.setTag(temp.getRank());
            tempText.setId(temp.getRank());

            // Rank
            tempText.setText(temp.getRankStr());
            if (temp.getRank() == 11)
                tempText.setText("J");
            else if (temp.getRank() == 12)
                tempText.setText("Q");
            else if (temp.getRank() == 13)
                tempText.setText("K");
            else if (temp.getRank() == 1)
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

                tempText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            handleRankSelectionFromPlayer2(tempText);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }

        // AI Hand
        for (Card temp : AI.getHand()) {
            TextView tempText = new TextView(this);
            tempText.setText(temp.getRankStr());
            tempText.setBackgroundResource(R.drawable.card_back);
            tempText.setWidth(300);
            aiHand.addView(tempText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }

        // Scores
        humanScore.setText(String.format(getResources().getString(R.string.your_score), human.getScore()));
        aiScore.setText(String.format(getResources().getString(R.string.ai_score), AI.getScore()));

        // Cards left in Deck
        cardsLeftInDeck.setText(String.format(getResources().getString(R.string.cards_left_in_deck), deck.size()));

        // Cards in Hand
        humanCardsInHand.setText(String.format(getResources().getString(R.string.cards_in_hand), human.getHandSize()));
        aiCardsInHand.setText(String.format(getResources().getString(R.string.cards_in_hand), AI.getHandSize()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setupGame();
        try {
            playRound();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}