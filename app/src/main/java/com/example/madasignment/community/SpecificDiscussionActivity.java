package com.example.madasignment.community;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.ArrayList;
import java.util.List;

public class SpecificDiscussionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReplies;
    private RepliesAdapter repliesAdapter;
    private List<Reply> replyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_discussion);

        // Initialize RecyclerView
        recyclerViewReplies = findViewById(R.id.recyclerViewReplies);
        recyclerViewReplies.setLayoutManager(new LinearLayoutManager(this));

        // Get data from Intent
        String postId = getIntent().getStringExtra("post_id");
        String postTitle = getIntent().getStringExtra("post_title");
        String postContent = getIntent().getStringExtra("post_content");

        // Set data to views
//        TextView tvPostTitle = findViewById(R.id.tvPostContent);
        TextView tvPostContent = findViewById(R.id.tvPostContent);

//        tvPostTitle.setText(postTitle);
        tvPostContent.setText(postContent);

        // Sample data for replies
        replyList = new ArrayList<>();
        replyList.add(new Reply("John", "This is a great topic! I totally agree."));
        replyList.add(new Reply("Mary", "Thanks for sharing. I learned something new today."));
        replyList.add(new Reply("Alex", "Could you elaborate more on this?"));

        // Set up adapter
        repliesAdapter = new RepliesAdapter(replyList);
        recyclerViewReplies.setAdapter(repliesAdapter);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Send reply
        findViewById(R.id.btnSendReply).setOnClickListener(v -> {
            // Add reply logic here
            // Example:
            replyList.add(new Reply("You", "This is a sample reply."));
            repliesAdapter.notifyDataSetChanged();
        });
    }
    private void setupReplies(String postId) {
        RecyclerView recyclerViewReplies = findViewById(R.id.recyclerViewReplies);
        recyclerViewReplies.setLayoutManager(new LinearLayoutManager(this));

        // Mock replies (replace with dynamic data if needed)
        List<Reply> replies = new ArrayList<>();
        replies.add(new Reply("User1", "This is a detailed reply to the post."));
        replies.add(new Reply("User2", "Thank you for sharing your thoughts."));
        replies.add(new Reply("User3", "I have a question about this topic."));

        RepliesAdapter repliesAdapter = new RepliesAdapter(replies);
        recyclerViewReplies.setAdapter(repliesAdapter);
    }
}
