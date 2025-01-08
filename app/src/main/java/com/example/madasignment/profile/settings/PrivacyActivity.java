package com.example.madasignment.profile.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PrivacyActivity extends AppCompatActivity {
    private EditText passwordField;
    private Button btnBack, btnSave;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_privacy);

        // Initialize views
        passwordField = findViewById(R.id.etp_password_pr);
        btnBack = findViewById(R.id.btn_back_pr);
        btnSave = findViewById(R.id.btn_save_pr);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Load current user's password (if available)
        loadUserPassword();

        // Back button functionality
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(PrivacyActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Save button functionality
        btnSave.setOnClickListener(v -> {
            String newPassword = passwordField.getText().toString().trim();

            if (newPassword.isEmpty()) {
                Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            } else {
                updatePassword(newPassword);
            }
        });
    }

    private void loadUserPassword() {
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null) {
            // Currently, Firebase Authentication does not allow direct retrieval of passwords.
            // This method is included as a placeholder if there is a custom mechanism for retrieving passwords.
            // For example, you could store passwords in your Firestore database or a secure backend.
            passwordField.setText("******"); // Set placeholder text for now.
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePassword(String newPassword) {
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null) {
            firebaseUser.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                            // Redirect to settings
                            Intent intent = new Intent(PrivacyActivity.this, SettingsActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Failed to update password. Try again.", Toast.LENGTH_SHORT).show();
                            Log.e("PrivacyActivity", "Password update error: ", task.getException());
                        }
                    });
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }
}
