package com.example.madasignment.community;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.ViewHolder> {

    private static final String TAG = "AddFriendAdapter";
    private final Context context;
    private List<Friend> friendList;
    private final FriendRequestCallback friendRequestCallback;

    public AddFriendAdapter(Context context, List<Friend> friendList, FriendRequestCallback callback) {
        this.context = context;
        this.friendList = friendList;
        this.friendRequestCallback = callback;
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
        Log.d(TAG, "Binding ViewHolder for userId: " + friend.getUserId() + ", name: " + friend.getName());

        holder.btnAdd.setOnClickListener(v -> {
            Log.d(TAG, "Add Friend Button Clicked for userId: " + friend.getUserId());
            if (friendRequestCallback != null) {
                friendRequestCallback.sendRequest(friend.getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView friendName;
        Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendName);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }

    // Callback interface for sending friend requests
    public interface FriendRequestCallback {
        void sendRequest(String receiverId);
    }
}
