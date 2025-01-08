package com.example.madasignment.community.discussion_forum;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder> {

    private static final String TAG = "DiscussionAdapter";
    private Context context;
    private List<DiscussionPost> discussionPostList = new ArrayList<>();

    public DiscussionAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DiscussionPost> posts) {
        discussionPostList.clear();
        if (posts != null) {
            discussionPostList.addAll(posts);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiscussionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_discussion_post, parent, false);
        return new DiscussionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionViewHolder holder, int position) {
        DiscussionPost post = discussionPostList.get(position);

        try {
            // Bind data to views
            holder.userName.setText(post.getUserName());
            holder.tvTitle.setText(post.getTitle());
            holder.tvContent.setText(post.getContent());
            holder.tvTimestamp.setText(post.getTimestamp());

            DatabaseReference repliesRef = FirebaseDatabase.getInstance()
                    .getReference("Replies")
                    .child(post.getPostId()); // Use postId as discussionId

            repliesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long answerCount = snapshot.getChildrenCount();
                    holder.answersCount.setText(answerCount + " answers");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    holder.answersCount.setText("0 answers");
                }
            });


            // Log bindings
            Log.d(TAG, "Binding post: " + post.getTitle());
        } catch (NullPointerException e) {
            Log.e(TAG, "View binding error: " + e.getMessage(), e);
        }

        // Set click listener on the entire itemView for navigation
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SpecificDiscussionActivity.class);
            intent.putExtra("discussionId", post.getPostId()); // Pass discussionId
            context.startActivity(intent);
        });

        // Retain functionality for btnReport
        holder.btnReport.setOnClickListener(v -> {
            Log.d(TAG, "Report button clicked for postId: " + post.getPostId());
            Intent intent = new Intent(context, ReportDiscussionActivity.class);
            intent.putExtra("discussionId", post.getPostId()); // Pass the discussion ID
            intent.putExtra("discussionTitle", post.getTitle()); // Optionally pass the title for context
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return discussionPostList.size();
    }

    public static class DiscussionViewHolder extends RecyclerView.ViewHolder {
        TextView userName, tvTitle, tvContent, tvTimestamp, answersCount;
        Button btnReport;

        public DiscussionViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views with correct IDs
            userName = itemView.findViewById(R.id.userName);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            answersCount = itemView.findViewById(R.id.answersCount); // Ensure ID matches your layout
            btnReport = itemView.findViewById(R.id.btnReport);
        }
    }
}
