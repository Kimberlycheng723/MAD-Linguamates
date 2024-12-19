package com.example.madasignment.lesson_quiz;

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

public class LessonSingleOptionQuestionFragment extends Fragment {

    private static final String ARG_QUESTION = "question";
    private static final String ARG_OPTIONS = "options";
    private static final String ARG_CORRECT_OPTION = "correctOption";

    private String question;
    private List<String> options;
    private String correctOption;
    private String selectedOption = null;

    public static LessonSingleOptionQuestionFragment newInstance(String question, List<String> options, String correctOption) {
        LessonSingleOptionQuestionFragment fragment = new LessonSingleOptionQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putStringArrayList(ARG_OPTIONS, new ArrayList<>(options));
        args.putString(ARG_CORRECT_OPTION, correctOption);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = getArguments().getString(ARG_QUESTION);
            options = getArguments().getStringArrayList(ARG_OPTIONS);
            correctOption = getArguments().getString(ARG_CORRECT_OPTION);

            // Shuffle the options for randomness
            Collections.shuffle(options);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_single_option, container, false);

        TextView questionText = view.findViewById(R.id.questionText);
        LinearLayout optionsContainer = view.findViewById(R.id.optionsContainer);
        ImageView checkButton = view.findViewById(R.id.checkButton);

        questionText.setText(question);

        // Dynamically add options
        for (String option : options) {
            View optionButton = createOptionButton(inflater, option);
            optionsContainer.addView(optionButton);
        }

        // Check button logic
        checkButton.setOnClickListener(v -> {
            if (selectedOption == null) {
                Toast.makeText(getContext(), "Please select an option.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (getActivity() instanceof LessonQuestionFragmentCallback) {
                if (selectedOption.equals(correctOption)) {
                    // Notify parent activity of correct answer
                    ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(true);
                } else {
                    // Notify parent activity of incorrect answer
                    ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(false);
                }
            }
        });


        return view;
    }

    private View createOptionButton(LayoutInflater inflater, String optionText) {
        // Inflate the FrameLayout
        View optionLayout = inflater.inflate(R.layout.item_option_button, null);

        // Access the TextView inside the FrameLayout
        TextView optionTextView = optionLayout.findViewById(R.id.optionText);
        optionTextView.setText(optionText);

        // Set click listener for the FrameLayout
        optionLayout.setOnClickListener(v -> {
            if (selectedOption != null && selectedOption.equals(optionText)) {
                // Deselect the current selection
                selectedOption = null;
                optionLayout.setBackgroundResource(R.drawable.lesson_button_not_selected);
            } else {
                // Reset all other options
                resetAllOptions();

                // Select the current option
                selectedOption = optionText;
                optionLayout.setBackgroundResource(R.drawable.lesson_button_selected);
            }
        });

        return optionLayout; // Return the FrameLayout
    }

    private void resetAllOptions() {
        LinearLayout optionsContainer = getView().findViewById(R.id.optionsContainer);
        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            View child = optionsContainer.getChildAt(i);
            child.setBackgroundResource(R.drawable.lesson_button_not_selected);
        }
    }
}
