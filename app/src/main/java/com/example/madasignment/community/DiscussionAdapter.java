package com.example.madasignment.community;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.List;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder> {

    private List<DiscussionPost> discussionPostList;

    public DiscussionAdapter(List<DiscussionPost> discussionPostList) {
        this.discussionPostList = discussionPostList;
    }

    public void setData(List<DiscussionPost> newDiscussionPosts) {
        this.discussionPostList = newDiscussionPosts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiscussionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discussion_post, parent, false);
        return new DiscussionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionViewHolder holder, int position) {
        DiscussionPost post = discussionPostList.get(position);

        // Set data to views
        holder.userName.setText(post.getUserName());
        holder.postContent.setText(post.getContent());
        holder.answersCount.setText(post.getAnswersCount() + " answers");



        // Navigate to SpecificDiscussionActivity when the "Answer" button is clicked
        holder.btnAnswer.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), SpecificDiscussionActivity.class);
            intent.putExtra("post_UserName", post.getUserName());
            intent.putExtra("post_content", post.getContent());
            holder.itemView.getContext().startActivity(intent);
        });

        holder.btnReport.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ReportDiscussionActivity.class);
            intent.putExtra("user_name", post.getUserName());
            intent.putExtra("post_content", post.getContent());
            holder.itemView.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return discussionPostList.size();
    }

    public static class DiscussionViewHolder extends RecyclerView.ViewHolder {
        TextView userName, postContent, answersCount;
        ImageView profileImage;
        Button btnAnswer, btnReport;

        public DiscussionViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            postContent = itemView.findViewById(R.id.postContent);
            answersCount = itemView.findViewById(R.id.answersCount);
            profileImage = itemView.findViewById(R.id.profileImage);
            btnAnswer = itemView.findViewById(R.id.btnAnswer);
            btnReport = itemView.findViewById(R.id.btnReport);
        }
    }
}
