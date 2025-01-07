package com.example.madasignment.gamification.badge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.List;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder> {

    private Context context;
    private List<Badge> badgeList;

    public BadgeAdapter(Context context, List<Badge> badgeList) {
        this.context = context;
        this.badgeList = badgeList;
    }

    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_badge, parent, false);
        return new BadgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeViewHolder holder, int position) {
        Badge badge = badgeList.get(position);

        if (badge != null) {
            holder.badgeName.setText(badge.getName());
            holder.badgeDescription.setText(badge.getDescription());
            holder.badgeStatus.setText(getBadgeStateDescription(badge.getState())); // Use the helper method

            int badgeImageRes = R.drawable.badge_locked; // Default to locked badge
            if ("completed".equals(badge.getState())) {
                badgeImageRes = R.drawable.badge_completed;
            } else if ("in_progress".equals(badge.getState())) {
                badgeImageRes = R.drawable.badge_in_progress;
            }
            holder.badgeImage.setImageResource(badgeImageRes);
        }
    }

    @Override
    public int getItemCount() {
        return badgeList.size();
    }

    public static class BadgeViewHolder extends RecyclerView.ViewHolder {
        ImageView badgeImage;
        TextView badgeName, badgeDescription, badgeStatus;

        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
            badgeImage = itemView.findViewById(R.id.badgeImage);
            badgeName = itemView.findViewById(R.id.badgeName);
            badgeDescription = itemView.findViewById(R.id.badgeDescription);
            badgeStatus = itemView.findViewById(R.id.badgeStatus);
        }
    }

    // Add this helper method here
    private String getBadgeStateDescription(String state) {
        switch (state) {
            case "completed":
                return "Completed";
            case "in_progress":
                return "In Progress";
            default:
                return "Locked";
        }
    }
}
