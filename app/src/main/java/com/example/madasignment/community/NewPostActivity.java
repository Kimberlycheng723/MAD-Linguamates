package com.example.madasignment.community;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;

public class NewPostActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextContent;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        // Initialize views
        editTextTitle = findViewById(R.id.editTextPostTitle);
        editTextContent = findViewById(R.id.editTextPostContent);
        buttonSubmit = findViewById(R.id.buttonSubmitPost);

        // Handle button click
        buttonSubmit.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String content = editTextContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(NewPostActivity.this, "Both fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call method to save the post (can integrate Firebase or local DB)
            savePost(title, content);

            // Finish the activity
            finish();
        });
    }

    private void savePost(String title, String content) {
        // Logic to save the post to your database
        Toast.makeText(this, "Post Submitted: " + title, Toast.LENGTH_SHORT).show();
    }
}
