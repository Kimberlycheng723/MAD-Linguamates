package com.example.madasignment.home.lesson_unit.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);

        // Retrieve question statuses
        final List<QuestionStatus> questionStatuses =
                (List<QuestionStatus>) getIntent().getSerializableExtra("QUESTION_STATUSES") != null
                        ? (List<QuestionStatus>) getIntent().getSerializableExtra("QUESTION_STATUSES")
                        : new ArrayList<>();

        // Retrieve total questions and scores
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 0);
        int totalScore = getIntent().getIntExtra("TOTAL_SCORE", 0);

        // Calculate speaking, listening, and vocabulary scores
        int speakingScore = 0, listeningScore = 0, vocabularyScore = 0;
        int speakingQuestions = 0, listeningQuestions = 0, vocabularyQuestions = 0;

        for (QuestionStatus status : questionStatuses) {
            if (status.isMatching) {
                continue; // Skip matching questions
            }
            if (status.isSpeaking) {
                speakingQuestions++;
                if (status.isCorrect) speakingScore++;
            } else if (status.isListening) {
                listeningQuestions++;
                if (status.isCorrect) listeningScore++;
            } else if (status.isVocabulary) {
                vocabularyQuestions++;
                if (status.isCorrect) vocabularyScore++;
            }
        }

        // Calculate percentages
        int totalValidQuestions = speakingQuestions + listeningQuestions + vocabularyQuestions;
        int totalPercentage = (totalQuestions > 0) ? (totalScore * 100) / totalQuestions : 0;
        int xpEarned = calculateXPEarned(totalPercentage);
        int vocabularyPercentage = (vocabularyQuestions > 0) ? (vocabularyScore * 100) / vocabularyQuestions : 0;
        int speakingPercentage = (speakingQuestions > 0) ? (speakingScore * 100) / speakingQuestions : 0;
        int listeningPercentage = (listeningQuestions > 0) ? (listeningScore * 100) / listeningQuestions : 0;

        // Update XP
        XPUtils.updateXP(xpEarned);

        // Update UI elements
        TextView percentageTextView = findViewById(R.id.percentageTextView);
        setColorBasedOnScore(percentageTextView, totalPercentage);
        percentageTextView.setText(totalPercentage + "%");

        TextView passFailText = findViewById(R.id.passFailText);
        if (totalPercentage >= 40) {
            passFailText.setText("You Passed!");
            passFailText.setTextColor(getResources().getColor(android.R.color.holo_blue_dark)); // Blue
        } else {
            passFailText.setText("Try Again!");
            passFailText.setTextColor(getResources().getColor(android.R.color.holo_red_dark)); // Red
        }

        TextView correctAnswersLabel = findViewById(R.id.correctAnswersLabel);
        correctAnswersLabel.setText("Correct Answers: " + totalScore + "/" + totalQuestions);

        TextView pointsEarnedLabel = findViewById(R.id.pointsEarnedLabel);
        pointsEarnedLabel.setText("XP Earned: " + xpEarned);

        TextView vocabularyTextView = findViewById(R.id.vocabularyTextView);
        setColorBasedOnScore(vocabularyTextView, vocabularyPercentage);
        vocabularyTextView.setText(vocabularyPercentage + "%");

        TextView speakingTextView = findViewById(R.id.speakingTextView);
        setColorBasedOnScore(speakingTextView, speakingPercentage);
        speakingTextView.setText(speakingPercentage + "%");

        TextView listeningTextView = findViewById(R.id.listeningTextView);
        setColorBasedOnScore(listeningTextView, listeningPercentage);
        listeningTextView.setText(listeningPercentage + "%");

        // Buttons
        ImageButton answerBreakdownButton = findViewById(R.id.answerBreakdownButton);
        answerBreakdownButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, AnswerBreakdownActivity.class);
            intent.putExtra("QUESTION_STATUSES", (Serializable) questionStatuses);
            startActivity(intent);
        });

        ImageButton retryQuizButton = findViewById(R.id.retryQuizButton);
        if (totalPercentage < 40) {
            retryQuizButton.setImageResource(R.drawable.test_retry_button); // Change button design for Retry
        }
        retryQuizButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, LessonTestActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Set color based on score range.
     *
     * @param textView The TextView to update.
     * @param score    The score percentage.
     */
    private void setColorBasedOnScore(TextView textView, int score) {
        if (score > 70) {
            textView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark)); // Blue
        } else if (score >= 40) {
            textView.setTextColor(Color.parseColor("#FFA500")); // Orange
        } else {
            textView.setTextColor(getResources().getColor(android.R.color.holo_red_dark)); // Red
        }
    }

    private int calculateXPEarned(int percentage) {
        if (percentage >= 90) {
            return 50;
        } else if (percentage >= 70) {
            return 30;
        } else {
            return 10;
        }
    }
}
