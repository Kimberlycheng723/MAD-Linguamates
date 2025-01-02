package com.example.madasignment.community;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AddFriendAdapter adapter;
    private List<Friend> friendList;
    private EditText searchFriendInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        // Initialize Views
        recyclerView = findViewById(R.id.recyclerAddFriend);
        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnSearch = findViewById(R.id.btnSearch);
        searchFriendInput = findViewById(R.id.searchFriendInput);

        // Set Back Button Functionality
        btnBack.setOnClickListener(v -> finish());

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendList = new ArrayList<>();
        loadMockData(); // Load mock data
        adapter = new AddFriendAdapter(friendList);
        recyclerView.setAdapter(adapter);

        // Search Button Functionality
        btnSearch.setOnClickListener(v -> {
            String query = searchFriendInput.getText().toString().trim();
            filterFriends(query);
        });
    }

    private void loadMockData() {
        friendList.add(new Friend("John Doe", 1000, R.drawable.ic_profile));
        friendList.add(new Friend("Jane Smith", 800, R.drawable.ic_profile));
        friendList.add(new Friend("Emily Johnson", 1200, R.drawable.ic_profile));
        friendList.add(new Friend("Chris Brown", 500, R.drawable.ic_profile));
        friendList.add(new Friend("Sophia Davis", 700, R.drawable.ic_profile));
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
