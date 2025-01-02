package com.example.madasignment.test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.ArrayList;
import java.util.List;

public class TestResultsActivity extends AppCompatActivity {

    private RecyclerView rvPerformanceBreakup;
    private TestPerformanceAdapter adapter;
    private List<TestPerformanceItem> performanceItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_test_results);

        // Initialize RecyclerView
        rvPerformanceBreakup = findViewById(R.id.rvPerformanceBreakup); // Ensure this matches your layout
        rvPerformanceBreakup.setLayoutManager(new LinearLayoutManager(this));

        // Load dummy data for testing (replace with Firebase data in real app)
        performanceItems = new ArrayList<>();
        performanceItems.add(new TestPerformanceItem("What is 2 + 2?", "4", "4", true));
        performanceItems.add(new TestPerformanceItem("What is the capital of France?", "Berlin", "Paris", false));
        performanceItems.add(new TestPerformanceItem("What is 5 * 6?", "30", "30", true));

        // Set up adapter
        adapter = new TestPerformanceAdapter(this, performanceItems);
        rvPerformanceBreakup.setAdapter(adapter);
    }
}

