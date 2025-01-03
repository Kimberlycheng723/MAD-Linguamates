package com.example.madasignment.community;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;
import com.example.madasignment.community.ReportDiscussionActivity;
import com.example.madasignment.community.SpecificDiscussionActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder> {
    private Context context;
    private List<DocumentSnapshot> discussionPostSnapshots = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DiscussionAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DocumentSnapshot> snapshots) {
        discussionPostSnapshots = snapshots;
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
        DocumentSnapshot snapshot = discussionPostSnapshots.get(position);
        String title = snapshot.getString("title");
        String content = snapshot.getString("content");
        String userName = snapshot.getString("UserName");

        holder.tvTitle.setText(title);
        holder.tvContent.setText(content);
        holder.tvUserName.setText(userName);

        // Button Actions
        holder.btnAnswer.setOnClickListener(v -> {
            Intent intent = new Intent(context, SpecificDiscussionActivity.class);
            intent.putExtra("discussionId", snapshot.getId()); // Pass Firestore Document ID
            context.startActivity(intent);
        });

        holder.btnReport.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDiscussionActivity.class);
            intent.putExtra("discussionId", snapshot.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return discussionPostSnapshots.size();
    }

    public static class DiscussionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvUserName;
        Button btnAnswer, btnReport;

        public DiscussionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btnAnswer = itemView.findViewById(R.id.btnAnswer);
            btnReport = itemView.findViewById(R.id.btnReport);
        }
    }
}
