package com.example.madasignment;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Find the button that navigates to the results page
        Button btnStartQuiz = findViewById(R.id.btnStartQuiz);

        // Handle button click to navigate to QuizResultsActivity
        btnStartQuiz.setOnClickListener(v -> {
            // Pass a score to the results page as an example
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("score", 20); // Pass the score (20 XP in this case)
            startActivity(intent);
        });
    }
}
