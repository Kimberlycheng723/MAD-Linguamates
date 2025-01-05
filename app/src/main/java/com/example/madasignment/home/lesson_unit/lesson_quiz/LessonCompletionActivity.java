package com.example.madasignment.home.lesson_unit.lesson_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.example.madasignment.gamification.BadgeFirebaseModel;
import com.example.madasignment.gamification.BadgeUtils;

import com.example.madasignment.home.lesson_unit.lesson_unit.LessonUnit;
import com.example.madasignment.home.lesson_unit.test.TestSplashScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LessonCompletionActivity extends AppCompatActivity {

    private int lessonsCompleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_completion);

        ImageView fireworkLeft = findViewById(R.id.fireworkLeft);
        ImageView fireworkRight = findViewById(R.id.fireworkRight);
        LinearLayout scoreContainer = findViewById(R.id.scoreContainer);
        TextView scoreText = findViewById(R.id.scoreText);
        Button takeQuizButton = findViewById(R.id.takeQuizButton);
        Button backToHomeButton = findViewById(R.id.backToHomeButton);

        Animation animationLeft = AnimationUtils.loadAnimation(this, R.anim.firework_animation);
        Animation animationRight = AnimationUtils.loadAnimation(this, R.anim.firework_animation);

        scoreContainer.startAnimation(animationLeft);
        fireworkLeft.startAnimation(animationLeft);
        fireworkRight.postDelayed(() -> fireworkRight.startAnimation(animationRight), 500);

        lessonsCompleted++;
        updateLessonBadges(lessonsCompleted);

        takeQuizButton.setOnClickListener(v -> startActivity(new Intent(this, TestSplashScreen.class)));
        backToHomeButton.setOnClickListener(v -> startActivity(new Intent(this, LessonUnit.class)));
    }


    private void updateLessonBadges(int lessonsCompleted) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the user's ID
        DatabaseReference badgesRef = FirebaseDatabase.getInstance()
                .getReference("UserStats")
                .child(userId)
                .child("badges");

        // Update "First Lesson" badge
        badgesRef.child("First Lesson").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    BadgeFirebaseModel badge = snapshot.getValue(BadgeFirebaseModel.class);
                    if (badge != null) {
                        // Update progress
                        int newProgress = Math.min(badge.getGoal(), lessonsCompleted);
                        badge.setProgress(newProgress);

                        // Update state based on progress
                        if (newProgress >= badge.getGoal()) {
                            badge.setState("completed");
                        } else {
                            badge.setState("in_progress");
                        }

                        // Save updated badge to Firebase
                        badgesRef.child("First Lesson").setValue(badge);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("BadgeUpdate", "Failed to update badge: " + error.getMessage());
            }
        });

        // Repeat similar logic for "5 Lessons" and "10 Lessons"
        updateSpecificBadge(badgesRef, "5 Lessons", lessonsCompleted, 5);
        updateSpecificBadge(badgesRef, "10 Lessons", lessonsCompleted, 10);
    }

    private void updateSpecificBadge(DatabaseReference badgesRef, String badgeName, int lessonsCompleted, int goal) {
        badgesRef.child(badgeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    BadgeFirebaseModel badge = snapshot.getValue(BadgeFirebaseModel.class);
                    if (badge != null) {
                        // Update progress and state
                        int newProgress = Math.min(goal, lessonsCompleted);
                        badge.setProgress(newProgress);
                        badge.setState(newProgress >= goal ? "completed" : (newProgress > 0 ? "in_progress" : "locked"));

                        // Save updated badge
                        badgesRef.child(badgeName).setValue(badge);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("BadgeUpdate", "Failed to update badge: " + error.getMessage());
            }
        });
    }

}
