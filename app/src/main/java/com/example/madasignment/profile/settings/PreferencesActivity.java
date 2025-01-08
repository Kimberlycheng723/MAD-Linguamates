package com.example.madasignment.profile.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
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
    private Switch soundEffectSwitch;
    private DatabaseReference preferencesRef;
    private String userId;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_preferences);

        soundEffectSwitch = findViewById(R.id.sw_sound_p);
        Button backButton = findViewById(R.id.btn_back_p);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        preferencesRef = FirebaseDatabase.getInstance().getReference("AccountSettings").child(userId);

        // Initialize AudioManager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Load state from SharedPreferences or Firebase
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        boolean isSoundOn = sharedPreferences.getBoolean("soundEffectsEnabled", true); // Default ON
        soundEffectSwitch.setChecked(isSoundOn); // Set initial state
        toggleSoundEffects(isSoundOn);

        // Sync state with Firebase
        preferencesRef.child("soundEffect").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean firebaseSoundOn = snapshot.getValue(Boolean.class);
                if (firebaseSoundOn != null) {
                    soundEffectSwitch.setChecked(firebaseSoundOn); // Sync with Firebase
                    toggleSoundEffects(firebaseSoundOn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // Handle switch toggle
        soundEffectSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save state to Firebase
            preferencesRef.child("soundEffect").setValue(isChecked);

            // Save state locally in SharedPreferences
            sharedPreferences.edit()
                    .putBoolean("soundEffectsEnabled", isChecked)
                    .apply();

            // Update sound effects
            toggleSoundEffects(isChecked);
        });

        // Handle back button
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PreferencesActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Toggles sound effects on or off based on the given state.
     *
     * @param isOn True to enable sound effects, false to mute.
     */
    private void toggleSoundEffects(boolean isOn) {
        if (audioManager != null) {
            if (isOn) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
            } else {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            }
        }
    }
}
