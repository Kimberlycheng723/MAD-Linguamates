package com.example.madasignment.community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SpecificDiscussionActivity extends AppCompatActivity {

    private static final String TAG = "SpecificDiscussionActivity";

    private TextView tvTitle, tvAuthorName, tvDiscussionContent;
    private RecyclerView repliesRecyclerView;
    private EditText etReply;
    private Button btnSendReply;

    private String discussionId;
    private DatabaseReference discussionRef, repliesRef;
    private DatabaseReference userRef;
    private List<Reply> repliesList;
    private RepliesAdapter repliesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_discussion);

        Log.d(TAG, "onCreate: Initializing SpecificDiscussionActivity");

        // Initialize Views
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthorName = findViewById(R.id.tvAuthorName);
        tvDiscussionContent = findViewById(R.id.tvDiscussionContent);
        repliesRecyclerView = findViewById(R.id.repliesRecyclerView);
        etReply = findViewById(R.id.etReply);
        btnSendReply = findViewById(R.id.btnSendReply);

        // Get discussionId from Intent
        discussionId = getIntent().getStringExtra("discussionId");
        if (discussionId == null || discussionId.isEmpty()) {
            Log.e(TAG, "onCreate: Discussion ID is missing");
            Toast.makeText(this, "Discussion ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d(TAG, "onCreate: Discussion ID received - " + discussionId);

        // Initialize Firebase References
        discussionRef = FirebaseDatabase.getInstance().getReference("DiscussionPosts").child(discussionId);
        repliesRef = discussionRef.child("Replies");
        userRef = FirebaseDatabase.getInstance().getReference("User");

        // Set up RecyclerView
        repliesList = new ArrayList<>();
        repliesAdapter = new RepliesAdapter(repliesList);
        repliesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        repliesRecyclerView.setAdapter(repliesAdapter);

        // Load Discussion Details
        loadDiscussionDetails();

        // Load Replies
        loadReplies();

        // Send Reply
        btnSendReply.setOnClickListener(v -> {
            String replyContent = etReply.getText().toString().trim();
            if (!replyContent.isEmpty()) {
                sendReply(replyContent);
            } else {
                Log.w(TAG, "onClick: Reply content is empty");
                Toast.makeText(this, "Reply cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        // Back Button
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Navigating back to DiscussionForumActivity");
            Intent intent = new Intent(SpecificDiscussionActivity.this, DiscussionForumActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadDiscussionDetails() {
        Log.d(TAG, "loadDiscussionDetails: Fetching discussion details for discussionId: " + discussionId);

        discussionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "onDataChange: Discussion data found for discussionId: " + discussionId);

                    // Extract details from the snapshot
                    String title = snapshot.child("title").getValue(String.class);
                    String userName = snapshot.child("userName").getValue(String.class);
                    String content = snapshot.child("content").getValue(String.class);

                    // Update the UI
                    tvTitle.setText(title != null ? title : "No Title");
                    tvAuthorName.setText(userName != null ? userName : "Unknown User");
                    tvDiscussionContent.setText(content != null ? content : "No Content");

                    Log.d(TAG, "loadDiscussionDetails: Title: " + title + ", UserName: " + userName + ", Content: " + content);
                } else {
                    Log.w(TAG, "onDataChange: No data found for discussionId: " + discussionId);
                    Toast.makeText(SpecificDiscussionActivity.this, "Discussion not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: Error fetching discussion details", error.toException());
                Toast.makeText(SpecificDiscussionActivity.this, "Failed to load discussion", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadReplies() {
        DatabaseReference discussionRepliesRef = FirebaseDatabase.getInstance()
                .getReference("Replies")
                .child(discussionId); // Use the current discussionId

        discussionRepliesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                repliesList.clear();
                for (DataSnapshot replySnapshot : snapshot.getChildren()) {
                    Reply reply = replySnapshot.getValue(Reply.class);
                    if (reply != null) {
                        repliesList.add(reply);
                    }
                }
                repliesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to load replies", error.toException());
            }
        });
    }



    private void sendReply(String replyContent) {
        Log.d(TAG, "sendReply: Attempting to send reply");

        // Generate a unique replyId
        String replyId = FirebaseDatabase.getInstance().getReference("Replies").child(discussionId).push().getKey();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (replyId != null && userId != null) {
            Log.d(TAG, "sendReply: Generated replyId - " + replyId);
            Log.d(TAG, "sendReply: Current userId - " + userId);

            DatabaseReference repliesRef = FirebaseDatabase.getInstance()
                    .getReference("Replies")
                    .child(discussionId); // Points to Replies/<discussionId>

            // Fetch user name from User node
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userName = snapshot.child("name").getValue(String.class);
                    if (userName == null) userName = "Anonymous";

                    Log.d(TAG, "sendReply: Fetched userName - " + userName);

                    // Create a Reply object
                    Reply reply = new Reply(replyId, userId, userName, replyContent, String.valueOf(System.currentTimeMillis()));

                    // Add the reply to the Replies/<discussionId>/<replyId> node
                    repliesRef.child(replyId).setValue(reply)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "sendReply: Reply successfully added to Replies/" + discussionId + "/" + replyId);
                                etReply.setText(""); // Clear the input field
                                Toast.makeText(SpecificDiscussionActivity.this, "Reply sent successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "sendReply: Failed to save reply", e);
                                Toast.makeText(SpecificDiscussionActivity.this, "Failed to send reply", Toast.LENGTH_SHORT).show();
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "sendReply: Error fetching userName", error.toException());
                }
            });
        } else {
            Log.e(TAG, "sendReply: replyId or userId is null");
            Toast.makeText(this, "Failed to send reply. Try again.", Toast.LENGTH_SHORT).show();
        }
    }






}
