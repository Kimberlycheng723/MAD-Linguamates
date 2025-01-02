package com.example.madasignment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private ProgressBar quizProgressBar;
    private TextView questionCounter, questionText, timerText, correctCount;
    private RadioGroup optionsContainer;
    private MaterialButton previousButton, nextButton;

    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;

    private static final long QUIZ_TIME = 60000; // 1 minute

    // List to store questions
    private List<Question> questions;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize views
        quizProgressBar = findViewById(R.id.quizProgressBar);
        questionCounter = findViewById(R.id.questionCounter);
        questionText = findViewById(R.id.questionText);
        optionsContainer = findViewById(R.id.optionsContainer);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        timerText = findViewById(R.id.timerText);
        correctCount = findViewById(R.id.correctCount);

        // Initialize questions
        initializeQuestions();

        // Shuffle the questions
        Collections.shuffle(questions);

        // Set progress bar max
        quizProgressBar.setMax(questions.size());

        // Initialize the first question
        updateQuestion();

        // Start the timer
        startQuizTimer();

        // Navigation button logic
        previousButton.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                updateQuestion();
            }
        });

        nextButton.setOnClickListener(v -> {
            if (currentQuestionIndex < questions.size() - 1) {
                checkAnswer();
                currentQuestionIndex++;
                updateQuestion();
            } else {
                checkAnswer();
                endQuiz();
            }
        });
    }

    private void initializeQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("苹果 (What does this mean?)", new String[]{"Apple", "Orange", "Banana", "Grape"}, 0));
        questions.add(new Question("北京 (What is this city?)", new String[]{"Shanghai", "Beijing", "Tokyo", "Hong Kong"}, 1));
        questions.add(new Question("你好 (What does this mean?)", new String[]{"Hello", "Goodbye", "Thank you", "Yes"}, 0));
        questions.add(new Question("书 (What does this mean?)", new String[]{"Book", "Pen", "Paper", "Bag"}, 0));
        questions.add(new Question("花 (What does this mean?)", new String[]{"Tree", "Flower", "Grass", "Leaf"}, 1));
        questions.add(new Question("月亮 (What does this mean?)", new String[]{"Star", "Moon", "Sun", "Sky"}, 1));
        questions.add(new Question("学校 (What does this mean?)", new String[]{"Hospital", "School", "Park", "Library"}, 1));
        questions.add(new Question("猫 (What does this mean?)", new String[]{"Dog", "Cat", "Bird", "Fish"}, 1));
        questions.add(new Question("雨 (What does this mean?)", new String[]{"Rain", "Snow", "Wind", "Cloud"}, 0));
        questions.add(new Question("大象 (What does this mean?)", new String[]{"Tiger", "Elephant", "Horse", "Lion"}, 1));
    }

    private void updateQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);

        // Update question text
        questionText.setText(currentQuestion.getQuestionText());

        // Update question counter
        questionCounter.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());

        // Update progress bar
        quizProgressBar.setProgress(currentQuestionIndex + 1);

        // Populate options dynamically
        optionsContainer.removeAllViews();
        for (int i = 0; i < currentQuestion.getOptions().length; i++) {
            RadioButton option = new RadioButton(this);
            option.setText(currentQuestion.getOptions()[i]);
            option.setId(i);
            optionsContainer.addView(option);
        }
    }

    private void checkAnswer() {
        int selectedId = optionsContainer.getCheckedRadioButtonId();
        if (selectedId == questions.get(currentQuestionIndex).getCorrectOptionIndex()) {
            correctAnswers++;
            correctCount.setText("Correct: " + correctAnswers);
        }
    }

    private void startQuizTimer() {
        countDownTimer = new CountDownTimer(QUIZ_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText("Time Left: " + (millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                Toast.makeText(QuizActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                endQuiz();
            }
        };
        countDownTimer.start();
    }

    private void endQuiz() {
        if (countDownTimer != null) countDownTimer.cancel();
        Toast.makeText(this, "Quiz Finished! Correct answers: " + correctAnswers, Toast.LENGTH_LONG).show();
        finish();
    }

    // Question class to represent a question
    static class Question {
        private String questionText;
        private String[] options;
        private int correctOptionIndex;

        public Question(String questionText, String[] options, int correctOptionIndex) {
            this.questionText = questionText;
            this.options = options;
            this.correctOptionIndex = correctOptionIndex;
        }

        public String getQuestionText() {
            return questionText;
        }

        public String[] getOptions() {
            return options;
        }

        public int getCorrectOptionIndex() {
            return correctOptionIndex;
        }
    }
}
