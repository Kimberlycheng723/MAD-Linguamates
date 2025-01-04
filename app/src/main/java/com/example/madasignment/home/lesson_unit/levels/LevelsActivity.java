package com.example.madasignment.home.lesson_unit.levels;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.madasignment.R;
import com.example.madasignment.home.lesson_unit.flashcard.FlashcardSplashScreenActivity;

public class LevelsActivity extends AppCompatActivity {

    private TextView diamondCount;
    private TextView streakCount;
    private ImageView backgroundImage;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(""); // Set an empty title
        }
        ImageView backButton = findViewById(R.id.levelsUpButton);
        backButton.setOnClickListener(v -> onBackPressed());


        diamondCount = findViewById(R.id.diamondCount);
        streakCount = findViewById(R.id.streakCount);

        // Set initial values for diamonds and streak
        updateDiamondAndStreak(100, 0);
        // Initialize views

        scrollView = findViewById(R.id.scrollView);

        // Level 1 Bubble
        ImageButton levelBubble1 = findViewById(R.id.levelBubble1);

        levelBubble1.setOnClickListener(v -> {
            // Start the Splash Screen Activity
            Intent intent = new Intent(LevelsActivity.this, FlashcardSplashScreenActivity.class);
            startActivity(intent);
        });



    }

    private void updateDiamondAndStreak(int diamonds, int streak) {
        diamondCount.setText(String.valueOf(diamonds));
        streakCount.setText(String.valueOf(streak));
    }


    }


