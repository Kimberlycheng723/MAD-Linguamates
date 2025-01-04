package com.example.madasignment.community;

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

            // Log bindings
            Log.d(TAG, "Binding post: " + post.getTitle());
        } catch (NullPointerException e) {
            Log.e(TAG, "View binding error: " + e.getMessage(), e);
        }

        // Button actions
        holder.btnAnswer.setOnClickListener(v -> {
            Intent intent = new Intent(context, SpecificDiscussionActivity.class);
            intent.putExtra("discussionId", post.getPostId());
            context.startActivity(intent);
        });

        holder.btnReport.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDiscussionActivity.class);
            intent.putExtra("discussionId", post.getPostId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return discussionPostList.size();
    }

    public static class DiscussionViewHolder extends RecyclerView.ViewHolder {
        TextView userName, tvTitle, tvContent, tvTimestamp;
        Button btnAnswer, btnReport;

        public DiscussionViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views with correct IDs
            userName = itemView.findViewById(R.id.userName);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            btnAnswer = itemView.findViewById(R.id.btnAnswer);
            btnReport = itemView.findViewById(R.id.btnReport);
        }
    }
}
