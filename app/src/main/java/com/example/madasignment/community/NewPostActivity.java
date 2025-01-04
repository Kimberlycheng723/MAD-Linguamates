package com.example.madasignment.community;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NewPostActivity extends AppCompatActivity {

    private EditText edtPostTitle, edtPostContent;
    private Button btnSubmitPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        edtPostTitle = findViewById(R.id.edtPostTitle); // Added for title
        edtPostContent = findViewById(R.id.edtPostContent);
        btnSubmitPost = findViewById(R.id.btnSubmitPost);

        btnSubmitPost.setOnClickListener(v -> {
            String postTitle = edtPostTitle.getText().toString().trim();
            String postContent = edtPostContent.getText().toString().trim();

            if (postTitle.isEmpty() || postContent.isEmpty()) {
                Toast.makeText(this, "Title and content cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            addNewPost(postTitle, postContent);
        });
    }

    private void addNewPost(String postTitle, String postContent) {
        Log.d("NewPostActivity", "addNewPost triggered");

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(); // Get current user's UID

        if (userId == null) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);

        // Fetch userName from the User node
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("username").getValue(String.class);

                if (userName == null || userName.isEmpty()) {
                    userName = "user"; // Default if no userName is found
                }

                // Proceed to create the post in the database with the fetched userName
                createPostInDatabase(postTitle, postContent, userName, userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NewPostActivity", "Failed to fetch userName", error.toException());
                // Use "Anonymous" if userName fetching fails
                createPostInDatabase(postTitle, postContent, "Anonymous", userId);
            }
        });
    }

    private void createPostInDatabase(String postTitle, String postContent, String userName, String userId) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("DiscussionPosts");
        String postId = postsRef.push().getKey(); // Generate a unique ID for the post
        String timestamp = String.valueOf(System.currentTimeMillis());

        if (postId == null) {
            Toast.makeText(this, "Error generating post. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> postData = new HashMap<>();
        postData.put("postId", postId);
        postData.put("title", postTitle);
        postData.put("content", postContent);
        postData.put("userName", userName); // Include fetched userName
        postData.put("userId", userId); // Include userId for reference
        postData.put("timestamp", timestamp);

        postsRef.child(postId).setValue(postData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("NewPostActivity", "Post added successfully");
                Toast.makeText(this, "Post added successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                Log.e("NewPostActivity", "Failed to add post", task.getException());
                Toast.makeText(this, "Failed to add post.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
