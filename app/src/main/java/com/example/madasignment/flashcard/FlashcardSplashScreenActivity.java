package com.example.madasignment.flashcard;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;

public class FlashcardSplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000; // 3 seconds delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_splash_screen);

        // Delay and launch the Flashcard activity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(FlashcardSplashScreenActivity.this, Flashcard.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}