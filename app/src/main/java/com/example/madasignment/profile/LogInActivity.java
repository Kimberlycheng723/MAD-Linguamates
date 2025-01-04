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
import com.example.madasignment.home.lesson_unit.lesson_unit.LessonUnit;
import com.google.firebase.auth.FirebaseAuth;

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
                        Intent intent = new Intent(LogInActivity.this, LessonUnit.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage;
                        if (task.getException() != null) {
                            errorMessage = task.getException().getMessage();
                        } else {
                            errorMessage = "Unknown error occurred";
                        }
                        Toast.makeText(LogInActivity.this, "Log-In Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
