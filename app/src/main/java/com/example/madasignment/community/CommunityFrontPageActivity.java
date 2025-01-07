package com.example.madasignment.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.gamification.AchievementOverviewActivity;
import com.example.madasignment.lessons.Module.module.Module;
import com.example.madasignment.R;
import com.example.madasignment.home.lesson_unit.lesson_unit.LessonUnit;
import com.example.madasignment.profile.ProfilePageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CommunityFrontPageActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_front_page);

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set up Bottom Navigation
        setupBottomNavigation();
        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.nav_blue));
        // Navigate to Discussion Forum
        findViewById(R.id.BtnDiscussionForum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityFrontPageActivity.this, DiscussionForumActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Friendlist
        findViewById(R.id.BtnFriendList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityFrontPageActivity.this, FriendlistActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupBottomNavigation() {
        // Ensure BottomNavigationView is properly initialized
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(CommunityFrontPageActivity.this, LessonUnit.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_lessons) {
                Intent intent = new Intent(CommunityFrontPageActivity.this, Module.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_progress) {
                Intent intent = new Intent(CommunityFrontPageActivity.this, AchievementOverviewActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_forum) {
                return true; // Already on Community Page
            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(CommunityFrontPageActivity.this, ProfilePageActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Set default selected item
        bottomNavigationView.setSelectedItemId(R.id.nav_forum);
    }
}
