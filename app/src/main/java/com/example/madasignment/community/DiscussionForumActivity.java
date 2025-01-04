package com.example.madasignment.community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

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

public class DiscussionForumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DiscussionAdapter discussionAdapter;
    private List<DiscussionPost> discussionPostList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forum);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(DiscussionForumActivity.this, CommunityFrontPageActivity.class);
            startActivity(intent);
            finish();
        });

        Button btnNewPost = findViewById(R.id.btnNewPost);
        btnNewPost.setOnClickListener(v -> {
            Intent intent = new Intent(DiscussionForumActivity.this, NewPostActivity.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recyclerViewDiscussion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("DiscussionPosts");

        discussionPostList = new ArrayList<>();
        discussionAdapter = new DiscussionAdapter(this);
        recyclerView.setAdapter(discussionAdapter);

        fetchDiscussionPosts();
    }

    private void fetchDiscussionPosts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                discussionPostList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DiscussionPost post = dataSnapshot.getValue(DiscussionPost.class);
                    if (post != null) {
                        discussionPostList.add(post);
                    }
                }
                discussionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DiscussionForumActivity", "Database error: " + error.getMessage());
            }
        });
    }

}