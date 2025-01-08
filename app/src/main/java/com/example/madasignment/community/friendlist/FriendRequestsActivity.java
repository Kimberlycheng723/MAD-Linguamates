package com.example.madasignment.community.friendlist;

import android.content.Intent;
import android.os.Bundle;
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

public class FriendRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendRequestAdapter adapter;
    private List<Friend> requestList;
    private DatabaseReference friendRequestsRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_friend_requests);

        // Initialize Firebase
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendRequestsRef = FirebaseDatabase.getInstance().getReference("FriendRequests").child(currentUserId);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerFriendRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestList = new ArrayList<>();
        adapter = new FriendRequestAdapter(this, requestList);
        recyclerView.setAdapter(adapter);

        // Load Friend Requests
        loadFriendRequests();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(FriendRequestsActivity.this, FriendlistActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadFriendRequests() {
        friendRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    String requesterId = requestSnapshot.getKey();
                    if (requesterId != null) {
                        // Fetch requester's name
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(requesterId);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                String name = userSnapshot.child("name").getValue(String.class);
                                requestList.add(new Friend(requesterId, name != null ? name : "Unknown"));
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(FriendRequestsActivity.this, "Failed to fetch friend details.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FriendRequestsActivity.this, "Failed to load friend requests.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}