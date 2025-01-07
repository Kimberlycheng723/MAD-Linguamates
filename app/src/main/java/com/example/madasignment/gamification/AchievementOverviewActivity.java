package com.example.madasignment.gamification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.example.madasignment.community.CommunityFrontPageActivity;
import com.example.madasignment.gamification.badge.MyBadgesActivity;
import com.example.madasignment.gamification.dailystreak.DailyStreakActivity;
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

public class AchievementOverviewActivity extends AppCompatActivity {

    private TextView streakCounterValue, rankNumber;
    private TextView badgesEarnedValue;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef, friendsRef, userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements_overview);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-linguamates-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseRef = database.getReference("UserStats");
        friendsRef = database.getReference("Friends");
        userRef = database.getReference("User");

        // Initialize UI elements
        streakCounterValue = findViewById(R.id.streakCounterValue);
        badgesEarnedValue = findViewById(R.id.badgesEarnedValue);
        rankNumber = findViewById(R.id.rankNumber);

        Button dailyStreakButton = findViewById(R.id.dailyStreakButton);
        Button checkLeaderboardButton = findViewById(R.id.checkLeaderboardButton);
        Button viewAllBadgesButton = findViewById(R.id.viewAllBadgesButton);
        ImageView upButton = findViewById(R.id.backArrow);

        upButton.setOnClickListener(v -> finish());


        dailyStreakButton.setOnClickListener(v -> {
            Intent intent = new Intent(AchievementOverviewActivity.this, DailyStreakActivity.class);
            startActivity(intent);
        });

        checkLeaderboardButton.setOnClickListener(v -> {
            Log.d("Leaderboard", "Navigating to Leaderboard Activity");
            Intent intent = new Intent(AchievementOverviewActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });

        viewAllBadgesButton.setOnClickListener(v -> {
            Log.d("Leaderboard", "Navigating to Leaderboard Activity");
            Intent intent = new Intent(AchievementOverviewActivity.this, MyBadgesActivity.class);
            startActivity(intent);
        });


        setupBottomNavigation();

        displayCurrentStreak();
        displayBadgesEarned();
        fetchAndStoreUserRank();
    }

    private void displayCurrentStreak() {
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference userRef = databaseRef.child(userId).child("currentStreak");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long currentStreak = snapshot.exists() ? snapshot.getValue(Long.class) : 0;
                streakCounterValue.setText(String.valueOf(currentStreak));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("DisplayStreak", "Failed to fetch streak: " + error.getMessage());
            }
        });
    }

    private void displayBadgesEarned() {
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference badgesRef = databaseRef.child(userId).child("badges");

        badgesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int badgesEarned = 0;
                for (DataSnapshot badgeSnapshot : snapshot.getChildren()) {
                    if ("completed".equalsIgnoreCase(badgeSnapshot.child("state").getValue(String.class))) {
                        badgesEarned++;
                    }
                }
                badgesEarnedValue.setText(String.valueOf(badgesEarned));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("DisplayBadges", "Failed to fetch badges: " + error.getMessage());
            }
        });
    }

    private void fetchAndStoreUserRank() {
        String currentUserId = auth.getCurrentUser().getUid();
        friendsRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot friendsSnapshot) {
                List<String> userIds = new ArrayList<>();
                for (DataSnapshot friendSnapshot : friendsSnapshot.getChildren()) {
                    userIds.add(friendSnapshot.getKey());
                }
                userIds.add(currentUserId); // Include the current user

                fetchUserDetails(userIds, currentUserId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FetchUserRank", "Failed to fetch friends: " + error.getMessage());
            }
        });
    }

    private void fetchUserDetails(List<String> userIds, String currentUserId) {
        List<LeaderboardItem> leaderboardList = new ArrayList<>();

        for (String userId : userIds) {
            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                    String username = userSnapshot.child("name").getValue(String.class);
                    Integer xp = userSnapshot.child("xp").getValue(Integer.class);

                    if (username != null && xp != null) {
                        leaderboardList.add(new LeaderboardItem(0, username, xp, userId));
                    }

                    if (leaderboardList.size() == userIds.size()) {
                        calculateRankAndStoreInDatabase(leaderboardList, currentUserId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FetchUserDetails", "Failed to fetch user details: " + error.getMessage());
                }
            });
        }
    }

    private void calculateRankAndStoreInDatabase(List<LeaderboardItem> leaderboardList, String currentUserId) {
        if (leaderboardList.isEmpty()) {
            updateStarWithRank("--");
            return;
        }

        Collections.sort(leaderboardList, (o1, o2) -> Integer.compare(o2.getXp(), o1.getXp()));

        for (int i = 0; i < leaderboardList.size(); i++) {
            LeaderboardItem item = leaderboardList.get(i);
            item.setRank(i + 1);

            if (item.getUserId().equals(currentUserId)) {
                int rank = i + 1;
                updateStarWithRank(String.valueOf(rank));
                storeRankInDatabase(rank);
                return;
            }
        }
    }

    private void updateStarWithRank(String rank) {
        rankNumber.setText(rank);
    }

    private void storeRankInDatabase(int rank) {
        String currentUserId = auth.getCurrentUser().getUid();
        databaseRef.child(currentUserId).child("currentLeague").setValue(rank)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("StoreRank", "Rank stored successfully.");
                    } else {
                        Log.e("StoreRank", "Failed to store rank: " + task.getException().getMessage());
                    }
                });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(AchievementOverviewActivity.this, LessonUnit.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_lessons) {
                Intent intent = new Intent(AchievementOverviewActivity.this, Module.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_progress) {
                return true;
            } else if (id == R.id.nav_forum) {
                Intent intent = new Intent(AchievementOverviewActivity.this, CommunityFrontPageActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(AchievementOverviewActivity.this, ProfilePageActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Set the current selected item
        bottomNavigationView.setSelectedItemId(R.id.nav_progress);
    }
}
