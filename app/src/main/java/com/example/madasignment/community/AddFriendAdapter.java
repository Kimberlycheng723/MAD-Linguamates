package com.example.madasignment.community;

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

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.ViewHolder> {

    private List<Friend> friendList;

    public AddFriendAdapter(List<Friend> friendList) {
        this.friendList = friendList;
    }

    public void updateList(List<Friend> newList) {
        this.friendList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        holder.friendName.setText(friend.getName());
        holder.friendXP.setText(friend.getXp() + " XP");
        holder.friendImage.setImageResource(friend.getProfileImage());

        holder.btnAdd.setOnClickListener(v -> {
            // Handle Add Friend Logic
            // E.g., send a friend request to the database or update UI
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView friendImage;
        TextView friendName, friendXP;
        Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendImage = itemView.findViewById(R.id.friendImage);
            friendName = itemView.findViewById(R.id.friendName);
            friendXP = itemView.findViewById(R.id.friendXP);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
