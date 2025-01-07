package com.example.madasignment.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.example.madasignment.gamification.badge.BadgeFirebaseModel;
import com.example.madasignment.home.lesson_unit.lesson_unit.LessonUnit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {


    private EditText etEmail, etPassword;
    private Button btnLogin, btnForgotPassword;
    private ImageView ivTogglePassword;
    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ImageButton upButton = findViewById(R.id.upButton);

        upButton.setOnClickListener(v -> {
            // Navigate back
            Intent intent = new Intent(LogInActivity.this, SignInActivity.class);
            startActivity(intent);

        });

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            // User is already logged in, skip login screen
            navigateToLessonUnit();
        }
        // Initialize Firebase
        auth = FirebaseAuth.getInstance();

        // Initialize views
        etEmail = findViewById(R.id.et_email_li);
        etPassword = findViewById(R.id.et_pass_li);
        btnLogin = findViewById(R.id.btn_login_li);
        btnForgotPassword = findViewById(R.id.btn_forgot_password);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);

        // Handle Login Button Click
        btnLogin.setOnClickListener(v -> loginUser());

        // Handle Forgot Password Button Click
        btnForgotPassword.setOnClickListener(v -> resetPassword());

        // Handle Password Visibility Toggle
        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
    }



    private void navigateToLessonUnit() {
        Intent intent = new Intent(LogInActivity.this, LessonUnit.class);
        startActivity(intent);
        finish();
    }
    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            Toast.makeText(this, getString(R.string.email_required), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            Toast.makeText(this, getString(R.string.password_required), Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        initializeBadgesInDatabase();
                        Intent intent = new Intent(LogInActivity.this, LessonUnit.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = "Login failed: ";
                        if (task.getException() != null) {
                            errorMessage += task.getException().getMessage();
                        } else {
                            errorMessage += "Unknown error occurred";
                        }
                        Toast.makeText(LogInActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("LogInActivity", errorMessage);
                    }
                });
    }

    private void resetPassword() {
        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email to reset password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LogInActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LogInActivity.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void togglePasswordVisibility() {
        if (etPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivTogglePassword.setImageResource(R.drawable.ic_visibility); // Update with your eye icon drawable
        } else {
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ivTogglePassword.setImageResource(R.drawable.ic_visibility_off); // Update with your crossed eye icon drawable
        }
        etPassword.setSelection(etPassword.getText().length());
    }

    private void initializeBadgesInDatabase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference badgesRef = FirebaseDatabase.getInstance()
                .getReference("UserStats")
                .child(userId)
                .child("badges");

        badgesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.d("BadgeInit", "No badges found. Initializing for new user...");
                    badgesRef.child("First Lesson").setValue(new BadgeFirebaseModel("locked", 0, 1));
                    badgesRef.child("5 Lessons").setValue(new BadgeFirebaseModel("locked", 0, 5));
                    badgesRef.child("10 Lessons").setValue(new BadgeFirebaseModel("locked", 0, 10));
                    badgesRef.child("First Test").setValue(new BadgeFirebaseModel("locked", 0, 1));
                    badgesRef.child("3 Tests").setValue(new BadgeFirebaseModel("locked", 0, 3));
                    badgesRef.child("5 Tests").setValue(new BadgeFirebaseModel("locked", 0, 5));
                    badgesRef.child("3-Day Streak").setValue(new BadgeFirebaseModel("locked", 0, 3));
                    badgesRef.child("5-Day Streak").setValue(new BadgeFirebaseModel("locked", 0, 5));
                    badgesRef.child("7-Day Streak").setValue(new BadgeFirebaseModel("locked", 0, 7));
                } else {
                    Log.d("BadgeInit", "Badges already exist. Skipping initialization.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseError", "Failed to check badges: " + error.getMessage());
            }
        });
    }
}
