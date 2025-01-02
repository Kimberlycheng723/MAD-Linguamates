package com.example.madasignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.lesson_unit.LessonUnit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    private EditText mail, password;
    private Button login, signUp;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_try); // Ensure this is the correct layout file

        // Initialize views
        mail = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.buttonLogin);
        signUp = findViewById(R.id.buttonSignUp);

        // Set click listeners
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = mail.getText().toString();
                String userPassword = password.getText().toString();

                signUpFirebase(userEmail,userPassword);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, LoginTry.class);
                startActivity(intent);
            }
        });
    }

    public void signUpFirebase(String userEmail, String userPassword){
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUp.this
                            , "Your account has been created", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignUp.this, LoginTry.class);
                            startActivity(i);
                            finish();

                        } else {
                            Toast.makeText(SignUp.this
                                    , "There is a problem", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    // The user no need to login the second time if they didn't click log out button
    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            Intent i = new Intent(SignUp.this, LessonUnit.class);
            startActivity(i);
            finish();

        }
    }
}
