package com.example.madasignment.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.madasignment.R;

import java.util.ArrayList;
import java.util.List;

public class Flashcard extends AppCompatActivity {

    private ViewPager2 viewPager;
    private FlashcardAdapter adapter;
    private ProgressBar progressBar;
    private TextView progressText;
    private ImageButton prevButton, nextButton;
    private TextView scrollInstructionText;


    private int totalCards = 5; // Total number of cards

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        // Initialize Views
        ImageView navigationIcon = findViewById(R.id.navigationIcon);
        navigationIcon.setOnClickListener(v -> {
            onBackPressed(); // Navigate back
        });
        viewPager = findViewById(R.id.viewPager);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);



        // Set up the ProgressBar
        progressBar.setMax(totalCards); // Total cards
        progressBar.setProgress(1); // Start progress
        progressText.setText("1/" + totalCards); // Start progress text

        // Sample data for multiple cards
        List<FlashcardData> cardDataList = new ArrayList<>();
        cardDataList.add(new FlashcardData("Card 1", R.drawable.flashcard_morning, R.drawable.flashcard_back_morning,R.drawable.flashcard_description_morning,R.raw.good_morning));
        cardDataList.add(new FlashcardData("Card 2", R.drawable.flashcard_afternoon, R.drawable.flashcard_back_afternoon,R.drawable.flashcard_afternoon_description,R.raw.good_afternoon));
        cardDataList.add(new FlashcardData("Card 3", R.drawable.flashcard_night,R.drawable.flashcard_back_night, R.drawable.flashcard_night_description,R.raw.good_night));
        cardDataList.add(new FlashcardData("Card 4", R.drawable.flashcard_welcome,R.drawable.flashcard_back_welcome, R.drawable.flashcard_description_welcome,R.raw.welcome));
        cardDataList.add(new FlashcardData("Card 5", R.drawable.flashcard_hello, R.drawable.flashcard_back_hello,R.drawable.flashcard_description_hello,R.raw.hello));

        // Set up the adapter
        adapter = new FlashcardAdapter(this, cardDataList);
        viewPager.setAdapter(adapter);

        // Handle page changes in ViewPager2
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateProgress(position + 1); // Update progress when page changes

                // Change the icon of the Next button on the last card
                if (position == adapter.getItemCount() - 1) {
                    nextButton.setImageResource(R.drawable.flashcard_finish_button); // Finish icon
                    nextButton.setContentDescription("Finish");
                } else {
                    nextButton.setImageResource(R.drawable.flashcard_next_button); // Default Next icon
                    nextButton.setContentDescription("Next");
                }
            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Hide the instruction text
                    scrollInstructionText.setVisibility(View.GONE);
                }
                return false; // Allow the event to propagate to ViewPager2
            }
        });
        // Handle Previous Button Click
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem > 0) {
                    viewPager.setCurrentItem(currentItem - 1, true); // Move to previous card
                }
            }
        });

        // Handle Next Button Click
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem < adapter.getItemCount() - 1) {
                    viewPager.setCurrentItem(currentItem + 1, true); // Move to next card
                } else {
                    // Navigate to the Finish splash screen
                    Intent intent = new Intent(Flashcard.this, FinishSplashActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    // Update ProgressBar and Text
    private void updateProgress(int currentCard) {
        progressBar.setProgress(currentCard);
        progressText.setText(currentCard + "/" + totalCards);
    }
}
