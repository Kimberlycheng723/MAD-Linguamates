package com.example.madasignment.gamification;

import android.os.Bundle;
import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<LeaderboardItem> leaderboardList;

    private DatabaseReference friendsRef;
    private DatabaseReference userRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendsRef = FirebaseDatabase.getInstance().getReference("Friends").child(currentUserId);
        userRef = FirebaseDatabase.getInstance().getReference("User");

        leaderboardList = new ArrayList<>();
        leaderboardAdapter = new LeaderboardAdapter(this, leaderboardList);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);

        loadLeaderboard();

        // Handle Back Arrow
        findViewById(R.id.backArrow).setOnClickListener(view -> finish());
    }

    private void loadLeaderboard() {
        friendsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot friendsSnapshot) {
                List<String> friendIds = new ArrayList<>();
                for (DataSnapshot friendSnapshot : friendsSnapshot.getChildren()) {
                    friendIds.add(friendSnapshot.getKey());
                }

                // Include the current user's ID in the leaderboard
                friendIds.add(currentUserId);

                fetchUserDetails(friendIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LeaderboardActivity.this, "Failed to load friends.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserDetails(List<String> userIds) {
        leaderboardList.clear();

        for (String userId : userIds) {
            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                    String username = userSnapshot.child("name").getValue(String.class);
                    Integer xp = userSnapshot.child("xp").getValue(Integer.class);

                    if (username != null && xp != null) {
                        leaderboardList.add(new LeaderboardItem(0, username, xp));
                    }

                    if (leaderboardList.size() == userIds.size()) {
                        sortLeaderboardAndUpdateUI();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LeaderboardActivity.this, "Failed to fetch user details.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sortLeaderboardAndUpdateUI() {
        // Sort leaderboard by XP in descending order
        Collections.sort(leaderboardList, new Comparator<LeaderboardItem>() {
            @Override
            public int compare(LeaderboardItem o1, LeaderboardItem o2) {
                return Integer.compare(o2.getXp(), o1.getXp());
            }
        });

        // Assign ranks based on sorted order
        for (int i = 0; i < leaderboardList.size(); i++) {
            leaderboardList.get(i).setRank(i + 1);
        }

        // Update RecyclerView
        leaderboardAdapter.notifyDataSetChanged();
    }
}
