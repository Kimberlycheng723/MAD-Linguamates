package com.example.madasignment.lessons.Module.module;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;
import com.example.madasignment.community.CommunityFrontPageActivity;
import com.example.madasignment.gamification.AchievementOverviewActivity;
import com.example.madasignment.home.lesson_unit.lesson_unit.LessonUnit;
import com.example.madasignment.profile.ProfilePageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Module extends AppCompatActivity {
    private RecyclerView moduleRecyclerView;
    private ModuleAdapter moduleAdapter;
    private List<ModuleData> easyModules, mediumModules, hardModules, allModules;
    private Spinner difficultySpinner;

    private BottomNavigationView bottomNavigationView;
    private int completedModules = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        // Initialize views
        initViews();

        // Load modules for all difficulties
        loadModules();

        // Initialize RecyclerView with "Easy" as default
        setupRecyclerView(easyModules);

        // Set up Spinner for difficulty selection
        setupSpinner();

        // Set up Bottom Navigation
        setupBottomNavigation();


    }

    private void initViews() {
        moduleRecyclerView = findViewById(R.id.moduleRecyclerView);
        difficultySpinner = findViewById(R.id.difficultySpinner);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void loadModules() {
        // Easy Modules
        easyModules = new ArrayList<>();
        easyModules.add(new ModuleData("My Day", R.drawable.module1, "transcription_my_day", "VD98ZnvusY8"));
        easyModules.add(new ModuleData("Me Too !", R.drawable.module2, "transcription_me_too", "UUDt4vyCOfo"));
        easyModules.add(new ModuleData("Cute !", R.drawable.module3, "transcription_my_day", "BZTUizv25hM"));



        // Medium Modules
        mediumModules = new ArrayList<>();
        mediumModules.add(new ModuleData("Red Light, Green Light", R.drawable.module4, "transcription_my_day", "3D7s0aPf1GM"));
        mediumModules.add(new ModuleData("Look Left, Look Right", R.drawable.module5, "transcription_my_day", "7hU_w_aggkQ"));
        mediumModules.add(new ModuleData("I Like Ice Cream", R.drawable.module6, "transcription_my_day", "YJZj0C8VIKY"));

        // Hard Modules
        hardModules = new ArrayList<>();
        hardModules.add(new ModuleData("One Cloud, Many Clouds", R.drawable.module7, "transcription_my_day", "Cv0qM_KPlIA"));
        hardModules.add(new ModuleData("Delicious Soup", R.drawable.module8, "transcription_my_day", "UEQsZXCZglI"));
        hardModules.add(new ModuleData("It's Snowy", R.drawable.module9, "transcription_my_day", "edTXMZY2e_0"));


        // Combine all modules
        allModules = new ArrayList<>();
        allModules.addAll(easyModules);
        allModules.addAll(mediumModules);
        allModules.addAll(hardModules);
    }

    private void setupSpinner() {
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String difficulty = parent.getItemAtPosition(position).toString();
                Log.d("ModuleActivity", "Selected Difficulty: " + difficulty);
                switch (difficulty) {
                    case "Easy":
                        setupRecyclerView(easyModules);
                        break;
                    case "Medium":
                        setupRecyclerView(mediumModules);
                        break;
                    case "Hard":
                        setupRecyclerView(hardModules);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("ModuleActivity", "No difficulty selected.");
            }
        });
    }

    private void setupRecyclerView(List<ModuleData> modules) {
        if (moduleAdapter == null) {
            // Create a new adapter and set it to the RecyclerView
            moduleAdapter = new ModuleAdapter(modules);
            moduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            moduleRecyclerView.setAdapter(moduleAdapter);
        } else {
            // Update the data in the existing adapter
            moduleAdapter.updateData(modules);
        }
    }


    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(Module.this, LessonUnit.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_lessons) {
                return true; // Stay on this page
            } else if (id == R.id.nav_progress) {
                Intent intent = new Intent(Module.this, AchievementOverviewActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_forum) {
                Intent intent = new Intent(Module.this, CommunityFrontPageActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(Module.this, ProfilePageActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_lessons);
    }


}
