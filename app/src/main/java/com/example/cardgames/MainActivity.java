package com.example.cardgames;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //MediaPlayer backgroundMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        backgroundMusic = MediaPlayer.create(this, R.raw.feelin);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();
        */

        ImageButton goFishImageBtn = findViewById(R.id.imageButtonGoFish);
        goFishImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), GoFishMaster.class);
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