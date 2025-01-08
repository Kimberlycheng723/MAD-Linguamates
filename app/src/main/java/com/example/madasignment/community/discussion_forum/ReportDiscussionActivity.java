package com.example.madasignment.community.discussion_forum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportDiscussionActivity extends AppCompatActivity {

    private TextView tvUserName, tvPostContent;
    private EditText etReason;
    private Button btnSubmit;
    private ImageButton btnBack;

    private DatabaseReference reportsRef;
    private String reportedPostId;
    private String reportedBy = "currentUserId"; // Replace with dynamic user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_report_discussion);

        // Initialize Views
        tvUserName = findViewById(R.id.tvUserName);
        tvPostContent = findViewById(R.id.tvPostContent);
        etReason = findViewById(R.id.etReason);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        // Firebase Reference
        reportsRef = FirebaseDatabase.getInstance().getReference("Reports");

        // Receive Data from Intent
        Intent intent = getIntent();
        reportedPostId = intent.getStringExtra("post_id");
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
            submitReport(reason);
        });
    }

    private void submitReport(String reason) {
        // Generate unique report ID
        String reportId = reportsRef.push().getKey();
        if (reportId == null) {
            Toast.makeText(this, "Failed to generate report ID. Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());

        // Replace the placeholder 'currentUserId' with the actual user ID
        String reportedBy = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Log for debugging
        Log.d("ReportDiscussion", "Submitting report: reportId=" + reportId + ", reportedPostId=" + reportedPostId + ", reportedBy=" + reportedBy);

        // Create Report Object
        Report report = new Report(reportId, reportedPostId, reportedBy, reason, timestamp);

        // Save Report to Firebase
        reportsRef.child(reportId).setValue(report).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Report submitted successfully.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReportDiscussionActivity.this, ReportSubmitActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to submit report.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
