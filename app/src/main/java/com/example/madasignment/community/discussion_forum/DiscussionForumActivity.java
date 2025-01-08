package com.example.madasignment.community.discussion_forum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;
import com.example.madasignment.community.CommunityFrontPageActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiscussionForumActivity extends AppCompatActivity {

    private static final String TAG = "DiscussionForumActivity"; // For Logcat
    private RecyclerView recyclerView;
    private DiscussionAdapter discussionAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_discussion_forum);

        Log.d(TAG, "onCreate: Initializing views and adapter");

        initViews();
        setupRecyclerView();
        fetchDiscussionPosts();
    }

    private void initViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> navigateToCommunityFrontPage());

        Button btnNewPost = findViewById(R.id.btnNewPost);
        btnNewPost.setOnClickListener(v -> navigateToNewPost());
    }

    private void setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: Setting up RecyclerView and adapter");
        recyclerView = findViewById(R.id.recyclerViewDiscussion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        discussionAdapter = new DiscussionAdapter(this);
        recyclerView.setAdapter(discussionAdapter);
    }

    private void fetchDiscussionPosts() {
        Log.d(TAG, "fetchDiscussionPosts: Fetching posts from Firebase");
        databaseReference = FirebaseDatabase.getInstance().getReference("DiscussionPosts");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: DataSnapshot received");
                List<DiscussionPost> fetchedPosts = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DiscussionPost post = dataSnapshot.getValue(DiscussionPost.class);
                    if (post != null) {
                        fetchedPosts.add(post);
                        Log.d(TAG, "Post fetched: " + post.getTitle());
                    } else {
                        Log.w(TAG, "Null post encountered in DataSnapshot");
                    }
                }
                if (!fetchedPosts.isEmpty()) {
                    Log.d(TAG, "Total posts fetched: " + fetchedPosts.size());
                    updateRecyclerView(fetchedPosts);
                } else {
                    Log.w(TAG, "No posts available");
                    Toast.makeText(DiscussionForumActivity.this, "No posts available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
                Toast.makeText(DiscussionForumActivity.this, "Failed to fetch posts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecyclerView(List<DiscussionPost> posts) {
        Log.d(TAG, "updateRecyclerView: Updating adapter with fetched posts");
        discussionAdapter.setData(posts);
        Toast.makeText(this, "Posts updated", Toast.LENGTH_SHORT).show();
    }

    private void navigateToCommunityFrontPage() {
        Log.d(TAG, "navigateToCommunityFrontPage: Navigating back to Community Front Page");
        Intent intent = new Intent(this, CommunityFrontPageActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToNewPost() {
        Log.d(TAG, "navigateToNewPost: Navigating to New Post Activity");
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);
    }
}
