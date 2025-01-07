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

public class ProfilePageActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private TextView tvStreakValue, tvLeagueValue, tvLessonValue, tvLanguageValue;
    private TextView tvnameValue, tvusernameValue, tvBioValue;
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
        tvLessonValue = findViewById(R.id.tv_lessonValue_pp);

        tvnameValue = findViewById(R.id.tv_name_pp);
        tvusernameValue = findViewById(R.id.tv_username_pp);
        tvBioValue = findViewById(R.id.tv_bio_pp);

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

                            String streak = snapshot.child("iv_statstreak_pp").getValue(String.class);
                            tvStreakValue.setText(streak != null ? streak : "0");

                            String league = snapshot.child("iv_statleague_pp").getValue(String.class);
                            tvLeagueValue.setText(league != null ? league : "None");

                            String languages = snapshot.child("iv_statlang_pp").getValue(String.class);
                            if (languages != null && !languages.equals("None")) {
                                // Count the number of languages
                                int languageCount = languages.split(",").length;
                                tvLanguageValue.setText(String.valueOf(languageCount));
                            } else {
                                tvLanguageValue.setText("0");
                            }

                            String lessons = snapshot.child("iv_statlessons_pp").getValue(String.class);
                            tvLessonValue.setText(lessons != null ? lessons : "0");

                            Log.d("ProfilePageActivity", "Snapshot: " + snapshot.toString());

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
