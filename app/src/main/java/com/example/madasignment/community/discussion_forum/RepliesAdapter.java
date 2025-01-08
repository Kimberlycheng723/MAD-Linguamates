package com.example.madasignment.community.discussion_forum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.List;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.ReplyViewHolder> {

    private List<Reply> replyList;

    public RepliesAdapter(List<Reply> replyList) {
        this.replyList = replyList;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reply, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        Reply reply = replyList.get(position);

        // Bind data to views based on the correct format
        holder.tvReplyUserName.setText(reply.getUserName() != null ? reply.getUserName() : "Anonymous");
        holder.tvReplyContent.setText(reply.getContent() != null ? reply.getContent() : "No content available");
    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    public static class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView tvReplyUserName, tvReplyContent;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views with matching IDs from item_reply.xml
            tvReplyUserName = itemView.findViewById(R.id.tvReplyUserName);
            tvReplyContent = itemView.findViewById(R.id.tvReplyContent);
        }
    }
}
