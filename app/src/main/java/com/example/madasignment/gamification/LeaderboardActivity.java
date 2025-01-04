package com.example.madasignment.gamification;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<LeaderboardItem> leaderboardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize leaderboard list
        leaderboardList = new ArrayList<>();
        leaderboardList.add(new LeaderboardItem(1, "#abcdef", 2637));
        leaderboardList.add(new LeaderboardItem(2, "#ghijkl", 2347));
        leaderboardList.add(new LeaderboardItem(3, "#mnopqr", 2290));
        leaderboardList.add(new LeaderboardItem(4, "#stuvwx", 2201));
        leaderboardList.add(new LeaderboardItem(5, "#yzabcd", 1909));

        // Set adapter
        leaderboardAdapter = new LeaderboardAdapter(this, leaderboardList);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);

        // Handle Back Arrow
        findViewById(R.id.backArrow).setOnClickListener(view -> finish());
    }
}
