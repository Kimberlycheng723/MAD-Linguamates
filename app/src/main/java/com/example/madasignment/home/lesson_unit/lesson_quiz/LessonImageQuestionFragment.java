package com.example.madasignment.home.lesson_unit.lesson_quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.madasignment.R;

public class LessonImageQuestionFragment extends Fragment {

    private static final String ARG_QUESTION = "question";
    private static final String ARG_OPTIONS = "options";
    private static final String ARG_IMAGE_RES_IDS = "imageResIds";

    private String question;
    private String[] options;
    private int[] imageResIds; // Resource IDs for the images
    private String selectedOption = null; // Track selected option

    public static LessonImageQuestionFragment newInstance(String question, String[] options, int[] imageResIds) {
        LessonImageQuestionFragment fragment = new LessonImageQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putStringArray(ARG_OPTIONS, options);
        args.putIntArray(ARG_IMAGE_RES_IDS, imageResIds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = getArguments().getString(ARG_QUESTION);
            options = getArguments().getStringArray(ARG_OPTIONS);
            imageResIds = getArguments().getIntArray(ARG_IMAGE_RES_IDS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_image_question, container, false);

        TextView questionText = view.findViewById(R.id.questionText);
        GridLayout optionsLayout = view.findViewById(R.id.imageOptions);
        ImageView checkButton = view.findViewById(R.id.checkButton);

        // Set question text
        questionText.setText(question);

        // Dynamically add options
        for (int i = 0; i < options.length; i++) {
            FrameLayout optionLayout = (FrameLayout) inflater.inflate(R.layout.fragment_lesson_image_item_image_option, optionsLayout, false);

            ImageView optionImage = optionLayout.findViewById(R.id.optionImage);
            TextView optionText = optionLayout.findViewById(R.id.optionText);

            // Set the image and text dynamically
            optionImage.setImageResource(imageResIds[i]);
            optionText.setText(options[i]);

            // Handle option selection
            int finalI = i;
            optionLayout.setOnClickListener(v -> {
                resetAllOptions(optionsLayout); // Reset other options
                optionLayout.setBackgroundResource(R.drawable.lesson_image_button_selected); // Highlight selected option
                selectedOption = options[finalI]; // Save selected option
            });

            // Add the option to the layout
            optionsLayout.addView(optionLayout);
        }

        // Check button logic
        checkButton.setOnClickListener(v -> {
            if (selectedOption == null) {
                // Notify parent activity of an incorrect answer
                if (getActivity() instanceof LessonQuestionFragmentCallback) {
                    ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(false);
                }
            } else {
                boolean isCorrect = checkAnswer(selectedOption);
                if (getActivity() instanceof LessonQuestionFragmentCallback) {
                    ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(isCorrect);
                }
            }
        });


        return view;
    }

    // Reset all options' backgrounds
    private void resetAllOptions(GridLayout optionsLayout) {
        for (int i = 0; i < optionsLayout.getChildCount(); i++) {
            View child = optionsLayout.getChildAt(i);
            child.setBackgroundResource(R.drawable.lesson_image_button_not_selected);
        }
    }

    // Example validation logic
    private boolean checkAnswer(String selectedOption) {
        return selectedOption.equalsIgnoreCase(options[0]); // Assume options[0] is the correct answer
    }


    }

