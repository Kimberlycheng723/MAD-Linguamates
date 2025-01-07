package com.example.madasignment.home.lesson_unit.levels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.madasignment.R;
import com.example.madasignment.home.lesson_unit.flashcard.FlashcardSplashScreenActivity;
import com.example.madasignment.profile.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LevelsActivity extends AppCompatActivity {

    private TextView diamondCount;
    private TextView streakCount;
    private ScrollView scrollView;

    // Firebase references
    private FirebaseAuth auth;
    private DatabaseReference userStatsRef;
    private DatabaseReference userProfileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        // Initialize Firebase Auth and get the authenticated user ID
        auth = FirebaseAuth.getInstance();
        String userId = getAuthenticatedUserId();

        if (userId == null) {
            // User is not authenticated, redirect to login screen
            redirectToLogin();
            return;
        }

        // Firebase database references
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-linguamates-default-rtdb.asia-southeast1.firebasedatabase.app");
        userStatsRef = database.getReference("UserStats").child(userId);
        userProfileRef = database.getReference("users").child(userId);


        streakCount = findViewById(R.id.streakCount);
        scrollView = findViewById(R.id.scrollView);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(""); // Set an empty title
        }

        // Back button setup
        ImageView backButton = findViewById(R.id.levelsUpButton);
        backButton.setOnClickListener(v -> onBackPressed());

        // Fetch and display data from Firebase
        fetchStreakData();

        // Level 1 Bubble
        ImageButton levelBubble1 = findViewById(R.id.levelBubble1);
        levelBubble1.setOnClickListener(v -> {
            // Start the Splash Screen Activity
            Intent intent = new Intent(LevelsActivity.this, FlashcardSplashScreenActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Get the authenticated user's ID.
     */
    private String getAuthenticatedUserId() {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d("FirebaseAuth", "User ID: " + userId);
            return userId;
        } else {
            Log.e("FirebaseAuth", "User not authenticated!");
            return null;
        }
    }

    /**
     * Redirect to the login screen if the user is not authenticated.
     */
    private void redirectToLogin() {
        Toast.makeText(this, "Please log in to continue.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Fetch streak and XP data from Firebase.
     */
    private void fetchStreakData() {
        // Fetch current streak
        userStatsRef.child("currentStreak").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int streak = snapshot.getValue(Integer.class);
                    updateStreakDisplay(streak);
                } else {
                    Log.d("FirebaseData", "Streak data not found. Setting default value.");
                    updateStreakDisplay(0); // Default value
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to fetch streak: " + error.getMessage());
                Toast.makeText(LevelsActivity.this, "Error fetching streak data.", Toast.LENGTH_SHORT).show();
                updateStreakDisplay(0); // Default fallback
            }
        });



    }

    private void updateStreakDisplay(int streak) {
        if (streakCount != null) {
            streakCount.setText(String.valueOf(streak));
        } else {
            Log.e("UIError", "streakCount TextView is null!");
        }
    }
}
