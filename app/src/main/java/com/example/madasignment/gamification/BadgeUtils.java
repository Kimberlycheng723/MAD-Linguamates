package com.example.madasignment.gamification;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BadgeUtils {

    // Update progress for a specific badge by name
    public static void updateBadgeProgress(String badgeName, int progress) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId == null) return; // Ensure user is authenticated

        DatabaseReference badgeRef = FirebaseDatabase.getInstance()
                .getReference("UserStats")
                .child(userId)
                .child("badges")
                .child(badgeName);

        badgeRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                BadgeFirebaseModel badge = task.getResult().getValue(BadgeFirebaseModel.class);
                if (badge != null) {
                    badge.setProgress(progress);

                    // Update badge state based on progress
                    if (progress >= badge.getGoal()) {
                        badge.setState("completed");
                    } else if (progress > 0) {
                        badge.setState("in_progress");
                    } else {
                        badge.setState("locked");
                    }

                    // Save the updated badge back to Firebase
                    badgeRef.setValue(badge);
                }
            }
        });
    }


    // Update streak badges (3-Day, 5-Day, 7-Day) based on current streak progress
    public static void updateStreakBadges(int currentStreak) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId == null) return;

        DatabaseReference badgesRef = FirebaseDatabase.getInstance()
                .getReference("UserStats")
                .child(userId)
                .child("badges");

        // Check and update the 3-Day Streak badge
        badgesRef.child("3-Day Streak").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                BadgeFirebaseModel badge = task.getResult().getValue(BadgeFirebaseModel.class);
                if (badge != null) {
                    if (!"completed".equals(badge.getState())) { // Only update if not completed
                        badge.setProgress(currentStreak);
                        if (currentStreak >= 3) {
                            badge.setState("completed");
                        } else if (currentStreak > 0) {
                            badge.setState("in_progress");
                        } else {
                            badge.setState("locked");
                        }
                        badgesRef.child("3-Day Streak").setValue(badge);
                    }
                }
            }
        });

        // Check and update the 5-Day Streak badge
        badgesRef.child("5-Day Streak").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                BadgeFirebaseModel badge = task.getResult().getValue(BadgeFirebaseModel.class);
                if (badge != null) {
                    if (!"completed".equals(badge.getState())) { // Only update if not completed
                        badge.setProgress(currentStreak);
                        if (currentStreak >= 5) {
                            badge.setState("completed");
                        } else if (currentStreak > 0) {
                            badge.setState("in_progress");
                        } else {
                            badge.setState("locked");
                        }
                        badgesRef.child("5-Day Streak").setValue(badge);
                    }
                }
            }
        });

        // Check and update the 7-Day Streak badge
        badgesRef.child("7-Day Streak").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                BadgeFirebaseModel badge = task.getResult().getValue(BadgeFirebaseModel.class);
                if (badge != null) {
                    if (!"completed".equals(badge.getState())) { // Only update if not completed
                        badge.setProgress(currentStreak);
                        if (currentStreak >= 7) {
                            badge.setState("completed");
                        } else if (currentStreak > 0) {
                            badge.setState("in_progress");
                        } else {
                            badge.setState("locked");
                        }
                        badgesRef.child("7-Day Streak").setValue(badge);
                    }
                }
            }
        });
    }

}
