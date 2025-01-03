package com.example.madasignment.community;

import android.media.Image;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SpecificDiscussionActivity extends AppCompatActivity {

    private TextView tvAuthorName, tvDiscussionContent, tvTitle;
    private RecyclerView repliesRecyclerView;
    private EditText etReply;
    private ImageButton btnSendReply;

    private RepliesAdapter repliesAdapter;
    private List<Reply> repliesList;

    private DatabaseReference discussionRef, repliesRef;
    private String discussionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_discussion);

        // Initialize Views
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthorName = findViewById(R.id.tvAuthorName);
        tvDiscussionContent = findViewById(R.id.tvDiscussionContent);
        repliesRecyclerView = findViewById(R.id.repliesRecyclerView);
        etReply = findViewById(R.id.etReply);
        Button btnSendReply = findViewById(R.id.btnSendReply);

        // Get discussionId from Intent
        discussionId = getIntent().getStringExtra("discussionId");

        // Initialize Firebase References
        discussionRef = FirebaseDatabase.getInstance().getReference("Discussions").child(discussionId);
        repliesRef = discussionRef.child("Replies");

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
                Toast.makeText(SpecificDiscussionActivity.this, "Reply cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDiscussionDetails() {
        discussionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String authorName = snapshot.child("authorName").getValue(String.class);
                    String discussionContent = snapshot.child("content").getValue(String.class);

                    tvAuthorName.setText(authorName != null ? authorName : "Anonymous");
                    tvDiscussionContent.setText(discussionContent != null ? discussionContent : "No content available");
                } else {
                    Toast.makeText(SpecificDiscussionActivity.this, "Discussion not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SpecificDiscussionActivity.this, "Failed to load discussion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadReplies() {
        repliesRef.addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(SpecificDiscussionActivity.this, "Failed to load replies", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendReply(String replyContent) {
        String userId = "currentUserId"; // Replace with dynamic user ID
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userName = snapshot.child("name").getValue(String.class);
                    String userProfilePic = snapshot.child("profilePic").getValue(String.class); // Optional
                    String replyId = repliesRef.push().getKey();
                    String timestamp = String.valueOf(System.currentTimeMillis());

                    // Create Reply Object
                    Reply reply = new Reply(replyId, userName != null ? userName : "Anonymous", replyContent, userProfilePic, timestamp);

                    // Save Reply to Database
                    repliesRef.child(replyId).setValue(reply).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SpecificDiscussionActivity.this, "Reply sent", Toast.LENGTH_SHORT).show();
                            etReply.setText(""); // Clear input field
                        } else {
                            Toast.makeText(SpecificDiscussionActivity.this, "Failed to send reply", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SpecificDiscussionActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SpecificDiscussionActivity.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
