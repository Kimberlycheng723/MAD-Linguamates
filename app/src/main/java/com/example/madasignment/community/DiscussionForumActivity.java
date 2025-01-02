package com.example.madasignment.community;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.List;

public class DiscussionForumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DiscussionAdapter discussionAdapter;
    private DiscussionRepository discussionRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forum);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Finish the activity to return to the front page
            finish();
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewDiscussion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Repository
        discussionRepository = new DiscussionRepository();

        // Fetch data and set the adapter
        List<DiscussionPost> discussionPostList = discussionRepository.getDiscussionPosts();
        discussionAdapter = new DiscussionAdapter(discussionPostList);
        recyclerView.setAdapter(discussionAdapter);
    }
}
