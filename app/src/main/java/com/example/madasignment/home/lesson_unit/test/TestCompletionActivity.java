package com.example.madasignment.home.lesson_unit.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.example.madasignment.gamification.BadgeUtils;
import com.example.madasignment.home.lesson_unit.lesson_unit.LessonUnit;


public class TestCompletionActivity extends AppCompatActivity {

    private int testsCompleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_completion);

        ImageView fireworkLeft = findViewById(R.id.fireworkLeft);
        ImageView fireworkRight = findViewById(R.id.fireworkRight);
        LinearLayout scoreContainer = findViewById(R.id.scoreContainer);
        TextView scoreText = findViewById(R.id.scoreText);
        Button backToHomeButton = findViewById(R.id.backToHomeButton);

        Animation animationLeft = AnimationUtils.loadAnimation(this, R.anim.firework_animation);
        Animation animationRight = AnimationUtils.loadAnimation(this, R.anim.firework_animation);

        scoreContainer.startAnimation(animationLeft);
        fireworkLeft.startAnimation(animationLeft);
        fireworkRight.postDelayed(() -> fireworkRight.startAnimation(animationRight), 500);

        testsCompleted++;
        updateTestBadges(testsCompleted);



        backToHomeButton.setOnClickListener(v -> startActivity(new Intent(this, LessonUnit.class)));
    }

    private void updateTestBadges(int testsCompleted) {
        if (testsCompleted >= 1) BadgeUtils.updateBadgeProgress("First Test", testsCompleted);
        if (testsCompleted >= 3) BadgeUtils.updateBadgeProgress("3 Tests", testsCompleted);
        if (testsCompleted >= 5) BadgeUtils.updateBadgeProgress("5 Tests", testsCompleted);
    }
}
