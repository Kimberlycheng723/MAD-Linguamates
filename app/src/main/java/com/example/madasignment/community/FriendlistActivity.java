package com.example.madasignment.community;

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
        setContentView(R.layout.activity_friendlist);

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
    }

    private void loadFriends() {
        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendList.clear();
                for (DataSnapshot friendSnapshot : snapshot.getChildren()) {
                    Friend friend = friendSnapshot.getValue(Friend.class);
                    if (friend != null) {
                        friendList.add(friend);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FriendlistActivity.this, "Failed to load friends", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
