package com.example.madasignment.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;

import java.util.List;

public class AnswerBreakdownActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_breakdown);

        // Retrieve question statuses from Intent
        List<QuestionStatus> questionStatuses = (List<QuestionStatus>) getIntent().getSerializableExtra("QUESTION_STATUSES");

        if (questionStatuses == null || questionStatuses.isEmpty()) {
            Log.e("AnswerBreakdown", "No question statuses to display.");
        } else {
            Log.d("AnswerBreakdown", "Received " + questionStatuses.size() + " question statuses.");
            GridLayout questionGridLayout = findViewById(R.id.questionGridLayout);
            populateQuestionGrid(questionGridLayout, questionStatuses);
        }

        // Handle Review Previous Lesson button click
        Button reviewPreviousLessonButton = findViewById(R.id.reviewPreviousLessonButton);
        reviewPreviousLessonButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ResultActivity.class); // Navigate to the ResultActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // Handle End Test button click
        Button endTestButton = findViewById(R.id.buttonEnd); // Ensure the button ID matches your layout
        endTestButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TestCompletionActivity.class); // Navigate to the Test Completion screen
            startActivity(intent);
        });
    }

    private void populateQuestionGrid(GridLayout questionGridLayout, List<QuestionStatus> questionStatuses) {
        questionGridLayout.removeAllViews(); // Clear existing views

        int columns = 4; // Number of questions per row
        questionGridLayout.setColumnCount(columns); // Set the column count

        if (questionStatuses != null && !questionStatuses.isEmpty()) {
            for (QuestionStatus status : questionStatuses) {
                FrameLayout questionFrame = new FrameLayout(this);

                questionFrame.setBackground(getResources().getDrawable(R.drawable.test_dotted_square));

                if (!status.isCorrect) {
                    View redCircle = new View(this);
                    redCircle.setBackground(getResources().getDrawable(R.drawable.red_circle));
                    FrameLayout.LayoutParams circleParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                    );
                    circleParams.setMargins(20, 20, 20, 20); // Adjust margins for spacing
                    questionFrame.addView(redCircle, circleParams);
                }

                TextView questionIndicator = new TextView(this);
                questionIndicator.setText("Q" + status.questionNumber);
                questionIndicator.setTextSize(16);
                questionIndicator.setGravity(android.view.Gravity.CENTER);
                questionIndicator.setTextColor(getResources().getColor(
                        !status.isCorrect ? android.R.color.white : android.R.color.black
                ));
                FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );
                questionFrame.addView(questionIndicator, textParams);

                // Add click listener to navigate to a specific question
                questionFrame.setOnClickListener(v -> {
                    Intent intent = new Intent(this, LessonTestActivity.class);
                    intent.putExtra("QUESTION_NUMBER", status.questionNumber); // Pass the question number
                    intent.putExtra("IS_FROM_BREAKDOWN", true); // Indicate navigation from breakdown
                    startActivity(intent);
                });

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 200;
                params.height = 200;
                params.setMargins(8, 8, 8, 8);
                questionGridLayout.addView(questionFrame, params);
            }
        } else {
            Log.e("AnswerBreakdown", "No question statuses to display.");
        }
    }
}
