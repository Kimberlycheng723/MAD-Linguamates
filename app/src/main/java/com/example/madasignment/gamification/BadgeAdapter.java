package com.example.madasignment.gamification;

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

        holder.badgeName.setText(badge.getName());
        holder.badgeDescription.setText(badge.getDescription());
        holder.badgeImage.setImageResource(badge.getImageResId());

        if (badge.getState().equals("locked")) {
            holder.badgeProgress.setVisibility(View.GONE);
        } else if (badge.getState().equals("in_progress")) {
            holder.badgeProgress.setVisibility(View.VISIBLE);
            holder.badgeProgress.setText("In Progress");
        } else if (badge.getState().equals("completed")) {
            holder.badgeProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return badgeList.size();
    }

    public static class BadgeViewHolder extends RecyclerView.ViewHolder {
        ImageView badgeImage;
        TextView badgeName, badgeDescription, badgeProgress;

        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
            badgeImage = itemView.findViewById(R.id.badgeImage);
            badgeName = itemView.findViewById(R.id.badgeName);
            badgeDescription = itemView.findViewById(R.id.badgeDescription);
            badgeProgress = itemView.findViewById(R.id.badgeProgress);
        }
    }
}
