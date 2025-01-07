package com.example.madasignment.community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

public class AddFriendActivity extends AppCompatActivity {

    private AddFriendAdapter adapter;
    private List<Friend> friendList;
    private EditText searchFriendInput;

    private DatabaseReference usersRef;
    private DatabaseReference friendRequestsRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_add_friend);

        // Initialize Views
        RecyclerView recyclerView = findViewById(R.id.recyclerAddFriend);
        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnSearch = findViewById(R.id.btnSearch);
        searchFriendInput = findViewById(R.id.searchFriendInput);

        // Set Back Button Functionality
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(AddFriendActivity.this, FriendlistActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Initialize Firebase References
        usersRef = FirebaseDatabase.getInstance().getReference("User");
        friendRequestsRef = FirebaseDatabase.getInstance().getReference("FriendRequests");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendList = new ArrayList<>();
        adapter = new AddFriendAdapter(this, friendList, this::sendFriendRequest);
        recyclerView.setAdapter(adapter);


        // Load Users from Firebase
        loadUsers();

        // Search Button Functionality
        btnSearch.setOnClickListener(v -> {
            String query = searchFriendInput.getText().toString().trim();
            filterFriends(query);
        });
    }

    private void loadUsers() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String id = userSnapshot.getKey();
                    String name = userSnapshot.child("name").getValue(String.class);

                    if (name != null && !id.equals(currentUserId)) {
                        friendList.add(new Friend(id, name));
                        Log.d("AddFriendActivity", "Loaded user: " + id + " - " + name);
                    }
                }
                if (friendList.isEmpty()) {
                    Toast.makeText(AddFriendActivity.this, "No users available to add.", Toast.LENGTH_SHORT).show();
                }
                adapter.updateList(friendList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AddFriendActivity", "Failed to load users: " + error.getMessage());
                Toast.makeText(AddFriendActivity.this, "Failed to load users: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendFriendRequest(String receiverId) {
        friendRequestsRef.child(receiverId).child(currentUserId).setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Friend request sent!", Toast.LENGTH_SHORT).show();
                        Log.d("AddFriendActivity", "Friend request sent to userId: " + receiverId);
                    } else {
                        Toast.makeText(this, "Failed to send friend request.", Toast.LENGTH_SHORT).show();
                        Log.e("AddFriendActivity", "Error sending friend request to userId: " + receiverId, task.getException());
                    }
                });
    }

    private void filterFriends(String query) {
        List<Friend> filteredList = new ArrayList<>();
        for (Friend friend : friendList) {
            if (friend.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(friend);
            }
        }
        adapter.updateList(filteredList);
    }
}