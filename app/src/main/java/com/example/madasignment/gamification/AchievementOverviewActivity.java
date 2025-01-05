package com.example.madasignment.gamification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AchievementOverviewActivity extends AppCompatActivity {

    private TextView streakCounterValue; // TextView to display the current streak
    private TextView badgesEarnedValue; // TextView to display the badges earned
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements_overview);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-linguamates-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseRef = database.getReference("UserStats");

        // Initialize UI elements
        streakCounterValue = findViewById(R.id.streakCounterValue);
        badgesEarnedValue = findViewById(R.id.badgesEarnedValue); // TextView for badges earned
        Button dailyStreakButton = findViewById(R.id.dailyStreakButton);
        Button checkLeaderboardButton = findViewById(R.id.checkLeaderboardButton);
        Button viewAllBadgesButton = findViewById(R.id.viewAllBadgesButton);

        // Set click listeners for navigation buttons
        dailyStreakButton.setOnClickListener(v -> {
            Intent intent = new Intent(AchievementOverviewActivity.this, DailyStreakActivity.class);
            startActivity(intent);
        });

        checkLeaderboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(AchievementOverviewActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });

        viewAllBadgesButton.setOnClickListener(v -> {
            Intent intent = new Intent(AchievementOverviewActivity.this, MyBadgesActivity.class);
            startActivity(intent);
        });

        // Handle Bottom Navigation
        setupBottomNavigation();

        // Fetch and display the current streak
        displayCurrentStreak();

        // Fetch and display the total badges earned
        displayBadgesEarned();
    }

    private void displayCurrentStreak() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            Log.e("DisplayStreak", "User authentication failed");
            return;
        }

        DatabaseReference userRef = databaseRef.child(userId).child("currentStreak");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long currentStreak = snapshot.getValue(Long.class) != null ? snapshot.getValue(Long.class) : 0;
                    streakCounterValue.setText(String.valueOf(currentStreak)); // Display the streak
                    Log.d("DisplayStreak", "Current streak displayed: " + currentStreak);
                } else {
                    streakCounterValue.setText("0"); // Default value if streak doesn't exist
                    Log.d("DisplayStreak", "No streak data found, displaying 0.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseError", "Database read failed: " + error.getMessage());
            }
        });
    }

    private void displayBadgesEarned() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            Log.e("DisplayBadges", "User authentication failed");
            return;
        }

        DatabaseReference badgesRef = databaseRef.child(userId).child("badges");

        badgesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int badgesEarned = 0;

                    for (DataSnapshot badgeSnapshot : snapshot.getChildren()) {
                        String state = badgeSnapshot.child("state").getValue(String.class);
                        if ("completed".equalsIgnoreCase(state)) {
                            badgesEarned++;
                        }
                    }

                    badgesEarnedValue.setText(String.valueOf(badgesEarned)); // Display the badges earned
                    Log.d("DisplayBadges", "Total badges earned: " + badgesEarned);
                } else {
                    badgesEarnedValue.setText("0"); // Default value if no badges found
                    Log.d("DisplayBadges", "No badges data found, displaying 0.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseError", "Database read failed: " + error.getMessage());
            }
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Navigate to Home
                return true;
            } else if (id == R.id.nav_lessons) {
                // Navigate to Lessons
                return true;
            } else if (id == R.id.nav_progress) {
                // Navigate to Progress
                return true;
            } else if (id == R.id.nav_forum) {
                // Navigate to Forum
                return true;
            } else if (id == R.id.nav_profile) {
                // Navigate to Profile
                return true;
            }
            return false;
        });

        // Set Default Selected Item
        bottomNavigationView.setSelectedItemId(R.id.nav_progress);
    }
}
