package com.example.madasignment.gamification.leaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;
import com.example.madasignment.community.CommunityFrontPageActivity;
import com.example.madasignment.home.lesson_unit.lesson_unit.LessonUnit;
import com.example.madasignment.lessons.Module.module.Module;
import com.example.madasignment.profile.ProfilePageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
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
        setupBottomNavigation();
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
                        leaderboardList.add(new LeaderboardItem(0, username, xp, userId)); // Add userId here
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
        Collections.sort(leaderboardList, (o1, o2) -> Integer.compare(o2.getXp(), o1.getXp()));

        DatabaseReference userStatsRef = FirebaseDatabase.getInstance().getReference("UserStats");

        // Assign ranks and update `currentLeague` in the database
        for (int i = 0; i < leaderboardList.size(); i++) {
            final int rank = i + 1; // Create a final variable for the rank
            LeaderboardItem item = leaderboardList.get(i);
            item.setRank(rank);

            // Update the user's `currentLeague` in the database
            userStatsRef.child(item.getUserId()).child("currentLeague").setValue(rank)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("LeaderboardUpdate", "Updated rank for user: " + item.getUsername());
                        } else {
                            Log.e("LeaderboardUpdate", "Failed to update rank for user: " + item.getUsername());
                        }
                    });
        }

        // Update RecyclerView
        leaderboardAdapter.notifyDataSetChanged();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(LeaderboardActivity.this, LessonUnit.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_lessons) {
                Intent intent = new Intent(LeaderboardActivity.this, Module.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_progress) {
                return true;
            } else if (id == R.id.nav_forum) {
                Intent intent = new Intent(LeaderboardActivity.this, CommunityFrontPageActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(LeaderboardActivity.this, ProfilePageActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Set the current selected item
        bottomNavigationView.setSelectedItemId(R.id.nav_progress);
    }
}
