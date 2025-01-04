package com.example.madasignment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
    private TextView tvStreakValue, tvLeagueValue, tvLessonValue, tvLanguageValue;

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

        tvStreakValue = findViewById(R.id.tv_streakValue_pp);
        tvLeagueValue = findViewById(R.id.tv_leagueValue_pp);
        tvLanguageValue = findViewById(R.id.tv_languageValue_pp);
        tvLessonValue = findViewById(R.id.tv_lessonValue_pp);

        // Fetch and Display Data
        fetchUserData(mobileNumber);
    }

    private void fetchUserData(String mobile) {
        databaseReference.child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve Data
                    String streak = snapshot.child("iv_statstreak_pp").getValue(String.class);
                    tvStreakValue.setText(streak != null ? streak : "0");

                    String league = snapshot.child("iv_statleague_pp").getValue(String.class);
                    tvLeagueValue.setText(league != null ? league : "None");

                    String languages = snapshot.child("iv_statlang_pp").getValue(String.class);
                    if (languages != null) {
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
                // Handle Database Error
                Log.d("ProfilePageActivity", "Error: " + error.getMessage());
            }
        });
    }

}
