package com.example.madasignment.gamification;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.example.madasignment.profile.MainActivity;

public class AchievementSplash extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements_splash);

        // Handler to delay and move to the next activity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(AchievementSplash.this, MainActivity.class); // Replace MainActivity with your target activity
            startActivity(intent);
            finish(); // Close splash screen activity
        }, SPLASH_TIME_OUT);
    }
}
