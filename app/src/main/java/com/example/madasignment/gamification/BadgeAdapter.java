package com.example.madasignment.gamification;

import android.content.Context;
import android.util.Log;
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
            holder.badgeName.setText(badge.getName() != null ? badge.getName() : "Unknown Badge");
            holder.badgeDescription.setText(badge.getDescription() != null ? badge.getDescription() : "No description available");
            holder.badgeImage.setImageResource(badge.getImageResId() > 0 ? badge.getImageResId() : R.drawable.badge_locked);

            Log.d("BadgeAdapter", "Binding badge: " + (badge.getName() != null ? badge.getName() : "Null") + ", State: " + badge.getState());
        } else {
            Log.e("BadgeAdapter", "Null badge at position: " + position);
            holder.badgeName.setText("Unknown Badge");
            holder.badgeDescription.setText("No description available");
            holder.badgeImage.setImageResource(R.drawable.badge_locked);
        }
    }

    @Override
    public int getItemCount() {
        return badgeList.size();
    }

    public static class BadgeViewHolder extends RecyclerView.ViewHolder {
        ImageView badgeImage;
        TextView badgeName, badgeDescription;

        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
            badgeImage = itemView.findViewById(R.id.badgeImage);
            badgeName = itemView.findViewById(R.id.badgeName);
            badgeDescription = itemView.findViewById(R.id.badgeDescription);
        }
    }
}
