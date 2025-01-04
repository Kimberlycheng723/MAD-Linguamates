package com.example.madasignment.home.lesson_unit.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.example.madasignment.home.lesson_unit.lesson_quiz.LessonCompletionActivity;
import com.google.android.material.button.MaterialButton;

public class TestSplashScreen  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_progress); // Link to your layout file (e.g., activity_main.xml)

        // Initialize the views
        ImageView backButton = findViewById(R.id.backButton);
        MaterialButton startQuizButton = findViewById(R.id.startQuizButton);

        // Back Button Click Listener
        backButton.setOnClickListener(view -> {
            // Action for the back button (simulating going back)
           Intent i = new Intent(TestSplashScreen.this, LessonCompletionActivity.class);
           startActivity(i);
        });

        // Start Quiz Button Click Listener
        startQuizButton.setOnClickListener(view -> {
            Intent i = new Intent(TestSplashScreen.this, LessonTestActivity.class);
            startActivity(i);
        });
    }
}

