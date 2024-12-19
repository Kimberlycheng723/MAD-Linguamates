package com.example.madasignment.lesson_quiz;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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

import java.util.ArrayList;
import java.util.Locale;

public class LessonSpeakingQuestionFragment extends Fragment {

    private static final int SPEECH_REQUEST_CODE = 0;

    private String correctPronunciation = "wan shang hao"; // Example for matching

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
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please pronounce the word...");

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Speech recognition not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String userSpeech = results.get(0).toLowerCase();
                if (getActivity() instanceof LessonQuestionFragmentCallback) {
                    if (userSpeech.equals(correctPronunciation)) {
                        ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(true);
                    } else {
                        ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(false);
                    }
                }

            }
            }
        }
    }

