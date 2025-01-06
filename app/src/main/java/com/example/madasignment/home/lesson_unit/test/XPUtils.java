package com.example.madasignment.home.lesson_unit.test;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class XPUtils {

    public static void updateXP(int xpEarned) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);

        userRef.child("xp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Integer currentXP = snapshot.getValue(Integer.class);
                if (currentXP == null) currentXP = 0;
                int updatedXP = currentXP + xpEarned;

                userRef.child("xp").setValue(updatedXP).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("XPUpdate", "XP updated to: " + updatedXP);
                    } else {
                        Log.e("XPUpdate", "Failed to update XP");
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("XPUpdate", "Error reading XP: " + error.getMessage());
            }
        });
    }
}
