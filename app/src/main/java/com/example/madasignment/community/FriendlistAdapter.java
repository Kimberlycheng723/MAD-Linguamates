package com.example.madasignment.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.List;

public class FriendlistAdapter extends RecyclerView.Adapter<FriendlistAdapter.FriendViewHolder> {

    private List<Friend> friendList;

    public FriendlistAdapter(List<Friend> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friendList.get(position);

        // Set name and XP
        holder.tvName.setText(friend.getName());
        holder.tvXP.setText(friend.getXp() + " XP");

        // Remove image-related code
        // holder.ivProfile.setImageResource(R.drawable.ic_profile); // No image handling
    }


    @Override
    public int getItemCount() {
        return friendList.size();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tvName, tvXP;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvName = itemView.findViewById(R.id.tvFriendName);
            tvXP = itemView.findViewById(R.id.tvFriendXP);
        }
    }
}

