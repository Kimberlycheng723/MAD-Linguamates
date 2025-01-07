package com.example.madasignment.community;

import android.content.Context;
import android.content.Intent;
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

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    private Context context;
    private List<Friend> requestList;
    private String currentUserId;

    public FriendRequestAdapter(Context context, List<Friend> requestList) {
        this.context = context;
        this.requestList = requestList;
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = requestList.get(position);
        holder.friendName.setText(friend.getName());

        holder.btnAccept.setOnClickListener(v -> acceptFriendRequest(friend.getUserId()));
        holder.btnReject.setOnClickListener(v -> rejectFriendRequest(friend.getUserId()));
    }

    private void acceptFriendRequest(String requesterId) {
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference("Friends");
        DatabaseReference friendRequestsRef = FirebaseDatabase.getInstance().getReference("FriendRequests").child(currentUserId);

        friendsRef.child(currentUserId).child(requesterId).setValue(true);
        friendsRef.child(requesterId).child(currentUserId).setValue(true);

        friendRequestsRef.child(requesterId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Friend request accepted!", Toast.LENGTH_SHORT).show();
                // Navigate back to FriendlistActivity
                Intent intent = new Intent(context, FriendlistActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Failed to accept friend request.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void rejectFriendRequest(String requesterId) {
        DatabaseReference friendRequestsRef = FirebaseDatabase.getInstance().getReference("FriendRequests").child(currentUserId);

        friendRequestsRef.child(requesterId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Friend request rejected.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to reject friend request.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView friendName;
        Button btnAccept, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}