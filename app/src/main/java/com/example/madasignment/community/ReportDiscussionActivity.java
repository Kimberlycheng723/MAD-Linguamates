package com.example.madasignment.community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;

public class ReportDiscussionActivity extends AppCompatActivity {

    private TextView tvUserName, tvPostContent;
    private EditText etReason;
    private Button btnSubmit;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_discussion);

        // Initialize Views
        tvUserName = findViewById(R.id.tvUserName);
        tvPostContent = findViewById(R.id.tvPostContent);
        etReason = findViewById(R.id.etReason);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        // Receive Data from Intent
        Intent intent = getIntent();
        String userName = intent.getStringExtra("user_name");
        String postContent = intent.getStringExtra("post_content");

        // Set Data
        tvUserName.setText(userName);
        tvPostContent.setText(postContent);

        // Back Button Logic
        btnBack.setOnClickListener(v -> finish());

        // Submit Button Logic
        btnSubmit.setOnClickListener(v -> {
            String reason = etReason.getText().toString().trim();
            if (reason.isEmpty()) {
                Toast.makeText(this, "Please provide a reason.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Handle Report Submission (Mock for Now)
            Intent intent2 = new Intent(ReportDiscussionActivity.this, ReportSubmitActivity.class);
            startActivity(intent2);
            finish(); // Close the current activity
        });
    }
}
