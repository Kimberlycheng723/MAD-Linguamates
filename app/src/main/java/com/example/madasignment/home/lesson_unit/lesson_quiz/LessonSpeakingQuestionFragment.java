package com.example.madasignment.home.lesson_unit.lesson_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.madasignment.R;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;

public class LessonSpeakingQuestionFragment extends Fragment {

    private static final int SPEECH_REQUEST_CODE = 0;

    private String correctPronunciation = "晚上好"; // Example correct pronunciation in Chinese characters

    public static LessonSpeakingQuestionFragment newInstance(String chineseText, String pinyin, int imageResId) {
        LessonSpeakingQuestionFragment fragment = new LessonSpeakingQuestionFragment();
        Bundle args = new Bundle();
        args.putString("CHINESE_TEXT", chineseText);
        args.putString("PINYIN", pinyin);
        args.putInt("IMAGE_RES_ID", imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_question_speaking, container, false);

        TextView chineseText = view.findViewById(R.id.chineseText);
        TextView pinyinText = view.findViewById(R.id.pinyinText);
        ImageView pronounceImage = view.findViewById(R.id.pronounceImage);
        ImageView micButton = view.findViewById(R.id.micButton);
        TextView cantSpeakNow = view.findViewById(R.id.cantSpeakNow);

        // Load arguments
        if (getArguments() != null) {
            chineseText.setText(getArguments().getString("CHINESE_TEXT"));
            pinyinText.setText(getArguments().getString("PINYIN"));
            pronounceImage.setImageResource(getArguments().getInt("IMAGE_RES_ID"));
        }

        // Set click listener for mic button
        micButton.setOnClickListener(v -> startSpeechRecognition());

        // Set click listener for "Can't Speak Now"
        cantSpeakNow.setOnClickListener(v -> {
            if (getActivity() instanceof LessonQuestionFragmentCallback) {
                ((LessonQuestionFragmentCallback) getActivity()).onQuestionSkipped();
            }
        });

        return view;
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh-CN"); // Set language to Simplified Chinese
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please pronounce the word in Mandarin Chinese...");

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Speech recognition not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String userSpeech = results.get(0).trim(); // Recognized speech
                Log.d("SpeechRecognition", "Recognized speech: " + userSpeech);

                // Compare with correct pronunciation
                if (isPronunciationCorrect(userSpeech, correctPronunciation)) {
                    Toast.makeText(getContext(), "Correct pronunciation!", Toast.LENGTH_SHORT).show();
                    if (getActivity() instanceof LessonQuestionFragmentCallback) {
                        ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(true);
                    }
                } else {
                    Toast.makeText(getContext(), "Incorrect pronunciation. Try again.", Toast.LENGTH_SHORT).show();
                    if (getActivity() instanceof LessonQuestionFragmentCallback) {
                        ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(false);
                    }
                }
            } else {
                Toast.makeText(getContext(), "Couldn't recognize speech. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isPronunciationCorrect(String userSpeech, String correctPronunciation) {
        // Normalize input
        userSpeech = userSpeech.trim().toLowerCase();
        correctPronunciation = correctPronunciation.trim().toLowerCase();

        // Direct match
        if (userSpeech.equals(correctPronunciation)) {
            return true;
        }

        // Convert to Pinyin and compare
        String userSpeechInPinyin = convertToPinyin(userSpeech);
        String correctPronunciationInPinyin = convertToPinyin(correctPronunciation);

        if (userSpeechInPinyin.equals(correctPronunciationInPinyin)) {
            return true;
        }

        // Fallback to similarity check
        return calculateSimilarity(userSpeechInPinyin, correctPronunciationInPinyin) > 0.85; // 85% similarity threshold
    }

    private double calculateSimilarity(String str1, String str2) {
        int levenshteinDistance = getLevenshteinDistance(str1, str2);
        int maxLength = Math.max(str1.length(), str2.length());
        return 1.0 - ((double) levenshteinDistance / maxLength);
    }

    private int getLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1)
                    );
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    private String convertToPinyin(String chineseText) {
        StringBuilder pinyinBuilder = new StringBuilder();

        for (char c : chineseText.toCharArray()) {
            // Get the Pinyin for the current character
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
            if (pinyinArray != null && pinyinArray.length > 0) {
                pinyinBuilder.append(pinyinArray[0]);
            } else {
                // Append character as is if no Pinyin is found
                pinyinBuilder.append(c);
            }
        }

        return pinyinBuilder.toString().toLowerCase();
    }
}
