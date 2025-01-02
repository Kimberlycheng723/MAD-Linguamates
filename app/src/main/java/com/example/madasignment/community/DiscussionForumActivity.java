package com.example.madasignment.community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.List;

public class DiscussionForumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forum);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(DiscussionForumActivity.this, CommunityFrontPageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        Button btnNewPost = findViewById(R.id.btnNewPost);
        btnNewPost.setOnClickListener(v -> {
            Intent intent = new Intent(DiscussionForumActivity.this, NewPostActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewDiscussion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Repository
        DiscussionRepository discussionRepository = new DiscussionRepository();

        // Fetch data and set the adapter
        List<DiscussionPost> discussionPostList = discussionRepository.getDiscussionPosts();
        DiscussionAdapter discussionAdapter = new DiscussionAdapter(discussionPostList);
        recyclerView.setAdapter(discussionAdapter);
    }
}
