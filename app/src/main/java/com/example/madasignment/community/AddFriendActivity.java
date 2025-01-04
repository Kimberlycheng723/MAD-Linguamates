package com.example.madasignment.community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class AddFriendActivity extends AppCompatActivity {

    private AddFriendAdapter adapter;
    private List<Friend> friendList;
    private EditText searchFriendInput;

    private DatabaseReference usersRef;


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

        // Initialize Firebase Reference
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendList = new ArrayList<>();
        adapter = new AddFriendAdapter(this, friendList);
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
                    int xp = userSnapshot.child("xp").getValue(Integer.class) != null ?
                            userSnapshot.child("xp").getValue(Integer.class) : 0;

//                    String profileImage = userSnapshot.child("profileImage").getValue(String.class);

                    if (name != null) {
                        friendList.add(new Friend(id, name, xp));
                    }
                }
                adapter.updateList(friendList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddFriendActivity.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
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
