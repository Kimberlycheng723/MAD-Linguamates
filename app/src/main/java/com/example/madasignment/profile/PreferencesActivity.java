package com.example.madasignment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PreferencesActivity extends AppCompatActivity {
    private Switch soundEffectSwitch, listeningExerciseSwitch;
    private DatabaseReference preferencesRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        soundEffectSwitch = findViewById(R.id.sw_sound_p);
        listeningExerciseSwitch = findViewById(R.id.sw_listen_p);
        Button backButton = findViewById(R.id.btn_back_p);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        preferencesRef = FirebaseDatabase.getInstance().getReference("AccountSettings").child(userId);

        // Load switch states
        preferencesRef.child("soundEffect").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                soundEffectSwitch.setChecked(snapshot.getValue(Boolean.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        soundEffectSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferencesRef.child("soundEffect").setValue(isChecked));

        listeningExerciseSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferencesRef.child("listeningExercises").setValue(isChecked));

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PreferencesActivity.this, ProfilePageActivity.class);
            startActivity(intent);
            finish();
        });
    }
}