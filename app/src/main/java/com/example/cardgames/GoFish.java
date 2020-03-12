package com.example.cardgames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.ViewGroup;

public class GoFish extends AppCompatActivity {

    ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_fish);
        mainLayout = findViewById(R.id.go_fish_main_layout);
    }
}