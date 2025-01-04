package com.example.madasignment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
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
    private EditText etName, etUsername, etBirthday, etEmail, etPhoneNumber;
    private Button btnDeleteAccount, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-linguamates-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference();

        // Initialize views
        etName = findViewById(R.id.et_name_ep);
        etUsername = findViewById(R.id.et_username_ep);
        etBirthday = findViewById(R.id.et_birthday_ep);
        etEmail = findViewById(R.id.et_email_ep);
        etPhoneNumber = findViewById(R.id.et_num_ep);
        btnDeleteAccount = findViewById(R.id.blt_dlt_ep);
        btnBack = findViewById(R.id.btn_back_ep);

        // Fetch and Display Data
        fetchUserData();

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

                            String num = snapshot.child("phone_number").getValue(String.class);
                            etPhoneNumber.setText(num != null ? num : "None");

                            String bday = snapshot.child("birthday").getValue(String.class);
                            etBirthday.setText(bday != null ? bday : "None");

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

    private void deleteAccount() {
        // Add Firebase account deletion logic
        Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();

        // Redirect to the login page or main page
        Intent intent = new Intent(EditProfileActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }
}

