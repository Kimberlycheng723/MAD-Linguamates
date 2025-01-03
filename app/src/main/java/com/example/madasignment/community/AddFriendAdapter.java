package com.example.madasignment.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.ViewHolder> {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String currentUserId = auth.getCurrentUser().getUid();

    private Context context;
    private List<Friend> friendList;

    public AddFriendAdapter(Context context, List<Friend> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

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
        // holder.friendImage.setImageResource(friend.getProfileImage()); // Uncomment when profile images are implemented

        holder.btnAdd.setOnClickListener(v -> {
            sendFriendRequest(friend.getId());
        });
    }

    private void sendFriendRequest(String receiverId) {
        DatabaseReference friendRequestsRef = FirebaseDatabase.getInstance().getReference("FriendRequests");
        friendRequestsRef.child(receiverId).child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(context, "Friend request already sent!", Toast.LENGTH_SHORT).show();
                } else {
                    friendRequestsRef.child(receiverId).child(currentUserId).setValue(true)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Friend request sent!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Failed to send request.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error checking friend request.", Toast.LENGTH_SHORT).show();
            }
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
