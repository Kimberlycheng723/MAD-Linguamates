package com.example.madasignment.gamification;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.madasignment.gamification.BadgeItemDecoration;

import com.example.madasignment.R;

import java.util.ArrayList;
import java.util.List;

public class MyBadgesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BadgeAdapter badgeAdapter;
    private List<Badge> badgeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_badges);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns in the grid

        // Add spacing between badges
        int spacing = getResources().getDimensionPixelSize(R.dimen.badge_spacing); // Spacing defined in 'dimens.xml'
        recyclerView.addItemDecoration(new BadgeItemDecoration(spacing));

        // Initialize Badge List
        badgeList = new ArrayList<>();
        badgeList.add(new Badge("5 Lessons", "Completed 5 lessons in a week", "completed", R.drawable.badge_completed));
        badgeList.add(new Badge("Lesson Streak", "Keep learning for 5 days", "completed", R.drawable.badge_completed));
        badgeList.add(new Badge("Top 25", "Top 25 in Friends League", "completed", R.drawable.badge_completed));
        badgeList.add(new Badge("10-Day Streak", "Complete 10 days streak", "in_progress", R.drawable.badge_in_progress));
        badgeList.add(new Badge("10 Lessons", "Completed 10 lessons in a week", "in_progress", R.drawable.badge_in_progress));
        badgeList.add(new Badge("Top 10", "Top 10 in Friends League", "locked", R.drawable.badge_locked));
        badgeList.add(new Badge("Complete", "Complete all lessons ", "locked", R.drawable.badge_locked));
        badgeList.add(new Badge("30-Day Streak", "Complete 30 days streak", "locked", R.drawable.badge_locked));
        badgeList.add(new Badge("1st Place", "1st in Friends League", "locked", R.drawable.badge_locked));

        // Set Adapter
        badgeAdapter = new BadgeAdapter(this, badgeList);
        recyclerView.setAdapter(badgeAdapter);
    }
}
