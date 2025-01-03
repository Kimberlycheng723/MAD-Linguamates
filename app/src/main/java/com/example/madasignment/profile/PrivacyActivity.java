package com.example.madasignment.profile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
import com.google.firebase.auth.FirebaseAuth;

public class PrivacyActivity extends AppCompatActivity {
    private EditText newPasswordField;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        newPasswordField = findViewById(R.id.etp_password_pr);
        mAuth = FirebaseAuth.getInstance();

        // Listen for "done" action on the keyboard
        newPasswordField.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String newPassword = newPasswordField.getText().toString().trim();
                if (newPassword.isEmpty()) {
                    Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    updatePassword(newPassword);
                }
                return true;
            }
            return false;
        });

        // Alternatively, use a TextWatcher for real-time validation or updates
        newPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String newPassword = s.toString().trim();
                if (!newPassword.isEmpty()) {
                    updatePassword(newPassword);
                }
            }
        });
    }

    private void updatePassword(String newPassword) {
        mAuth.getCurrentUser().updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error updating password: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
