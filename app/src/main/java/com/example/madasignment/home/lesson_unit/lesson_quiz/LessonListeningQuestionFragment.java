package com.example.madasignment.home.lesson_unit.lesson_quiz;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.madasignment.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LessonListeningQuestionFragment extends Fragment {

    private static final String ARG_AUDIO_RES_ID = "audioResId";
    private static final String ARG_OPTIONS = "options";
    private static final String ARG_CORRECT_OPTION = "correctOption";

    private int audioResId;
    private List<String> options;
    private String correctOption;
    private String selectedOption = null;
    private MediaPlayer mediaPlayer;

    public static LessonListeningQuestionFragment newInstance(int audioResId, List<String> options, String correctOption) {
        LessonListeningQuestionFragment fragment = new LessonListeningQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_AUDIO_RES_ID, audioResId);
        args.putStringArrayList(ARG_OPTIONS, new ArrayList<>(options));
        args.putString(ARG_CORRECT_OPTION, correctOption);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            audioResId = getArguments().getInt(ARG_AUDIO_RES_ID);
            options = getArguments().getStringArrayList(ARG_OPTIONS);
            correctOption = getArguments().getString(ARG_CORRECT_OPTION);

            // Shuffle the options for randomness
            Collections.shuffle(options);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_question_listening, container, false);

        LinearLayout optionsContainer = view.findViewById(R.id.optionsContainer);
        ImageView playAudioButton = view.findViewById(R.id.playAudioButton);
        ImageView checkButton = view.findViewById(R.id.checkButton);
        TextView cantListenNow = view.findViewById(R.id.cantListenNow);

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(getContext(), audioResId);

        playAudioButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(mp -> {
                    // Reset the MediaPlayer to the beginning after playing
                    mp.seekTo(0);
                });

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause(); // Pause if playing
                    mediaPlayer.seekTo(0); // Reset to the beginning
                } else {
                    mediaPlayer.start(); // Start playback
                    Toast.makeText(getContext(), "Playing audio", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Audio not initialized", Toast.LENGTH_SHORT).show();
            }
        });

        // Dynamically add options
        for (String option : options) {
            View optionButton = createOptionButton(inflater, option);
            optionsContainer.addView(optionButton);
        }

        checkButton.setOnClickListener(v -> {
            if (selectedOption == null) {
                Toast.makeText(getContext(), "Please select an option.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isCorrect = selectedOption.equals(correctOption);
            if (getActivity() instanceof LessonQuestionFragmentCallback) {
                ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(isCorrect);
            }
        });

        // Skip question
        cantListenNow.setOnClickListener(v -> {
            if (getActivity() instanceof LessonQuestionFragmentCallback) {
                // Notify the activity to skip this question
                ((LessonQuestionFragmentCallback) getActivity()).onQuestionSkipped();
            }
        });

        return view;
    }

    private View createOptionButton(LayoutInflater inflater, String optionText) {
        View optionLayout = inflater.inflate(R.layout.item_option_button, null);

        TextView optionTextView = optionLayout.findViewById(R.id.optionText);
        optionTextView.setText(optionText);

        optionLayout.setOnClickListener(v -> {
            if (selectedOption != null && selectedOption.equals(optionText)) {
                // Deselect
                selectedOption = null;
                optionLayout.setBackgroundResource(R.drawable.lesson_button_not_selected);
            } else {
                // Reset other options
                resetAllOptions();

                // Select this option
                selectedOption = optionText;
                optionLayout.setBackgroundResource(R.drawable.lesson_button_selected);
            }
        });

        return optionLayout;
    }

    private void resetAllOptions() {
        LinearLayout optionsContainer = getView().findViewById(R.id.optionsContainer);
        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            View child = optionsContainer.getChildAt(i);
            child.setBackgroundResource(R.drawable.lesson_button_not_selected);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
