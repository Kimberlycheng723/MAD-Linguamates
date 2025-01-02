package com.example.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnLogin, btnSignup;
    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        etName = findViewById(R.id.et_name_si);
        etEmail = findViewById(R.id.et_email_si);
        etPassword = findViewById(R.id.et_pass_si);
        btnLogin = findViewById(R.id.btn_login_si);
        btnSignup = findViewById(R.id.btn_signup_si);

        // Set login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });

        // Set sign-up button click listener (redirect to a sign-up activity)
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("name", name);
                            userMap.put("email", email);
                            userMap.put("password", password); // Avoid storing plain text passwords

                            database.child("users").child(userId).setValue(userMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(SignInActivity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignInActivity.this, LogInActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, "Sign-Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // todo: change to redirect login page
//    private void handleLogin() {
//        String name = etName.getText().toString().trim();
//        String email = etEmail.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        if (TextUtils.isEmpty(name)) {
//            etName.setError("Name is required");
//            return;
//        }
//
//        if (TextUtils.isEmpty(email)) {
//            etEmail.setError("Email is required");
//            return;
//        }
//
//        if (TextUtils.isEmpty(password)) {
//            etPassword.setError("Password is required");
//            return;
//        }
//
//        // Perform authentication (replace with actual authentication logic)
//        if (email.equals("test@example.com") && password.equals("password123")) {
//            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
//            // Navigate to another activity on success
//            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void navigateToLogin() {
        // Navigate to the log-in activity
        Intent intent = new Intent(SignInActivity.this, LogInActivity.class);
        startActivity(intent);
    }
}
