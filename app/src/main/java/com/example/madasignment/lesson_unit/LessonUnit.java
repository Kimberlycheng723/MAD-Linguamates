package com.example.madasignment.lesson_unit;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.Module.Module;
import com.example.madasignment.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class LessonUnit extends AppCompatActivity {

    private Button btnBeginner, btnIntermediate, btnAdvanced;
    private RecyclerView unitsRecyclerView;
    private LessonUnitsAdapter unitsAdapter; // Custom adapter for RecyclerView
    private List<LessonUnitData> unitList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        btnBeginner = findViewById(R.id.btnBeginner);
        btnIntermediate = findViewById(R.id.btnIntermediate);
        btnAdvanced = findViewById(R.id.btnAdvanced);
        unitsRecyclerView = findViewById(R.id.unitsRecyclerView);
        ImageView flagIcon = findViewById(R.id.flagIcon);
        TextView motivationalQuote = findViewById(R.id.motivationalQuote);

        String styledText = "<font color='#000000'>The more you practice,</font><br>" +
                "<font color='#007BFF'>The easier it gets!</font>";
        motivationalQuote.setText(Html.fromHtml(styledText));

        String selectedLanguage = getIntent().getStringExtra("language");

        // Set flag and quote dynamically
        if ("Chinese".equals(selectedLanguage)) {
            flagIcon.setImageResource(R.drawable.flag_chinese);
            motivationalQuote.setText("The more you practice, the easier it gets!");
        } else if ("French".equals(selectedLanguage)) {
            flagIcon.setImageResource(R.drawable.flag_chinese);
            motivationalQuote.setText("Plus vous pratiquez, plus cela devient facile !");
        } else if ("Spanish".equals(selectedLanguage)) {
            flagIcon.setImageResource(R.drawable.flag_chinese);
            motivationalQuote.setText("¡Cuanto más practicas, más fácil se vuelve!");
        }

        updateButtonSelection(btnBeginner);
        // Set up RecyclerView
        unitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        unitList = new ArrayList<>();
        unitsAdapter = new LessonUnitsAdapter(unitList);
        unitsRecyclerView.setAdapter(unitsAdapter);

        // Load Beginner Units by Default
        loadUnits("Beginner");

        // Button click listeners to load the appropriate modules
        btnBeginner.setOnClickListener(view -> {
            updateButtonSelection(btnBeginner); // Highlight the selected button
            loadUnits("Beginner"); // Load Beginner module
        });

        btnIntermediate.setOnClickListener(view -> {
            updateButtonSelection(btnIntermediate); // Highlight the selected button
            loadUnits("Intermediate"); // Load Intermediate module
        });

        btnAdvanced.setOnClickListener(view -> {
            updateButtonSelection(btnAdvanced); // Highlight the selected button
            loadUnits("Advanced"); // Load Advanced module
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {

                return true;
            } else if (id == R.id.nav_lessons) {
                Intent intent = new Intent(LessonUnit.this, Module.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_progress) {
                Toast.makeText(this, "Progress Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_forum) {
                Toast.makeText(this, "Forum Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(LessonUnit.this, profile.class);
                startActivity(intent);
                return true;
            } else {
                return false;
            }
        });




    }


    private void updateButtonSelection(Button selectedButton) {
        // Reset all buttons to non-selected style
        btnBeginner.setBackgroundResource(R.drawable.unit_btn_non_selected);
        btnIntermediate.setBackgroundResource(R.drawable.unit_btn_non_selected);
        btnAdvanced.setBackgroundResource(R.drawable.unit_btn_non_selected);

        // Clear backgroundTint for all buttons
        btnBeginner.setBackgroundTintList(null);
        btnIntermediate.setBackgroundTintList(null);
        btnAdvanced.setBackgroundTintList(null);

        // Apply selected style
        selectedButton.setBackgroundResource(R.drawable.unit_btn_selected);
        selectedButton.setBackgroundTintList(null); // Ensure no tint is applied
        selectedButton.setTextColor(getResources().getColor(R.color.black)); // Black text for selected
    }



    private void loadUnits(String difficulty) {
        unitList.clear();
        if (difficulty.equals("Beginner")) {
            unitList.add(new LessonUnitData("Common Greetings and Phrases", 100, "Replay", R.drawable.unit_greeting, "#FF8D8D", "#FF6F6F")); // Pink Button
            unitList.add(new LessonUnitData("Numbers, Days, and Months", 70, "Continue", R.drawable.unit_numbers, "#FFDA35", "#DAEC00")); // Green Button
            unitList.add(new LessonUnitData("Colours and Shapes", 0, "Start", R.drawable.unit_colours, "#A1D7FF", "#86CDFF")); // Blue Button
        } else if (difficulty.equals("Intermediate")) {
            unitList.add(new LessonUnitData("Foods and Drinks", 100, "Replay", R.drawable.unit_foods, "#FFDA35", "#DAEC00")); // Orange Button
            unitList.add(new LessonUnitData("Sports and Games", 75, "Continue", R.drawable.unit_sports, "#A8E6CF", "#66BB6A")); // Purple Button
            unitList.add(new LessonUnitData("Emotions and Feelings", 0, "Start", R.drawable.unit_emotions, "#D1C4E9", "#B39DDB")); // Light Blue Button
        } else if (difficulty.equals("Advanced")) {
            unitList.add(new LessonUnitData("Travel Around World", 100, "Replay", R.drawable.unit_travels, "#A8E6CF", "#66BB6A")); // Teal Button
            unitList.add(new LessonUnitData("Festivals and Traditions", 70, "Continue", R.drawable.unit_festivals, "#D1C4E9", "#B39DDB")); // Yellow Button
            unitList.add(new LessonUnitData("Health and Wellness", 25, "Start", R.drawable.unit_health, "#A1D7FF", "#86CDFF")); // Light Green Button
        }
        unitsAdapter.notifyDataSetChanged();
    }

}