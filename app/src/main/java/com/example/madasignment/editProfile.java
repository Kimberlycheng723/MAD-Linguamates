package com.example.madasignment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editProfile extends AppCompatActivity {

    private EditText nameEditText, usernameEditText, birthdayEditText, phoneNumberEditText, avatarUrlEditText;
    private Button saveButton;

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // ProgressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        nameEditText = findViewById(R.id.nameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        birthdayEditText = findViewById(R.id.birthdayEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        avatarUrlEditText = findViewById(R.id.avatarUrlEditText);
        saveButton = findViewById(R.id.saveButton);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading profile...");
        progressDialog.setCancelable(false);

        // Fetch user profile data
        fetchUserProfile();

        // Save button listener
        saveButton.setOnClickListener(v -> saveUserProfile());
    }

    // Fetch user data from Firestore
    private void fetchUserProfile() {
        progressDialog.show(); // Show loading spinner

        String userId = auth.getCurrentUser().getUid(); // Get current user's UID

        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss(); // Dismiss spinner

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Populate UI fields with the retrieved data
                            nameEditText.setText(document.getString("name"));
                            usernameEditText.setText(document.getString("username"));
                            birthdayEditText.setText(document.getString("birthday"));
                            phoneNumberEditText.setText(document.getString("phoneNumber"));
                            avatarUrlEditText.setText(document.getString("avatarUrl"));
                        } else {
                            Toast.makeText(this, "No profile data found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error fetching profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Save user profile data to Firestore
    private void saveUserProfile() {
        String name = nameEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String birthday = birthdayEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String avatarUrl = avatarUrlEditText.getText().toString();

        if (name.isEmpty() || username.isEmpty() || birthday.isEmpty() || phoneNumber.isEmpty() || avatarUrl.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid(); // Get current user's UID

        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("name", name);
        userProfile.put("username", username);
        userProfile.put("birthday", birthday);
        userProfile.put("phoneNumber", phoneNumber);
        userProfile.put("avatarUrl", avatarUrl);

        db.collection("users").document(userId)
                .set(userProfile) // Save user profile data
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error saving profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
