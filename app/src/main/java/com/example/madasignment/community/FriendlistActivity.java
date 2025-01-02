package com.example.madasignment.community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.ArrayList;
import java.util.List;

public class FriendlistActivity extends AppCompatActivity {

    private RecyclerView recyclerFriendlist;
    private FriendlistAdapter adapter;
    private List<Friend> friendList;

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

        // Set RecyclerView
        recyclerFriendlist.setLayoutManager(new LinearLayoutManager(this));
        friendList = new ArrayList<>();
        loadMockData(); // Load sample data
        adapter = new FriendlistAdapter(friendList);
        recyclerFriendlist.setAdapter(adapter);

        // Add Friend Action (placeholder)
        btnAddFriend.setOnClickListener(v -> {
            Intent intent = new Intent(FriendlistActivity.this, AddFriendActivity.class);
            startActivity(intent);
        });
    }

    private void loadMockData() {
        friendList.add(new Friend("John Doe", 1200, R.drawable.ic_profile));
        friendList.add(new Friend("Jane Smith", 950, R.drawable.ic_profile));
        friendList.add(new Friend("Emily Davis", 800, R.drawable.ic_profile));
        friendList.add(new Friend("Chris Brown", 500, R.drawable.ic_profile));
    }
}
