package com.example.madasignment.gamification.badge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DiffUtil;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBadgesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BadgeAdapter badgeAdapter;
    private List<Badge> badgeList;
    private DatabaseReference badgesRef;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements_my_badges);
        ImageButton upButton = findViewById(R.id.upButton);

        upButton.setOnClickListener(v -> {
            // Navigate back
            onBackPressed();
        });
        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns in the grid
        badgeList = new ArrayList<>();
        badgeAdapter = new BadgeAdapter(this, badgeList);
        recyclerView.setAdapter(badgeAdapter);

        // Initialize Firebase reference
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (userId == null) {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        badgesRef = FirebaseDatabase.getInstance()
                .getReference("UserStats")
                .child(userId)
                .child("badges");

        loadBadges();
        setupBottomNavigation();
    }

    private void loadBadges() {
        progressBar.setVisibility(View.VISIBLE);

        badgesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Badge> newBadgeList = new ArrayList<>();

                // Create a map for badge names to their corresponding badges
                Map<String, Badge> badgeMap = new HashMap<>();
                for (DataSnapshot badgeSnapshot : dataSnapshot.getChildren()) {
                    String name = badgeSnapshot.getKey();
                    BadgeFirebaseModel badgeModel = badgeSnapshot.getValue(BadgeFirebaseModel.class);

                    if (badgeModel != null && name != null) {
                        Badge badge = new Badge(
                                name,
                                getBadgeDescription(name),
                                badgeModel.getState(),
                                getBadgeImage(badgeModel.getState()),
                                badgeModel.getGoal(),
                                badgeModel.getProgress()
                        );
                        badgeMap.put(name, badge);
                    }
                }

                // Add badges in the desired order
                addBadgeToList(newBadgeList, badgeMap, "First Lesson");
                addBadgeToList(newBadgeList, badgeMap, "3 Lessons");
                addBadgeToList(newBadgeList, badgeMap, "5 Lessons");
                addBadgeToList(newBadgeList, badgeMap, "First Test");
                addBadgeToList(newBadgeList, badgeMap, "3 Tests");
                addBadgeToList(newBadgeList, badgeMap, "5 Tests");
                addBadgeToList(newBadgeList, badgeMap, "3-Day Streak");
                addBadgeToList(newBadgeList, badgeMap, "5-Day Streak");
                addBadgeToList(newBadgeList, badgeMap, "7-Day Streak");

                updateBadges(newBadgeList);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MyBadgesActivity", "Error fetching badges: " + databaseError.getMessage());
                Toast.makeText(MyBadgesActivity.this, "Failed to load badges.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void addBadgeToList(List<Badge> badgeList, Map<String, Badge> badgeMap, String badgeName) {
        if (badgeMap.containsKey(badgeName)) {
            badgeList.add(badgeMap.get(badgeName));
        } else {
            Log.e("MyBadgesActivity", "Badge not found: " + badgeName);
        }
    }


    private void updateBadges(List<Badge> newBadgeList) {
        BadgeDiffCallback diffCallback = new BadgeDiffCallback(badgeList, newBadgeList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        badgeList.clear();
        badgeList.addAll(newBadgeList);
        diffResult.dispatchUpdatesTo(badgeAdapter);
    }

    private int getBadgeImage(String state) {
        switch (state) {
            case "completed":
                return R.drawable.badge_completed;
            case "in_progress":
                return R.drawable.badge_in_progress;
            default:
                return R.drawable.badge_locked;
        }
    }

    private String getBadgeDescription(String badgeName) {
        switch (badgeName) {
            case "First Lesson":
                return "Complete first lesson";
            case "3 Lessons":
                return "Complete 3 lessons";
            case "5 Lessons":
                return "Complete 5 lessons";
            case "First Test":
                return "Complete the first test";
            case "3 Tests":
                return "Complete 3 tests";
            case "5 Tests":
                return "Complete 5 tests";
            case "3-Day Streak":
                return "Get a 3-Day Streak";
            case "5-Day Streak":
                return "Get a 5-Day Streak";
            case "7-Day Streak":
                return "Get a 7-Day Streak";
            default:
                return "No description available.";
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(MyBadgesActivity.this, LessonUnit.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_lessons) {
                Intent intent = new Intent(MyBadgesActivity.this, Module.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_progress) {
                return true;
            } else if (id == R.id.nav_forum) {
                Intent intent = new Intent(MyBadgesActivity.this, CommunityFrontPageActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(MyBadgesActivity.this, ProfilePageActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Set the current selected item
        bottomNavigationView.setSelectedItemId(R.id.nav_progress);
    }
}
