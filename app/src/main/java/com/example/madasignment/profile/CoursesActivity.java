package com.example.madasignment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;

public class CoursesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        Button btnBack = findViewById(R.id.btn_back_c);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CoursesActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}