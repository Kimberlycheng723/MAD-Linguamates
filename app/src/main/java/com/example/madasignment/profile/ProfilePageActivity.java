package com.example.madasignment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.madasignment.gamification.AchievementOverviewActivity;
import com.example.madasignment.lessons.Module.module.Module;
import com.example.madasignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.madasignment.community.CommunityFrontPageActivity;
import com.example.madasignment.home.lesson_unit.lesson_unit.LessonUnit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfilePageActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private TextView tvStreakValue, tvLeagueValue, tvBadgesValue, tvLanguageValue;
    private TextView tvnameValue, tvusernameValue, tvBioValue;

    private TextView tvBadge1, tvBadge2;
    private BottomNavigationView bottomNavigationView; // Declare as a private variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-linguamates-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference();

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.nav_blue));
        setupBottomNavigation();

        // Initialize buttons
        Button btnSet = findViewById(R.id.btn_set_pp);
        Button editPage = findViewById(R.id.Bt_editProfile);

        btnSet.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePageActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        editPage.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePageActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Initialize text views
        tvStreakValue = findViewById(R.id.tv_streakValue_pp);
        tvLeagueValue = findViewById(R.id.tv_leagueValue_pp);
        tvLanguageValue = findViewById(R.id.tv_languageValue_pp);
        tvBadgesValue = findViewById(R.id.tv_badgesValue_pp);

        tvnameValue = findViewById(R.id.tv_name_pp);
        tvusernameValue = findViewById(R.id.tv_username_pp);
        tvBioValue = findViewById(R.id.tv_bio_pp);

        tvBadge1 = findViewById(R.id.tv_badge1_pp);
        tvBadge2 = findViewById(R.id.tv_badge2_pp);

        // Fetch and Display Data
        fetchUserData();
    }

    private void fetchUserData() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            try {
                databaseReference.child("UserStats").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            String streak = snapshot.child("currentStreak").getValue() != null
                                    ? String.valueOf(snapshot.child("currentStreak").getValue())
                                    : "0";
                            tvStreakValue.setText(streak);

                            String league = snapshot.child("currentLeague").getValue() != null
                                    ? String.valueOf(snapshot.child("currentLeague").getValue())
                                    : "0";
                            tvLeagueValue.setText(league);

                            String languages = snapshot.child("language").getValue(String.class);
                            if (languages != null && !languages.equals("None")) {
                                // Count the number of languages
                                int languageCount = languages.split(",").length;
                                tvLanguageValue.setText(String.valueOf(languageCount));
                            } else {
                                tvLanguageValue.setText("0");
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        Log.d("ProfilePageActivity", "Error: " + error.getMessage());
                    }
                });

                databaseReference.child("User").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            // Retrieve Data
                            String name = snapshot.child("name").getValue(String.class);
                            tvnameValue.setText(name != null ? name : "0");

                            String username = snapshot.child("username").getValue(String.class);
                            tvusernameValue.setText(username != null ? username : "0");

                            String bio = snapshot.child("Bio").getValue(String.class);
                            tvBioValue.setText(bio != null ? bio : "No bio available");

                            Log.d("ProfilePageActivity", "Snapshot: " + snapshot.toString());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle Database Error
                        Log.d("ProfilePageActivity", "Error: " + error.getMessage());
                        Toast.makeText(ProfilePageActivity.this, "Failed to load data. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

                databaseReference.child("UserStats").child(userId).child("badges").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            int completedBadgeCount = 0;
                            List<String> completedBadges = new ArrayList<>();

                            // Iterate over badges and filter the completed ones
                            for (DataSnapshot badgeSnapshot : snapshot.getChildren()) {
                                String badgeName = badgeSnapshot.getKey();
                                String badgeState = badgeSnapshot.child("state").getValue(String.class);

                                if (badgeState.equals("completed")) {
                                    completedBadges.add(badgeName);
                                    completedBadgeCount++;
                                }
                            }

                            // Sort the completed badges (customize criteria as needed)
                            Collections.sort(completedBadges);

                            // Display up to two completed badges
                            String badgeDisplay1 = completedBadges.size() > 0 ? completedBadges.get(0) : "None";
                            tvBadge1.setText( badgeDisplay1 != null ? badgeDisplay1 : "0");

                            String badgeDisplay2 = completedBadges.size() > 1 ? completedBadges.get(1) : "None";
                            tvBadge2.setText( badgeDisplay2 != null ? badgeDisplay2 : "0");

                            // Display the count of completed badges
                            String badgesValue = String.valueOf(completedBadgeCount);
                            tvBadgesValue.setText(badgesValue);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("DatabaseError", error.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.d("ProfilePageActivity", "Exception: " + e.getMessage());
            }

        }
    }

    private void setupBottomNavigation() {
        // Set listener for bottom navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(ProfilePageActivity.this, LessonUnit.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_lessons) {
                Intent intent = new Intent(ProfilePageActivity.this, Module.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_progress) {
                Intent intent = new Intent(ProfilePageActivity.this, AchievementOverviewActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_forum) {
                Intent intent = new Intent(ProfilePageActivity.this, CommunityFrontPageActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_profile) {
                return true; // Current activity
            }
            return false;
        });

        // Set the current selected item to the profile page
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }
}
