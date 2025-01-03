package com.example.madasignment.profile;

import android.content.Context;
import android.content.Intent;
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
    private Switch soundEffectSwitch, listeningExerciseSwitch;
    private DatabaseReference preferencesRef;
    private String userId;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        soundEffectSwitch = findViewById(R.id.sw_sound_p);
        listeningExerciseSwitch = findViewById(R.id.sw_listen_p);
        Button backButton = findViewById(R.id.btn_back_p);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        preferencesRef = FirebaseDatabase.getInstance().getReference("AccountSettings").child(userId);

        // Initialize AudioManager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Load switch states
        preferencesRef.child("soundEffect").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isSoundOn = snapshot.getValue(Boolean.class);
                if (isSoundOn != null) {
                    soundEffectSwitch.setChecked(isSoundOn);
                    toggleSoundEffects(isSoundOn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        soundEffectSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesRef.child("soundEffect").setValue(isChecked);
            toggleSoundEffects(isChecked);
        });

        listeningExerciseSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferencesRef.child("listeningExercises").setValue(isChecked));

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PreferencesActivity.this, ProfilePageActivity.class);
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
