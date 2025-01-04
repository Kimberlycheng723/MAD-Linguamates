package com.example.madasignment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import com.example.madasignment.profile.SignInActivity;
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

        // Use Handler to add a delay after the animation ends
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Navigate to MainActivity after the delay
            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            finish();
        }, 3000); // 5000ms = 5 seconds delay
    }
}