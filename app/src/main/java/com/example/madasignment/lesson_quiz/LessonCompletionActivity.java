package com.example.madasignment.lesson_quiz;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;

public class LessonCompletionActivity extends AppCompatActivity {

    private static final String EXTRA_SCORE = "extra_score"; // Key for passing score
    private int score = 0; // Default score placeholder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_completion);

        // Initialize views
        ImageView fireworkLeft = findViewById(R.id.fireworkLeft);
        ImageView fireworkRight = findViewById(R.id.fireworkRight);
        LinearLayout scoreContainer =findViewById(R.id.scoreContainer);

        TextView scoreText = findViewById(R.id.scoreText);
        Button takeQuizButton = findViewById(R.id.takeQuizButton);
        Button backToHomeButton = findViewById(R.id.backToHomeButton);

        // Temporary score for testing UI
        if (getIntent() != null && getIntent().hasExtra(EXTRA_SCORE)) {
            score = getIntent().getIntExtra(EXTRA_SCORE, 0);
        } else {
            score = 50; // Temporary hardcoded score for testing purposes
        }

        // Set the score in the TextView
        scoreText.setText(String.format("+ %d", score));

        // Firework animations
        fireworkLeft.setVisibility(View.VISIBLE);

        scoreContainer.setVisibility(View.VISIBLE);

        Animation animationLeft = AnimationUtils.loadAnimation(this, R.anim.firework_animation);
        Animation animationRight = AnimationUtils.loadAnimation(this, R.anim.firework_animation);

        scoreContainer.startAnimation(animationLeft);
        fireworkLeft.startAnimation(animationLeft);
        fireworkRight.postDelayed(() -> {
            fireworkRight.setVisibility(View.VISIBLE);
            fireworkRight.startAnimation(animationRight);
        }, 500);

        // Button logic
        takeQuizButton.setOnClickListener(v -> {
            Log.d("Navigation", "Take A Quiz button clicked");
            finish(); // Replace with Intent to open quiz activity
        });

        backToHomeButton.setOnClickListener(v -> {
            Log.d("Navigation", "Back To Home button clicked");
            finish(); // Replace with Intent to open home activity
        });
    }
}
