package com.example.madasignment.community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import com.example.madasignment.R;


import androidx.appcompat.app.AppCompatActivity;

public class ReportSubmitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_submit);

        // Back Button Logic
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Navigate back to the Forum Discussion
            Intent intent = new Intent(ReportSubmitActivity.this, DiscussionForumActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }
}
