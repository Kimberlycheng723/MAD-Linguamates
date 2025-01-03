package com.example.madasignment.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePageActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String mobileNumber = "1234567890";
    private TextView tvStatStreak, tvStatLeague, tvStatLang, tvStatLessons;
    private ImageView ivStatStreak, ivStatLeague, ivStatLang, ivStatLessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Button btnset = findViewById(R.id.btn_set_pp);
        Button editPage = findViewById(R.id.Bt_editProfile);

        btnset.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePageActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        editPage.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePageActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Initialize Firebase Database Reference
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-linguamates-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference("userStats");

        // Initialize Views
        tvStatStreak = findViewById(R.id.tv_stat_dayStreak_pp);
        tvStatLeague = findViewById(R.id.tv_league_pp);
        tvStatLang = findViewById(R.id.tv_statlanguage_pp);
        tvStatLessons = findViewById(R.id.tv_lessons_pp);

        ivStatStreak = findViewById(R.id.iv_statstreak_pp);
        ivStatLeague = findViewById(R.id.iv_statleague_pp);
        ivStatLang = findViewById(R.id.iv_statlang_pp);
        ivStatLessons = findViewById(R.id.iv_statlessons_pp);

        // Fetch and Display Data
        fetchUserData(mobileNumber);
    }

    private void fetchUserData(String mobile) {
        databaseReference.child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve Data
                    String streak = snapshot.child("iv_statstreak_pp").getValue(String.class);
                    tvStatStreak.setText("Day Streak: " + (streak != null ? streak : "0"));

                    String league = snapshot.child("iv_statleague_pp").getValue(String.class);
                    tvStatLeague.setText("League: " + (league != null ? league : "None"));

                    String languages = snapshot.child("iv_statlang_pp").getValue(String.class);
                    tvStatLang.setText("Languages: " + (languages != null ? languages : "0"));

                    String lessons = snapshot.child("iv_statlessons_pp").getValue(String.class);
                    tvStatLessons.setText("Lessons: " + (lessons != null ? lessons : "0"));

                    Log.d("ProfilePageActivity", "Snapshot: " + snapshot.toString());

                    // Update ImageViews
                    ivStatStreak.setImageResource(R.drawable.streakl);
                    ivStatLeague.setImageResource(R.drawable.ranking);
                    ivStatLang.setImageResource(R.drawable.bookmark);
                    ivStatLessons.setImageResource(R.drawable.level);
                } else {
                    // Handle no data found for the mobile
                    tvStatStreak.setText("No data found");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle Database Error
                tvStatStreak.setText("Error: " + error.getMessage());
            }
        });
    }

//    private void fetchUserData(String mobile) {
//        databaseReference.child(mobile).get()
//                .addOnSuccessListener(dataSnapshot -> {
//                    if (dataSnapshot.exists()) {
//                        UserStats user = dataSnapshot.getValue(UserStats.class);
//                        if (user != null) {
//                            Log.d("Firebase", "Name: " + user.getName());
//                            Log.d("Firebase", "Streak: " + user.getIv_statstreak_pp());
//                            Log.d("Firebase", "League: " + user.getIv_statleague_pp());
//                            Log.d("Firebase", "Languages: " + user.getIv_statlang_pp());
//                            Log.d("Firebase", "Lessons: " + user.getIv_statlessons_pp());
//                        }
//                    } else {
//                        Log.d("Firebase", "No data found for this mobile");
//                    }
//                })
//                .addOnFailureListener(e -> Log.e("Firebase", "Failed to fetch user data", e));
//    }
}
