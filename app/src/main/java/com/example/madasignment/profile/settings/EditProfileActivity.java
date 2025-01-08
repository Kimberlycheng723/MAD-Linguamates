package com.example.madasignment.profile.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.example.madasignment.profile.ProfilePageActivity;
import com.example.madasignment.profile.signin_login.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private EditText etName, etUsername, etEmail, etPhoneNumber, etBio;
    private Button btnDeleteAccount, btnBack, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_profile);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-linguamates-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference();

        // Initialize views
        etName = findViewById(R.id.et_name_ep);
        etUsername = findViewById(R.id.et_username_ep);
        etEmail = findViewById(R.id.et_email_ep);
        etPhoneNumber = findViewById(R.id.et_num_ep);
        etBio = findViewById(R.id.et_bio_ep);
        btnDeleteAccount = findViewById(R.id.blt_dlt_ep);
        btnBack = findViewById(R.id.btn_back_ep);
        btnSave = findViewById(R.id.btn_save_ep);

        // Fetch and Display Data
        fetchUserData();

        // Save button click listener
        btnSave.setOnClickListener(v -> {
            saveUserData();
        });

        // Set up back button
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, ProfilePageActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up delete account button
        btnDeleteAccount.setOnClickListener(v -> {
            deleteAccount();
        });
    }



    private void fetchUserData() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null){
            String userId = firebaseUser.getUid();
            try {
                databaseReference.child("User").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Retrieve Data

                            String name = snapshot.child("name").getValue(String.class);
                            etName.setText(name != null ? name : "None");

                            String username = snapshot.child("username").getValue(String.class);
                            etUsername.setText(username != null ? username : "None");

                            String email = snapshot.child("email").getValue(String.class);
                            etEmail.setText(email != null ? email : "None");

                            String num = snapshot.child("phone").getValue(String.class);
                            etPhoneNumber.setText(num != null ? num : "None");

                            String bio = snapshot.child("Bio").getValue(String.class);
                            etBio.setText(bio != null ? bio : "None");

                            Log.d("ProfilePageActivity", "Snapshot: " + snapshot.toString());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle Database Error
                        Log.d("EditProfileActivity", "Error: " + error.getMessage());
                    }
                });
            }catch(Exception e) {
                Log.d("EditProfileActivity", "Exception: " + e.getMessage());
            }
        }
    }

    private void saveUserData() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            try {
                // Get updated data from EditTexts
                String updatedName = etName.getText().toString().trim();
                String updatedUsername = etUsername.getText().toString().trim();
                String updatedEmail = etEmail.getText().toString().trim();
                String updatedPhoneNumber = etPhoneNumber.getText().toString().trim();
                String updatedBio = etBio.getText().toString().trim();

                // Update data in Firebase
                databaseReference.child("User").child(userId).child("name").setValue(updatedName);
                databaseReference.child("User").child(userId).child("username").setValue(updatedUsername);
                databaseReference.child("User").child(userId).child("email").setValue(updatedEmail);
                databaseReference.child("User").child(userId).child("phone").setValue(updatedPhoneNumber);
                databaseReference.child("User").child(userId).child("Bio").setValue(updatedBio)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Failed to update profile. Try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                Log.d("EditProfileActivity", "Exception: " + e.getMessage());
            }
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteAccount() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                    // Redirect to the login page or main page
                    Intent intent = new Intent(EditProfileActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Failed to delete account. Try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

}

