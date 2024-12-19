package com.example.madasignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.flashcard.Flashcard;
import com.example.madasignment.lesson_quiz.LessonQuestionActivity;
import com.example.madasignment.lesson_unit.LessonUnit;
import com.example.madasignment.levels.LevelsActivity;
import com.example.madasignment.read.ReadOnlyActivity;
import com.example.madasignment.video.video;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up button to navigate to Flashcard Activity
        findViewById(R.id.btnFlashcard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Flashcard.class);
                startActivity(intent);
            }
        });

        // Set up button to navigate to Video Activity
        findViewById(R.id.listen_and_read_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, video.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.read_only_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReadOnlyActivity.class);
                startActivity(intent);
            }
        });




        // Navigate to Unit Activity
        findViewById(R.id.btnUnit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LessonUnit.class);
                startActivity(intent);
            }
        });

        Button btnTestLevels = findViewById(R.id.btnLevel);
        btnTestLevels.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LevelsActivity.class);
            startActivity(intent);
        });

        Button startQuizButton = findViewById(R.id.startQuizButton);
        // Set button click listener
        startQuizButton.setOnClickListener(v -> {
            // Navigate to QuestionActivity
            Intent intent = new Intent(MainActivity.this, LessonQuestionActivity.class);
            startActivity(intent);
        });
    }
}
