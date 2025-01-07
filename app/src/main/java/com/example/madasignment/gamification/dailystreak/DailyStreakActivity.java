package com.example.madasignment.gamification.dailystreak;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DailyStreakActivity extends AppCompatActivity {

    private TextView monthLabel, currentStreakValue;
    private GridLayout calendarGrid;
    private Calendar currentCalendar;
    private DatabaseReference databaseRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_streaks);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-linguamates-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseRef = database.getReference("UserStats");

        // Initialize views
        monthLabel = findViewById(R.id.monthLabel);

        currentStreakValue = findViewById(R.id.currentStreakValue);
        calendarGrid = findViewById(R.id.calendarGrid);

        Button prevMonthButton = findViewById(R.id.prevMonthButton);
        Button nextMonthButton = findViewById(R.id.nextMonthButton);


        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> onBackPressed());
        // Initialize calendar
        currentCalendar = Calendar.getInstance();
        currentCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        // Render initial calendar
        renderCalendar(List.of());

        // Handle month navigation
        prevMonthButton.setOnClickListener(view -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateMonthLabel();
            renderCalendar(List.of());
        });

        nextMonthButton.setOnClickListener(view -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateMonthLabel();
            renderCalendar(List.of());
        });

        // Load streak data from database to display
        loadStreakData();
    }

    private void updateMonthLabel() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        String monthName = monthFormat.format(currentCalendar.getTime()); // Get month name
        monthLabel.setText(monthName);
    }

    private void loadStreakData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (userId == null) {
            return;
        }

        DatabaseReference userRef = databaseRef.child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long currentStreak = snapshot.child("currentStreak").getValue(Long.class) != null
                            ? snapshot.child("currentStreak").getValue(Long.class)
                            : 0;
                    List<String> calendarDays = (List<String>) snapshot.child("calendarDays").getValue();

                    // Update UI
                    updateUI(currentStreak, calendarDays);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error
            }
        });
    }

    private void updateUI(long currentStreak, List<String> calendarDays) {
        currentStreakValue.setText(String.valueOf(currentStreak));

        renderCalendar(calendarDays != null ? calendarDays : List.of());
    }

    private void renderCalendar(List<String> calendarDays) {
        calendarGrid.removeAllViews();
        renderWeekHeaders();

        Calendar calendar = (Calendar) currentCalendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        if (firstDayOfWeek < 0) firstDayOfWeek += 7;

        for (int i = 0; i < firstDayOfWeek; i++) {
            addCalendarCell("", false);
        }

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= daysInMonth; day++) {
            String dayString = String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, day);
            boolean isActive = calendarDays.contains(dayString);
            addCalendarCell(String.valueOf(day), isActive);
        }
    }

    private void renderWeekHeaders() {
        String[] daysOfWeek = {"M", "T", "W", "T", "F", "S", "S"};
        for (String day : daysOfWeek) {
            TextView headerCell = new TextView(this);
            headerCell.setText(day);
            headerCell.setTextSize(16);
            headerCell.setGravity(android.view.Gravity.CENTER);
            headerCell.setTextColor(getResources().getColor(android.R.color.black));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            calendarGrid.addView(headerCell, params);
        }
    }

    private void addCalendarCell(String text, boolean isActive) {
        TextView cell = new TextView(this);
        cell.setText(text);
        cell.setTextSize(14); // Adjust text size for smaller cells
        cell.setTextColor(getResources().getColor(android.R.color.black));
        cell.setGravity(android.view.Gravity.CENTER);

        if (isActive) {
            cell.setBackgroundResource(R.drawable.circle_background);
            cell.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            cell.setTextColor(getResources().getColor(android.R.color.black));
        }

        // Calculate original cell size
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int totalMargin = (4 * 12); // Margins around cells
        int originalCellSize = (screenWidth - totalMargin) / 7; // Default size for 7-day week

        // Scale cell size by a factor (e.g., 0.4)
        int scaledCellSize = (int) (originalCellSize * 0.8);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = scaledCellSize;
        params.height = scaledCellSize;
        params.setMargins(12, 12, 12, 12); // Margins for spacing

        calendarGrid.addView(cell, params);
    }

}