package com.example.madasignment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmailActivity extends AppCompatActivity {

    private TextView tvVerify, tvCheck;
    private Button btnVerify, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);  // Use the correct layout file

        tvVerify = findViewById(R.id.tv_verify_ve);
        tvCheck = findViewById(R.id.tv_check_ve);
        btnVerify = findViewById(R.id.btn_verify_ve);
        btnNext = findViewById(R.id.btn_nextPage_ve);

        // Get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Check if user is signed in and if email is verified
        if (user != null) {
            if (user.isEmailVerified()) {
                // Email is already verified, proceed to main screen
                startActivity(new Intent(VerifyEmailActivity.this, LogInActivity.class));
                finish();
            } else {
                // If email is not verified, prompt the user
                Toast.makeText(VerifyEmailActivity.this, "Please verify your email.", Toast.LENGTH_SHORT).show();
            }
        }

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(VerifyEmailActivity.this, LogInActivity.class);
            startActivity(intent);
        });

        // When the user clicks on the Verify button, send the verification email
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    // Send a verification email again if needed
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(VerifyEmailActivity.this, "Verification email sent!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(VerifyEmailActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        // Periodically check if the email is verified
        checkEmailVerificationStatus();
    }

    private void checkEmailVerificationStatus() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Recheck email verification status periodically
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (user.isEmailVerified()) {
                            // If email is verified, redirect to the Login page
                            startActivity(new Intent(VerifyEmailActivity.this, LogInActivity.class));
                            finish();
                        } else {
                            // If email is not verified, prompt the user again
                            Toast.makeText(VerifyEmailActivity.this, "Please verify your email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
