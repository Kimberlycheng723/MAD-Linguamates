package com.example.madasignment.profile.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.example.madasignment.profile.signin_login.LogInActivity;
import com.example.madasignment.profile.ProfilePageActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        Button btnPreferences = findViewById(R.id.btn_pref_s);
        Button btnPrivacy = findViewById(R.id.btn_privacy_s);
        Button btnCourses = findViewById(R.id.btn_courses_s);
        Button btnBack = findViewById(R.id.btn_back_s);
        Button btnSO = findViewById(R.id.btn_signout_s);

        btnPreferences.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, PreferencesActivity.class);
            startActivity(intent);
        });

        btnPrivacy.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, PrivacyActivity.class);
            startActivity(intent);
        });

        btnCourses.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, CoursesActivity.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ProfilePageActivity.class);
            startActivity(intent);
        });

        btnSO.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);
            startActivity(intent);
        });
    }
}
