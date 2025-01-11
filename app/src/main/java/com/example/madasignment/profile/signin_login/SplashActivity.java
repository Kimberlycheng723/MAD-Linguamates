package com.example.madasignment.profile.signin_login;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import com.example.madasignment.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_splash_screen);

        TextView titleText = findViewById(R.id.titleText);

        // Load fade-in animation
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        titleText.startAnimation(fadeInAnimation);
        titleText.setVisibility(View.VISIBLE);

        // Add a delay to navigate to LogInActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // 3-second delay
    }}
