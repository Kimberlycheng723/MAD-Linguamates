package com.example.madasignment.community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewPostActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText edtPostContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        edtPostContent = findViewById(R.id.edtPostContent);
        Button btnSubmitPost = findViewById(R.id.btnSubmitPost);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("DiscussionPosts");

        btnSubmitPost.setOnClickListener(v -> {
            String postContent = edtPostContent.getText().toString().trim();
            if (!postContent.isEmpty()) {
                addNewPost(postContent);
            } else {
                Toast.makeText(this, "Post content cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewPost(String postContent) {
        String postId = databaseReference.push().getKey(); // Generate a unique ID for the post
        String timestamp = String.valueOf(System.currentTimeMillis());

        // Create a new post object
        DiscussionPost post = new DiscussionPost(postId, postContent,"anonymous", timestamp);

        // Add the post to the database
        databaseReference.child(postId).setValue(post)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Post added successfully!", Toast.LENGTH_SHORT).show();
                    // Navigate back to the discussion forum
                    Intent intent = new Intent(NewPostActivity.this, DiscussionForumActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to add post.", Toast.LENGTH_SHORT).show());
    }
}
