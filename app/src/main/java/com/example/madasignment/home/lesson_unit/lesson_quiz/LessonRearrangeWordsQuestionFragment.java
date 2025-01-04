package com.example.madasignment.home.lesson_unit.lesson_quiz;

import android.content.ClipData;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
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

public class LessonRearrangeWordsQuestionFragment extends Fragment {

    private static final String ARG_QUESTION = "question";
    private static final String ARG_CORRECT_ANSWER = "correctAnswer";

    private String question;
    private String correctAnswer;
    private List<String> words;
    private LinearLayout dropZone;

    public static LessonRearrangeWordsQuestionFragment newInstance(String question, String correctAnswer) {
        LessonRearrangeWordsQuestionFragment fragment = new LessonRearrangeWordsQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putString(ARG_CORRECT_ANSWER, correctAnswer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = getArguments().getString(ARG_QUESTION);
            correctAnswer = getArguments().getString(ARG_CORRECT_ANSWER);

            // Split correct answer into words and shuffle them
            words = new ArrayList<>();
            Collections.addAll(words, correctAnswer.split(""));
            Collections.shuffle(words);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_rearrange_words, container, false);

        TextView questionText = view.findViewById(R.id.questionText);
        LinearLayout dropZone = view.findViewById(R.id.dropZone);
        LinearLayout wordsContainer = view.findViewById(R.id.wordsContainer);
        ImageView submitButton = view.findViewById(R.id.submitButton); // Updated to ImageView

        questionText.setText(question);

        // Add words to the wordsContainer
        for (String word : words) {
            TextView wordView = createWordView(word);
            wordsContainer.addView(wordView);
        }

        // Set drag listener on dropZone
        dropZone.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundResource(R.drawable.lesson_rearrange_drop_box);
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundResource(R.drawable.lesson_rearrange_drop_box);
                    return true;
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    if (draggedView.getParent() == wordsContainer) {
                        ((ViewGroup) draggedView.getParent()).removeView(draggedView);
                        dropZone.addView(draggedView);
                    } else {
                        Toast.makeText(getContext(), "Invalid drop!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundResource(R.drawable.lesson_rearrange_drop_box);
                    return true;
                default:
                    return false;
            }
        });

        // Set drag listener on wordsContainer to allow dragging items back
        wordsContainer.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    if (draggedView.getParent() == dropZone) {
                        ((ViewGroup) draggedView.getParent()).removeView(draggedView);
                        wordsContainer.addView(draggedView);
                    } else {
                        Toast.makeText(getContext(), "Invalid drop!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                default:
                    return true;
            }
        });

        // Set submit button logic
        submitButton.setOnClickListener(v -> {
            StringBuilder answer = new StringBuilder();
            for (int i = 0; i < dropZone.getChildCount(); i++) {
                TextView textView = (TextView) dropZone.getChildAt(i);
                answer.append(textView.getText());
            }

            if (getActivity() instanceof LessonQuestionFragmentCallback) {
                if (answer.toString().equals(correctAnswer)) {
                    // Notify parent activity for correct answer
                    ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(true);
                } else {
                    // Notify parent activity for wrong answer
                    ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(false);
                    resetWords((LinearLayout) getView().findViewById(R.id.wordsContainer), dropZone);
                }
            }
        });


        return view;
    }


    // Helper to create TextView for a word
    private TextView createWordView(String word) {
        TextView wordView = new TextView(getContext());
        wordView.setText(word);
        wordView.setPadding(16, 16, 16, 16);
        wordView.setBackgroundResource(R.drawable.lesson_rearrange_option_box);
        wordView.setTextSize(18);
        wordView.setGravity(Gravity.CENTER);
        wordView.setOnLongClickListener(v -> {
            ClipData data = ClipData.newPlainText("word", word);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(data, shadowBuilder, v, 0);
            }
            return true;
        });
        return wordView;
    }

    // Helper to reset words back to original container
    private void resetWords(LinearLayout wordsContainer, LinearLayout dropZone) {
        while (dropZone.getChildCount() > 0) {
            View child = dropZone.getChildAt(0);
            dropZone.removeView(child);
            wordsContainer.addView(child);
        }
    }

}
