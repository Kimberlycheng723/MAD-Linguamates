package com.example.madasignment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button btnLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();

        // Initialize views
        etEmail = findViewById(R.id.et_email_li);
        etPassword = findViewById(R.id.et_pass_li);
        btnLogin = findViewById(R.id.btn_login_li);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
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
