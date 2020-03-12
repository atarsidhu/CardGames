package com.example.cardgames;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

class Card extends View {

    private int rank;
    private int suit;

    ConstraintLayout cardLayout;
    TextView rankText;
    ImageView suitIcon;

    public Card(Context context) {
        super(context);
        setup();
    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void setup() {
        this.setBackground(getResources().getDrawable(R.drawable.card_base));
        cardLayout = new ConstraintLayout(this.getContext());
        rankText = new TextView(this.getContext());
        rankText.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        rankText.setText("THIS IS SOME SAMPLE TEXT");
        rankText.setTextColor(getResources().getColor(R.color.colorPrimary));
        suitIcon = new ImageView(this.getContext());
        rankText.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        suitIcon.setImageDrawable(getResources().getDrawable(R.drawable.hearts));
        cardLayout.addView(rankText);
        cardLayout.addView(suitIcon);


    }

    public void setRank(int newRank) {
        if (newRank >= 1 && newRank <= 4) rank = newRank;
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

}
