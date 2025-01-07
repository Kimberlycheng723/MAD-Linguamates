package com.example.madasignment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.R;
//import com.example.madasignment.auth.LoginPage;
import com.example.madasignment.home.lesson_unit.lesson_unit.LessonUnit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LanguageSelectPage extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private RadioButton rbChinese;
    private Button btnSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languageselect_page);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-linguamates-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference();

        // Initialize UI Components
        rbChinese = findViewById(R.id.rb_chinese_ls);
        btnSave = findViewById(R.id.btn_save_ls);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLanguageAndNavigate();
            }
        });
    }

    private void saveLanguageAndNavigate() {
        // Validate if the radio button is selected
        if (!rbChinese.isChecked()) {
            Toast.makeText(this, "Please select the language before proceeding", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set the selected language
        String selectedLanguage = "Chinese";

        // Save to Firebase
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            databaseReference.child("UserStats").child(userId).child("language").setValue(selectedLanguage)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(LanguageSelectPage.this, "Language saved successfully", Toast.LENGTH_SHORT).show();
                        // Navigate to Login Page
                        Intent intent = new Intent(LanguageSelectPage.this, LessonUnit.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("LanguageSelectPage", "Failed to save language: " + e.getMessage());
                        Toast.makeText(LanguageSelectPage.this, "Failed to save language", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
