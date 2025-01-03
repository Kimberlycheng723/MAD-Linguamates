package com.example.madasignment.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.madasignment.R;
import com.example.madasignment.lesson_quiz.LessonListeningQuestionFragment;
import com.example.madasignment.lesson_quiz.LessonQuestionFragmentCallback;
import com.example.madasignment.lesson_quiz.LessonQuestionFragmentFactory;
import com.example.madasignment.lesson_quiz.LessonSpeakingQuestionFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LessonTestActivity extends AppCompatActivity implements LessonQuestionFragmentCallback {

    private static final long TEST_DURATION = 120000; // 2 minutes
    private static List<Fragment> questionFragments; // Store shuffled order persistently
    private ProgressBar progressBar;
    private TextView timerTextView;
    private List<QuestionStatus> questionStatuses = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int totalScore = 0;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_question);

        boolean isFromBreakdown = getIntent().getBooleanExtra("IS_FROM_BREAKDOWN", false);
        int questionNumberToLoad = getIntent().getIntExtra("QUESTION_NUMBER", -1);

        // Initialize or reuse question fragments
        if (questionFragments == null) {
            questionFragments = LessonQuestionFragmentFactory.createQuestionFragments();
            Collections.shuffle(questionFragments); // Shuffle only once
        }

        // Set specific question index if navigating from breakdown
        if (isFromBreakdown && questionNumberToLoad > 0 && questionNumberToLoad <= questionFragments.size()) {
            currentQuestionIndex = questionNumberToLoad - 1;
        }

        // Initialize views
        progressBar = findViewById(R.id.progressBar);

        if (isFromBreakdown) {
            progressBar.setVisibility(View.GONE);
            addBackToBreakdownButton();
        } else {
            progressBar.setMax(questionFragments.size()); // Set the maximum progress
            progressBar.setProgress(0); // Start progress at 0
            addTimer();
            startTimer();
        }

        loadCurrentQuestion();
    }

    private void addTimer() {
        timerTextView = new TextView(this);
        timerTextView.setText("Time left: 2:00");
        timerTextView.setTextSize(18);
        timerTextView.setTextColor(getResources().getColor(android.R.color.black));
        timerTextView.setGravity(Gravity.CENTER_HORIZONTAL);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        params.setMargins(16, 16, 16, 0);

        FrameLayout rootLayout = findViewById(android.R.id.content);
        rootLayout.addView(timerTextView, params);
    }

    private void startTimer() {
        if (!getIntent().getBooleanExtra("IS_FROM_BREAKDOWN", false)) {
            timer = new CountDownTimer(TEST_DURATION, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long minutes = millisUntilFinished / 60000;
                    long seconds = (millisUntilFinished % 60000) / 1000;
                    timerTextView.setText(String.format("Time left: %02d:%02d", minutes, seconds));
                }

                @Override
                public void onFinish() {
                    timerTextView.setText("Time's up!");
                    endTest();
                }
            };
            timer.start();
        }
    }

    private void loadCurrentQuestion() {
        if (currentQuestionIndex < questionFragments.size()) {
            Fragment fragment = questionFragments.get(currentQuestionIndex);

            // Replace the fragment container with the current question fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();

            // Update progress bar
            progressBar.setProgress(currentQuestionIndex); // Keep the progress starting at 0
        } else {
            endTest();
        }
    }

    private void endTest() {
        if (timer != null) {
            timer.cancel();
        }

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("TOTAL_SCORE", totalScore);
        intent.putExtra("TOTAL_QUESTIONS", questionFragments.size());
        intent.putExtra("QUESTION_STATUSES", (Serializable) questionStatuses);
        startActivity(intent);
        finish();
    }

    private void addBackToBreakdownButton() {
        Button backToBreakdownButton = new Button(this);
        backToBreakdownButton.setText("Back to Breakdown");
        backToBreakdownButton.setOnClickListener(v -> navigateBackToBreakdown());

        FrameLayout rootLayout = findViewById(android.R.id.content);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.setMargins(16, 16, 16, 32);
        rootLayout.addView(backToBreakdownButton, params);
    }

    private void navigateBackToBreakdown() {
        Intent intent = new Intent(this, AnswerBreakdownActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onQuestionAnswered(boolean isCorrect) {
        int questionNumber = currentQuestionIndex + 1;
        Fragment currentFragment = questionFragments.get(currentQuestionIndex);

        // Determine question type
        boolean isSpeaking = currentFragment instanceof LessonSpeakingQuestionFragment;
        boolean isListening = currentFragment instanceof LessonListeningQuestionFragment;
        boolean isVocabulary = !(isSpeaking || isListening);

        // Record question status
        questionStatuses.add(new QuestionStatus(questionNumber, isCorrect, isVocabulary, isSpeaking, isListening));

        if (isCorrect) {
            totalScore++;
        }

        currentQuestionIndex++;
        loadCurrentQuestion();
    }

    @Override
    public void onQuestionSkipped() {
        int questionNumber = currentQuestionIndex + 1;

        // Default skipped question to vocabulary type
        questionStatuses.add(new QuestionStatus(questionNumber, false, true, false, false));

        currentQuestionIndex++;
        loadCurrentQuestion();
    }
}
