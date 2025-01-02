package com.example.madasignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madasignment.lesson_unit.LessonUnit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginTry extends AppCompatActivity {

    EditText mail;
    EditText password;
    Button enter;
    Button forgotPassword;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_try);

        mail=findViewById(R.id.editTextLoginEmail);
        password = findViewById(R.id.editTextLoginPassword);
        enter = findViewById(R.id.buttonEnter);
        forgotPassword = findViewById(R.id.buttonForgot);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userMail = mail.getText().toString();
                String userPassword = password.getText().toString();

                loginFirebase(userMail, userPassword);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginTry.this, ForgetActivity.class);
                startActivity(i);
            }
        });
    }


    public void loginFirebase (String userMail, String userPassword){
        auth.signInWithEmailAndPassword(userMail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent i = new Intent(LoginTry.this, LessonUnit.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(LoginTry.this, "Mail or Password is not correct", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}