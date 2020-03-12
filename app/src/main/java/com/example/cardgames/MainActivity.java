package com.example.cardgames;

import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton goFishImageBtn = findViewById(R.id.imageButtonGoFish);
        goFishImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), GoFish.class);
                startActivity(startIntent);
            }
        });

        ImageButton blackjackImageBtn = findViewById(R.id.imageButtonBlackjack);
        blackjackImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), Blackjack.class);
                startActivity(startIntent);
            }
        });
    }
}