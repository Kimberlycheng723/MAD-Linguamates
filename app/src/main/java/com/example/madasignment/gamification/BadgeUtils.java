package com.example.madasignment.gamification;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BadgeUtils {

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

                    if (progress >= badge.getGoal()) {
                        badge.setState("completed");
                    } else if (progress > 0) {
                        badge.setState("in_progress");
                    }

                    // Save the updated badge back to Firebase
                    badgeRef.setValue(badge);
                }
            }
        });
    }
}
