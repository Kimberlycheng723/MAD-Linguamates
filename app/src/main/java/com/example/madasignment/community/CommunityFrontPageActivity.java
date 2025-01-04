package com.example.madasignment.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.Module.Module;
import com.example.madasignment.R;
import com.example.madasignment.lesson_unit.LessonUnit;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CommunityFrontPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_front_page);

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
//                Toast.makeText(CommunityFrontPageActivity.this, "Navigating to Friendlist", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CommunityFrontPageActivity.this, FriendlistActivity.class);
                startActivity(intent);
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
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
                Toast.makeText(this, "Progress Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_forum) {

                return true;
            } else if (id == R.id.nav_profile) {
                Toast.makeText(this, "Profile Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });

    }
}
