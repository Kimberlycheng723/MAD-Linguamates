package com.example.madasignment.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.example.madasignment.lesson_quiz.LessonQuestionActivity;

public class FinishSplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_splash);

        // Automatically navigate to LessonQuestionActivity after a delay (e.g., 3 seconds)
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(FinishSplashActivity.this, LessonQuestionActivity.class);
            startActivity(intent);
            finish(); // Finish this activity
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}
