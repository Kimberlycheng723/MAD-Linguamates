package com.example.madasignment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    // Declare variables
    private EditText etName, etEmail, etPassword, etPhone;
    private Button btnSignup, btnLogin;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-linguamates-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference();

        // Initialize UI components
        etName = findViewById(R.id.et_name_si);
        etEmail = findViewById(R.id.et_email_si);
        etPassword = findViewById(R.id.et_pass_si);
        etPhone = findViewById(R.id.et_num_si);

        btnSignup = findViewById(R.id.btn_signup_si);
        btnLogin = findViewById(R.id.btn_login_si);

        // Set Sign-Up button click listener
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Set Login button click listener (redirect to login activity)
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    private void registerUser() {
        // Get user inputs
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }
        if(TextUtils.isEmpty(phone)){
            etPhone.setError("Phone number is required");
            return;
        }

        createUser(name, email, password, phone);

        // Check if the phone number is already registered
//        databaseReference.child("User").orderByChild("phone").equalTo(phone)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            etPhone.setError("Phone number already registered");
//                        } else {
//                            // Proceed to register the user
//                            createUser(name, email, password, phone);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(SignInActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    // Create a new user
    private void createUser(String name, String email, String password, String phone) {
        // Register user with Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get current user
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            // Store user data in Firebase Realtime Database
                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("name", name);
                            userMap.put("email", email);
                            userMap.put("phone", phone);

                            // Prepare default user statistics
                            HashMap<String, Object> statsMap = new HashMap<>();
                            statsMap.put("iv_statlang_pp", "None");
                            statsMap.put("iv_statleague_pp", "None");
                            statsMap.put("iv_statlessons_pp", "0");
                            statsMap.put("iv_statstreak_pp", "0");

                            // Save User data
                            databaseReference.child("User").child(userId).setValue(userMap)
                                    .addOnCompleteListener(userTask  -> {
                                        if (userTask .isSuccessful()) {
                                            // Save User Stats data
                                            databaseReference.child("UserStats").child(userId).setValue(statsMap)
                                                    .addOnCompleteListener(statsTask -> {
                                                        if (statsTask.isSuccessful()) {
                                                            Toast.makeText(SignInActivity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(SignInActivity.this, LogInActivity.class));
                                                            finish();
                                                        } else {
                                                            Toast.makeText(SignInActivity.this, "Failed to save user statistics", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(SignInActivity.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Show error message
                        Toast.makeText(SignInActivity.this, "Sign-Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Navigate to Login Activity
    private void navigateToLogin() {
        // Redirect to Login Activity
        Intent intent = new Intent(SignInActivity.this, LogInActivity.class);
        startActivity(intent);
    }
}
