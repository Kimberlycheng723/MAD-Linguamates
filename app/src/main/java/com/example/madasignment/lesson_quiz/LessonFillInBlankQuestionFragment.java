package com.example.madasignment.lesson_quiz;

import android.os.Bundle;
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

public class LessonFillInBlankQuestionFragment extends Fragment {

    private static final String ARG_QUESTION = "question";
    private static final String ARG_IMAGE = "imageResId";
    private static final String ARG_CORRECT_OPTION = "correctOption";

    private String question;
    private int imageResId;
    private String correctOption;
    private ImageView selectedButton = null; // Track the selected button

    public static LessonFillInBlankQuestionFragment newInstance(String question, int imageResId, String correctOption) {
        LessonFillInBlankQuestionFragment fragment = new LessonFillInBlankQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putInt(ARG_IMAGE, imageResId);
        args.putString(ARG_CORRECT_OPTION, correctOption);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = getArguments().getString(ARG_QUESTION);
            imageResId = getArguments().getInt(ARG_IMAGE);
            correctOption = getArguments().getString(ARG_CORRECT_OPTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_question_fill_in_blank, container, false);

        // Initialize views
        ImageView questionImage = view.findViewById(R.id.questionImage);
        TextView questionText = view.findViewById(R.id.questionText);
        ImageView option1 = view.findViewById(R.id.option1Background);
        ImageView option2 = view.findViewById(R.id.option2Background);
        ImageView option3 = view.findViewById(R.id.option3Background);
        ImageView checkButton = view.findViewById(R.id.checkButton);

        // Set question and image data
        questionImage.setImageResource(imageResId);
        questionText.setText(question);

        // Handle option clicks
        View.OnClickListener optionClickListener = v -> {
            ImageView clickedButton = (ImageView) v;

            // Change the background of the clicked button
            if (selectedButton != null) {
                selectedButton.setImageResource(R.drawable.lesson_button_not_selected);
            }
            clickedButton.setImageResource(R.drawable.lesson_button_selected);
            selectedButton = clickedButton;
        };

        option1.setOnClickListener(optionClickListener);
        option2.setOnClickListener(optionClickListener);
        option3.setOnClickListener(optionClickListener);

        // Handle check button click
        checkButton.setOnClickListener(v -> {
            if (selectedButton == null) {
                Toast.makeText(getContext(), "Please select an option!", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedOption = "";
            if (selectedButton == option1) selectedOption = "谢谢";
            else if (selectedButton == option2) selectedOption = "晚上好";
            else if (selectedButton == option3) selectedOption = "早上好";

            // Check if the answer is correct
            if (selectedOption.equals(correctOption)) {
                if (getActivity() instanceof LessonQuestionFragmentCallback) {
                    ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(true);
                }
            } else {
                if (getActivity() instanceof LessonQuestionFragmentCallback) {
                    ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(false);
                }
            }
        });


        return view;
    }
}
