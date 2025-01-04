package com.example.madasignment.home.lesson_unit.lesson_quiz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.madasignment.R;

import java.util.Collections;
import java.util.List;

public class LessonQuestionActivity extends AppCompatActivity implements LessonQuestionFragmentCallback {

    private ProgressBar progressBar;
    private ImageView closeQuizButton;
    private List<Fragment> questionFragments;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_question);

        // Initialize Views
        progressBar = findViewById(R.id.progressBar);
        closeQuizButton = findViewById(R.id.closeQuizButton);

        // Set initial progress to 0
        progressBar.setProgress(0);

        // Handle Close Button
        closeQuizButton.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit the quiz?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show());

        // Initialize and shuffle question fragments
        questionFragments = LessonQuestionFragmentFactory.createQuestionFragments();
        Collections.shuffle(questionFragments); // Randomize the questions

        // Load the first question
        loadNextQuestion();
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < questionFragments.size()) {
            // Update progress dynamically
            int progress = (currentQuestionIndex) * 100 / questionFragments.size(); // Start at 0 for the first question
            updateProgress(progress);

            // Load the fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, questionFragments.get(currentQuestionIndex))
                    .commit();
        } else {
            // Quiz finished, show congratulations dialog
            Intent intent = new Intent(this, LessonCompletionActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void showQuizCompletionScreen() {
        new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage("You've completed the quiz!")
                .setPositiveButton("Finish", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void updateProgress(int progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void onQuestionAnswered(boolean isCorrect) {
        if (isCorrect) {
            showCorrectDialog();
        } else {
            showWrongDialog();
        }
    }

    @Override
    public void onQuestionSkipped() {
        // Skip the current question and load the next one
        currentQuestionIndex++;
        loadNextQuestion();
    }

    private void showCorrectDialog() {
        // Create a dialog with a translucent theme
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_correct_answer);

        // Find and set up the Continue button
        ImageButton continueButton = dialog.findViewById(R.id.continueImageButton);
        continueButton.setOnClickListener(v -> {
            dialog.dismiss(); // Dismiss the dialog
            currentQuestionIndex++; // Move to the next question
            loadNextQuestion(); // Load the next question fragment
        });

        dialog.setCancelable(false); // Prevent accidental dismiss
        dialog.show(); // Display the dialog
    }

    private void showWrongDialog() {
        // Create a dialog with a translucent theme
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_wrong_answer);

        // Find and set up the Try Again button
        ImageButton tryAgainButton = dialog.findViewById(R.id.tryAgainImageButton);
        tryAgainButton.setOnClickListener(v -> {
            dialog.dismiss(); // Close the dialog and allow the user to retry
        });

        dialog.setCancelable(false); // Prevent accidental dismiss
        dialog.show(); // Display the dialog
    }
}
