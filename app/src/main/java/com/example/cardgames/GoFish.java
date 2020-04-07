package com.example.cardgames;

import android.os.Handler;
import android.view.View;

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
    private View btnAce;
    private View btnTwo;
    private View btnThree;
    private View btnFour;
    private View btnFive;
    private View btnSix;
    private View btnSeven;
    private View btnEight;
    private View btnNine;
    private View btnTen;
    private View btnJack;
    private View btnQueen;
    private View btnKing;

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

    // Constants
    private int STARTING_HAND_SIZE = 7;
    private long POPUP_DISPLAY_DURATION = 1500;

    private void playRound() throws InterruptedException {

        updateUI();

        Log.i("Go Fish", "=======================================================\nPlaying Round");
        Log.i("Go Fish", human.toString());
        Log.i("Go Fish", AI.toString());

        if (deck.isEmpty()) {
            rankSelector.setVisibility(View.GONE);
            String gameOverMessage = (AI.getScore() > human.getScore()) ? "Game Over!\nYou Lose." : "Game Over!\nYou Won!";
            centreText.setText(gameOverMessage);
            centreText.setVisibility(View.VISIBLE);
            Log.i("Go Fish", "Game Over!");
            return;
            //endGame();
        }

        // AI's Turn
        if (!currentlyPlayerTurn) {
            lockPlayer = true;

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
            turn(human);
            lockPlayer = false;

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

    public void handleRankSelectionFromPlayer(View v) throws InterruptedException {
        if (lockPlayer) return;
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

    private void askPlayerForRank(final Player asker, final Player asked, final int rank) throws InterruptedException {
        String askerType = (asker.isHuman()) ? "human" : "nonhuman";
        String askedType = (asked.isHuman()) ? "human" : "nonhuman";
        Log.i("Go Fish", "A " + askerType + " has asked for " + rank + "s from a " + askedType);



        final boolean originalCurrentlyPlayerTurn = currentlyPlayerTurn;
        final int originalAskerScore = asker.getScore();

        ArrayList<Card> returnedCards = asked.getCardsWithRank(rank);
        if (returnedCards.isEmpty()) {
            currentlyPlayerTurn = !currentlyPlayerTurn;
            Card cardToPickUp = deck.drawTop();
            Log.i("Go Fish", "Go Fish!");
            Log.i("Go Fish", ((asker.isHuman()) ? "Human " : "Nonhuman ") + "Picking up " + cardToPickUp.getStyleId());
            asker.pickUpCard(cardToPickUp);

            // If new card rank is the same as one of the cards already in your hand

        } else {
            asker.addToHand(returnedCards);
            Log.i("Go Fish", "Cards of rank " + rank + " found!");
        }

        completedPairs.batchAdd(scorePoints(asker));

        final String[] rank_names = getResources().getStringArray(R.array.rank_names_plural);

        // AI's turn
        if (!originalCurrentlyPlayerTurn) {
/*            // AI's turn
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    centreText.setText(R.string.ai_turn);
                    centreText.setVisibility(View.VISIBLE);
                }
            }, POPUP_DISPLAY_DURATION);

 */
            turn(AI);

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
                    centreText.setText((asker.getScore() > originalAskerScore) ? R.string.match_found : R.string.go_fish_description);
                }
            }, 5*POPUP_DISPLAY_DURATION);

            // Hide Centre Text and play a new round
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    centreText.setVisibility(View.GONE);
                    try {
                        playRound();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 6*POPUP_DISPLAY_DURATION);

        }

        // Player's Turn
        else {
/*            // Player's turn
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    centreText.setText(R.string.your_turn);
                    centreText.setVisibility(View.VISIBLE);
                }
            }, POPUP_DISPLAY_DURATION);

 */
            turn(human);
            // Swap to "Go Fish" or "Match Found"
            rankSelector.setVisibility(View.GONE);
            centreText.setText((originalCurrentlyPlayerTurn == currentlyPlayerTurn) ? R.string.match_found : R.string.go_fish_description);
            centreText.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    centreText.setVisibility(View.GONE);
                    try {
                        playRound();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },2*POPUP_DISPLAY_DURATION);
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

        displayButtons();
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

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                completedPairs.batchAdd(scorePoints(human));
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        completedPairs.batchAdd(scorePoints(AI));
//                    }
//                }, 1000);
//            }
//        }, 1000);

        completedPairs.batchAdd(scorePoints(human));
        if(human.getScore() > 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rankSelector.setVisibility(View.GONE);
                    centreText.setVisibility(View.VISIBLE);
                    if(human.getScore() == 1)
                        centreText.setText("You started with 1 pair!");
                    else
                        centreText.setText("You started with " + human.getScore() + " pairs!");
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
        }, POPUP_DISPLAY_DURATION);

        completedPairs.batchAdd(scorePoints(AI));
    }

    private void turn(Player player){
        if(player == AI)
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
        humanScore.setText(String.valueOf(human.getScore()));
        aiScore.setText(String.valueOf(AI.getScore()));

        // Cards left in Deck
        cardsLeftInDeck.setText(String.format(getResources().getString(R.string.cards_left_in_deck), deck.size()));

        // Cards in Hand
        humanCardsInHand.setText(String.format(getResources().getString(R.string.cards_in_hand), human.getHandSize()));
        aiCardsInHand.setText(String.format(getResources().getString(R.string.cards_in_hand), AI.getHandSize()));
    }

    private void displayButtons() {
        // Show/Hide "Do you have any" buttons based on ranks in hand
        btnAce.setVisibility(View.GONE);
        btnTwo.setVisibility(View.GONE);
        btnThree.setVisibility(View.GONE);
        btnFour.setVisibility(View.GONE);
        btnFive.setVisibility(View.GONE);
        btnSix.setVisibility(View.GONE);
        btnSeven.setVisibility(View.GONE);
        btnEight.setVisibility(View.GONE);
        btnNine.setVisibility(View.GONE);
        btnTen.setVisibility(View.GONE);
        btnJack.setVisibility(View.GONE);
        btnQueen.setVisibility(View.GONE);
        btnKing.setVisibility(View.GONE);
        for (Card temp : human.getHand()) {
            if (temp.getRank() == Integer.parseInt(btnAce.getTag().toString()))
                btnAce.setVisibility(View.VISIBLE);
            if (temp.getRank() == Integer.parseInt(btnTwo.getTag().toString()))
                btnTwo.setVisibility(View.VISIBLE);
            if (temp.getRank() == Integer.parseInt(btnThree.getTag().toString()))
                btnThree.setVisibility(View.VISIBLE);
            if (temp.getRank() == Integer.parseInt(btnFour.getTag().toString()))
                btnFour.setVisibility(View.VISIBLE);
            if (temp.getRank() == Integer.parseInt(btnFive.getTag().toString()))
                btnFive.setVisibility(View.VISIBLE);
            if (temp.getRank() == Integer.parseInt(btnSix.getTag().toString()))
                btnSix.setVisibility(View.VISIBLE);
            if (temp.getRank() == Integer.parseInt(btnSeven.getTag().toString()))
                btnSeven.setVisibility(View.VISIBLE);
            if (temp.getRank() == Integer.parseInt(btnEight.getTag().toString()))
                btnEight.setVisibility(View.VISIBLE);
            if (temp.getRank() == Integer.parseInt(btnNine.getTag().toString()))
                btnNine.setVisibility(View.VISIBLE);
            if (temp.getRank() == Integer.parseInt(btnTen.getTag().toString()))
                btnTen.setVisibility(View.VISIBLE);
            if (temp.getRank() == Integer.parseInt(btnJack.getTag().toString()))
                btnJack.setVisibility(View.VISIBLE);
            if (temp.getRank() == Integer.parseInt(btnQueen.getTag().toString()))
                btnQueen.setVisibility(View.VISIBLE);
            if (temp.getRank() == Integer.parseInt(btnKing.getTag().toString()))
                btnKing.setVisibility(View.VISIBLE);
        }
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