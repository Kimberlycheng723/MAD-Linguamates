package com.example.madasignment.community;

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
        holder.tvUserName.setText(reply.getUserName());
        holder.tvReplyContent.setText(reply.getContent());
    }


    @Override
    public int getItemCount() {
        return replyList.size();
    }

    public void updateReplies(List<Reply> replies) {
        this.replyList.clear();
        this.replyList.addAll(replies);
        notifyDataSetChanged();
    }

    static class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvReplyContent;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvReplyContent = itemView.findViewById(R.id.tvReplyContent);
        }
    }
}
