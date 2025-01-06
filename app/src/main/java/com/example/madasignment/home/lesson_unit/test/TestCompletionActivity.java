package com.example.madasignment.home.lesson_unit.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.example.madasignment.gamification.BadgeUtils;
import com.example.madasignment.home.lesson_unit.lesson_unit.LessonUnit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TestCompletionActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private int testsCompleted = 0; // Local variable for tracking tests

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_completion);

        // Firebase setup
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;
        databaseRef = FirebaseDatabase.getInstance("https://mad-linguamates-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("UserStats")
                .child(userId);

        // Initialize UI components
        ImageView fireworkLeft = findViewById(R.id.fireworkLeft);
        ImageView fireworkRight = findViewById(R.id.fireworkRight);
        LinearLayout scoreContainer = findViewById(R.id.scoreContainer);
        TextView scoreText = findViewById(R.id.scoreText);
        Button backToHomeButton = findViewById(R.id.backToHomeButton);

        // Animations
        Animation animationLeft = AnimationUtils.loadAnimation(this, R.anim.firework_animation);
        Animation animationRight = AnimationUtils.loadAnimation(this, R.anim.firework_animation);

        scoreContainer.startAnimation(animationLeft);
        fireworkLeft.startAnimation(animationLeft);
        fireworkRight.postDelayed(() -> fireworkRight.startAnimation(animationRight), 500);

        // Update test count and badges
        incrementTestCount();

        // Back to home button
        backToHomeButton.setOnClickListener(v -> startActivity(new Intent(this, LessonUnit.class)));
    }

    private void incrementTestCount() {
        databaseRef.child("testCount").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Integer testCount = task.getResult().getValue(Integer.class);
                testsCompleted = (testCount != null) ? testCount : 0;

                // Increment the count
                testsCompleted++;

                // Update test count in Firebase
                databaseRef.child("testCount").setValue(testsCompleted);

                // Update badges for all test milestones
                BadgeUtils.updateBadgeProgress("First Test", testsCompleted);
                BadgeUtils.updateBadgeProgress("3 Tests", testsCompleted);
                BadgeUtils.updateBadgeProgress("5 Tests", testsCompleted);
            } else {
                // Handle case where testCount doesn't exist in Firebase
                testsCompleted = 1; // Start with 1 for the first test
                databaseRef.child("testCount").setValue(testsCompleted);

                // Update badges
                BadgeUtils.updateBadgeProgress("First Test", testsCompleted);
                BadgeUtils.updateBadgeProgress("3 Tests", testsCompleted);
                BadgeUtils.updateBadgeProgress("5 Tests", testsCompleted);
            }
        });
    }
}
