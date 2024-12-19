package com.example.madasignment.read;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.Module.Module;
import com.example.madasignment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadOnlyActivity extends AppCompatActivity {
    private RecyclerView transcriptionList;
    private ReadOnlyAdapter adapter;
    private boolean showTranslation = false; // Initially hide translations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_only);

        // Setup Toolbar with "Up" button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Translation");

        }

        // Initialize RecyclerView
        transcriptionList = findViewById(R.id.transcription_list);
        transcriptionList.setLayoutManager(new LinearLayoutManager(this));

        // Get the filename from the Intent
        String readContentFile = getIntent().getStringExtra("readContent");

        // Load transcription data from JSON
        List<ReadOnlyTranscription> transcriptionData = loadTranscriptionData(readContentFile);

        // Set up the RecyclerView adapter
        adapter = new ReadOnlyAdapter(transcriptionData, showTranslation);
        transcriptionList.setAdapter(adapter);

        // Handle toggle for showing/hiding translations
        ImageButton toggleTranslation = findViewById(R.id.toggle_translation);
        toggleTranslation.setOnClickListener(v -> {
            showTranslation = !showTranslation; // Toggle translation state
            adapter.setShowTranslation(showTranslation);
            toggleTranslation.setImageResource(showTranslation ? R.drawable.read_only_translation : R.drawable.read_only_translation);
        });

        // Handle "Finish Reading" button
        ImageButton finishReadingButton = findViewById(R.id.finish_reading_button);
        finishReadingButton.setOnClickListener(v -> {
            Intent intent = new Intent(ReadOnlyActivity.this, Module.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Handle "Up" button click
            Intent intent = new Intent(this, Module.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<ReadOnlyTranscription> loadTranscriptionData(String filename) {
        List<ReadOnlyTranscription> transcriptionList = new ArrayList<>();
        try {
            // Load the transcription JSON from raw resources
            int resourceId = getResources().getIdentifier(filename, "raw", getPackageName());
            InputStream is = getResources().openRawResource(resourceId);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            // Parse JSON
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String chinese = obj.getString("chinese");
                String english = obj.getString("english");
                transcriptionList.add(new ReadOnlyTranscription(chinese, english));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transcriptionList;
    }
}
