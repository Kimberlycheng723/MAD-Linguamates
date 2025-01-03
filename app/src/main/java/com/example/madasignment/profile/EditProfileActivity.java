package com.example.madasignment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etName, etUsername, etBirthday, etEmail, etPhoneNumber;
    private Button btnDeleteAccount, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        etName = findViewById(R.id.et_name_ep);
        etUsername = findViewById(R.id.et_username_ep);
        etBirthday = findViewById(R.id.et_birthday_ep);
        etEmail = findViewById(R.id.et_email_ep);
        etPhoneNumber = findViewById(R.id.et_num_ep);
        btnDeleteAccount = findViewById(R.id.blt_dlt_ep);
        btnBack = findViewById(R.id.btn_back_ep);

//        // Load existing data (this is an example; adjust based on your Firebase integration)
//        loadUserData();

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
//
//    private void loadUserData() {
//        // Placeholder for loading data from Firebase
//        etName.setText("John Doe");
//        etUsername.setText("johndoe");
//        etBirthday.setText("01/01/2000");
//        etEmail.setText("johndoe@example.com");
//        etPhoneNumber.setText("1234567890");
//    }

    private void deleteAccount() {
        // Add Firebase account deletion logic
        Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();

        // Redirect to the login page or main page
        Intent intent = new Intent(EditProfileActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }
}

