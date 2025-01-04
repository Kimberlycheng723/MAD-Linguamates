package com.example.madasignment.home.lesson_unit.lesson_unit;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;
import com.example.madasignment.community.CommunityFrontPageActivity;
import com.example.madasignment.gamification.AchievementOverviewActivity;
import com.example.madasignment.home.lesson_unit.lesson_unit.LessonUnitData;
import com.example.madasignment.home.lesson_unit.lesson_unit.LessonUnitsAdapter;
import com.example.madasignment.lessons.Module.module.Module;
import com.example.madasignment.profile.ProfilePageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

public class LessonUnit extends AppCompatActivity {

    private Button btnBeginner, btnIntermediate, btnAdvanced;
    private RecyclerView unitsRecyclerView;
    private LessonUnitsAdapter unitsAdapter;
    private List<LessonUnitData> unitList;

    private FirebaseAuth auth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-linguamates-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseRef = database.getReference("UserStats");

        // Initialize views
        btnBeginner = findViewById(R.id.btnBeginner);
        btnIntermediate = findViewById(R.id.btnIntermediate);
        btnAdvanced = findViewById(R.id.btnAdvanced);
        unitsRecyclerView = findViewById(R.id.unitsRecyclerView);
        ImageView flagIcon = findViewById(R.id.flagIcon);
        TextView motivationalQuote = findViewById(R.id.motivationalQuote);

        // Set motivational quote
        String styledText = "<font color='#000000'>The more you practice,</font><br>" +
                "<font color='#007BFF'>The easier it gets!</font>";
        motivationalQuote.setText(Html.fromHtml(styledText));

        // Handle language-specific adjustments
        String selectedLanguage = getIntent().getStringExtra("language");
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

        // Set up RecyclerView
        unitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        unitList = new ArrayList<>();
        unitsAdapter = new LessonUnitsAdapter(unitList);
        unitsRecyclerView.setAdapter(unitsAdapter);

        // Load Beginner Units by Default
        loadUnits("Beginner");

        // Handle button clicks
        btnBeginner.setOnClickListener(view -> {
            updateButtonSelection(btnBeginner);
            loadUnits("Beginner");
        });

        btnIntermediate.setOnClickListener(view -> {
            updateButtonSelection(btnIntermediate);
            loadUnits("Intermediate");
        });

        btnAdvanced.setOnClickListener(view -> {
            updateButtonSelection(btnAdvanced);
            loadUnits("Advanced");
        });

        // Handle bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_lessons) {
                startActivity(new Intent(LessonUnit.this, Module.class));
                return true;
            } else if (id == R.id.nav_progress) {
                startActivity(new Intent(LessonUnit.this, AchievementOverviewActivity.class));
                return true;
            } else if (id == R.id.nav_forum) {
                startActivity(new Intent(LessonUnit.this, CommunityFrontPageActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(LessonUnit.this, ProfilePageActivity.class));
                return true;
            } else {
                return false;
            }
        });

        // Check streak when the user opens the activity
        checkStreak();
    }


    private void checkStreak() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            Log.e("CheckStreak", "User authentication failed");
            return;
        }

        DatabaseReference userRef = databaseRef.child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
                long currentStreak = 0;
                List<String> calendarDays = new ArrayList<>();

                if (snapshot.exists()) {
                    currentStreak = snapshot.child("currentStreak").getValue(Long.class) != null
                            ? snapshot.child("currentStreak").getValue(Long.class)
                            : 0;
                    String lastLoginDate = snapshot.child("lastLoginDate").getValue(String.class);
                    calendarDays = (List<String>) snapshot.child("calendarDays").getValue();

                    if (calendarDays == null) calendarDays = new ArrayList<>();

                    if (lastLoginDate != null) {
                        Calendar lastLogin = Calendar.getInstance();
                        try {
                            lastLogin.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(lastLoginDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar today = Calendar.getInstance();
                        int difference = (int) ((today.getTimeInMillis() - lastLogin.getTimeInMillis()) / (1000 * 60 * 60 * 24));

                        if (difference == 1) {
                            currentStreak++;
                        } else if (difference > 1) {
                            currentStreak=1;
                        }
                    } else {
                        currentStreak = 1;
                    }

                    if (!calendarDays.contains(todayDate)) {
                        calendarDays.add(todayDate);
                    }

                } else {
                    currentStreak = 1;
                    calendarDays.add(todayDate);
                }

                // Update database
                long finalStreak = currentStreak; // Effectively final variable
                userRef.child("currentStreak").setValue(finalStreak);
                userRef.child("lastLoginDate").setValue(todayDate);
                userRef.child("calendarDays").setValue(calendarDays);

                // Log the streak update
                Log.d("CheckStreak", "Streak updated to: " + finalStreak);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseError", "Database read failed: " + error.getMessage());
            }
        });
    }








    private void updateButtonSelection(Button selectedButton) {
        btnBeginner.setBackgroundResource(R.drawable.unit_btn_non_selected);
        btnIntermediate.setBackgroundResource(R.drawable.unit_btn_non_selected);
        btnAdvanced.setBackgroundResource(R.drawable.unit_btn_non_selected);

        selectedButton.setBackgroundResource(R.drawable.unit_btn_selected);
        selectedButton.setTextColor(getResources().getColor(R.color.black));
    }

    private void loadUnits(String difficulty) {
        unitList.clear();
        if (difficulty.equals("Beginner")) {
            unitList.add(new LessonUnitData("Common Greetings and Phrases", 100, "Replay", R.drawable.unit_greeting, "#FF8D8D", "#FF6F6F"));
            unitList.add(new LessonUnitData("Numbers, Days, and Months", 70, "Continue", R.drawable.unit_numbers, "#FFDA35", "#DAEC00"));
            unitList.add(new LessonUnitData("Colours and Shapes", 0, "Start", R.drawable.unit_colours, "#A1D7FF", "#86CDFF"));
        } else if (difficulty.equals("Intermediate")) {
            unitList.add(new LessonUnitData("Foods and Drinks", 100, "Replay", R.drawable.unit_foods, "#FFDA35", "#DAEC00"));
            unitList.add(new LessonUnitData("Sports and Games", 75, "Continue", R.drawable.unit_sports, "#A8E6CF", "#66BB6A"));
            unitList.add(new LessonUnitData("Emotions and Feelings", 0, "Start", R.drawable.unit_emotions, "#D1C4E9", "#B39DDB"));
        } else if (difficulty.equals("Advanced")) {
            unitList.add(new LessonUnitData("Travel Around World", 100, "Replay", R.drawable.unit_travels, "#A8E6CF", "#66BB6A"));
            unitList.add(new LessonUnitData("Festivals and Traditions", 70, "Continue", R.drawable.unit_festivals, "#D1C4E9", "#B39DDB"));
            unitList.add(new LessonUnitData("Health and Wellness", 25, "Start", R.drawable.unit_health, "#A1D7FF", "#86CDFF"));
        }
        unitsAdapter.notifyDataSetChanged();
    }
}
