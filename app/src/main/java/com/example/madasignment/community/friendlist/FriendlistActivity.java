package com.example.madasignment.community.friendlist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendlistActivity extends AppCompatActivity {

    private RecyclerView recyclerFriendlist;
    private FriendlistAdapter adapter;
    private List<Friend> friendList;
    private DatabaseReference friendsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_friendlist);

        // Initialize Views
        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnAddFriend = findViewById(R.id.btnAddFriend);
        recyclerFriendlist = findViewById(R.id.recyclerFriendlist);

        // Set Back Button Functionality
        btnBack.setOnClickListener(v -> finish());

        // Initialize RecyclerView
        recyclerFriendlist.setLayoutManager(new LinearLayoutManager(this));
        friendList = new ArrayList<>();
        adapter = new FriendlistAdapter(friendList);
        recyclerFriendlist.setAdapter(adapter);

        // Initialize Firebase Reference
        friendsRef = FirebaseDatabase.getInstance().getReference("Friends");

        // Load Friend Data from Firebase
        loadFriends();

        // Add Friend Action (navigate to AddFriendActivity)
        btnAddFriend.setOnClickListener(v -> {
            Intent intent = new Intent(FriendlistActivity.this, AddFriendActivity.class);
            startActivity(intent);
        });
        Button btnViewFriendRequests = findViewById(R.id.btnViewFriendRequests);

        btnViewFriendRequests.setOnClickListener(v -> {
            Intent intent = new Intent(FriendlistActivity.this, FriendRequestsActivity.class);
            startActivity(intent);
        });

    }

    private void loadFriends() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendsRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendList.clear();
                for (DataSnapshot friendSnapshot : snapshot.getChildren()) {
                    String friendId = friendSnapshot.getKey();
                    if (friendId != null && !friendId.equals(currentUserId)) {
                        // Fetch friend's name
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(friendId);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                String name = userSnapshot.child("name").getValue(String.class);
                                if (name != null) {
                                    friendList.add(new Friend(friendId, name)); // Updated Friend model constructor
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(FriendlistActivity.this, "Failed to fetch friend details.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FriendlistActivity.this, "Failed to load friends.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}