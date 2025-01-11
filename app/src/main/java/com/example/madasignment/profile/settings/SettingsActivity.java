package com.example.madasignment.profile.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.example.madasignment.profile.ProfilePageActivity;
import com.example.madasignment.profile.SaveSharedPreference;
import com.example.madasignment.profile.signin_login.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        Button btnPreferences = findViewById(R.id.btn_pref_s);
        Button btnPrivacy = findViewById(R.id.btn_privacy_s);
        Button btnCourses = findViewById(R.id.btn_courses_s);
        Button btnBack = findViewById(R.id.btn_back_s);
        Button btnSignOut = findViewById(R.id.btn_signout_s);

        // Preferences Button
        btnPreferences.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, PreferencesActivity.class);
            startActivity(intent);
        });

        // Privacy Button
        btnPrivacy.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, PrivacyActivity.class);
            startActivity(intent);
        });

        // Courses Button
        btnCourses.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, CoursesActivity.class);
            startActivity(intent);
        });

        // Back Button
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ProfilePageActivity.class);
            startActivity(intent);
        });

        // Sign Out Button
        btnSignOut.setOnClickListener(v -> {
            logOutUser();
        });
    }

    private void logOutUser() {
        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut();

        // Clear saved user data
        SaveSharedPreference.clearUserData(this);

        // Redirect to LogInActivity
        Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
        startActivity(intent);

        // Optionally show a logout confirmation
        finish(); // Close the SettingsActivity
    }
}
