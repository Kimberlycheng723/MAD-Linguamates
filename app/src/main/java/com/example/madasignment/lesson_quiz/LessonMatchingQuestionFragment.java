package com.example.madasignment.lesson_quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.madasignment.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LessonMatchingQuestionFragment extends Fragment {

    private static final String ARG_QUESTION = "question";
    private static final String ARG_WORDS = "words";
    private static final String ARG_TRANSLATIONS = "translations";

    private String question;
    private List<String> words;
    private List<String> translations;
    private HashMap<String, String> correctMatches = new HashMap<>();
    private HashMap<String, String> selectedMatches = new HashMap<>();
    private String selectedEnglishWord = null;
    private Button lastSelectedTranslationButton = null;

    public static LessonMatchingQuestionFragment newInstance(String question, List<String> words, List<String> translations) {
        LessonMatchingQuestionFragment fragment = new LessonMatchingQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putStringArrayList(ARG_WORDS, new ArrayList<>(words));
        args.putStringArrayList(ARG_TRANSLATIONS, new ArrayList<>(translations));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = getArguments().getString(ARG_QUESTION);
            words = getArguments().getStringArrayList(ARG_WORDS);
            translations = getArguments().getStringArrayList(ARG_TRANSLATIONS);

            // Populate correct matches
            for (int i = 0; i < words.size(); i++) {
                correctMatches.put(words.get(i), translations.get(i));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_question_matching, container, false);

        GridLayout matchingGridLayout = view.findViewById(R.id.matchingGridLayout);
        ImageView submitButton = view.findViewById(R.id.submitButton);

        // Shuffle words and translations
        Collections.shuffle(words);
        Collections.shuffle(translations);

        // Add English words
        // Adding English words
        for (String word : words) {
            FrameLayout optionLayout = createOptionButton(inflater, word, true);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(32, 64, 32, 64); // Set margins
            optionLayout.setLayoutParams(params); // Apply the margins to the layout
            matchingGridLayout.addView(optionLayout); // Add to GridLayout
        }

// Adding translations
        for (String translation : translations) {
            FrameLayout translationLayout = createOptionButton(inflater, translation, false);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(32, 64, 32, 64); // Set margins
            translationLayout.setLayoutParams(params); // Apply the margins to the layout
            matchingGridLayout.addView(translationLayout); // Add to GridLayout
        }


        // Submit button logic
        submitButton.setOnClickListener(v -> {
            if (selectedMatches.size() < words.size()) {
                Toast.makeText(getContext(), "Please complete all matches.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isCorrect = validateMatches();

            // Notify the activity about the result
            if (getActivity() instanceof LessonQuestionFragmentCallback) {
                ((LessonQuestionFragmentCallback) getActivity()).onQuestionAnswered(isCorrect);
            }
        });


        return view;
    }

    private FrameLayout createOptionButton(LayoutInflater inflater, String text, boolean isWord) {
        FrameLayout optionLayout = (FrameLayout) inflater.inflate(R.layout.lesson_matching_item_matching_pair, null);

        ImageView optionImage = optionLayout.findViewById(R.id.matchingOptionButton);
        TextView optionText = optionLayout.findViewById(R.id.matchingOptionText);

        optionText.setText(text);

        optionImage.setOnClickListener(v -> {
            if (isWord) {
                // Handle English word selection
                if (selectedEnglishWord != null && selectedEnglishWord.equals(text)) {
                    // Deselect if clicked again
                    selectedEnglishWord = null;
                    optionImage.setBackgroundResource(R.drawable.lesson_matching_not_selected);
                } else {
                    // Reset the background of the previous English word
                    resetEnglishSelection();

                    // Select the new English word
                    selectedEnglishWord = text;
                    optionImage.setBackgroundResource(R.drawable.lesson_matching_selected);
                }
            } else {
                // Handle Chinese translation selection
                if (selectedEnglishWord != null) {
                    // Check if already matched
                    if (selectedMatches.containsKey(selectedEnglishWord)) {
                        Toast.makeText(getContext(), "This word is already matched.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (selectedMatches.containsValue(text)) {
                        Toast.makeText(getContext(), "This translation is already matched.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Validate the match
                    if (correctMatches.get(selectedEnglishWord).equals(text)) {
                        // Correct match
                        selectedMatches.put(selectedEnglishWord, text);

                        // Lock both options and turn them green
                        lockOption(findOptionLayoutByText(selectedEnglishWord, true), R.drawable.lesson_matching_green_button);
                        lockOption(optionLayout, R.drawable.lesson_matching_green_button);

                        // Clear the selected English word
                        selectedEnglishWord = null;
                    } else {
                        // Incorrect match
                        optionImage.setBackgroundResource(R.drawable.lesson_matching_red_button);
                        findOptionLayoutByText(selectedEnglishWord, true).findViewById(R.id.matchingOptionButton)
                                .setBackgroundResource(R.drawable.lesson_matching_red_button);

                        Toast.makeText(getContext(), "Incorrect Match. Try Again.", Toast.LENGTH_SHORT).show();

                        // Reset only the current selection
                        resetSingleSelection();
                    }
                } else {
                    // Prompt user to select an English word first
                    Toast.makeText(getContext(), "Select an English word first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return optionLayout;
    }


    private void resetSingleSelection() {
        GridLayout matchingGridLayout = getView().findViewById(R.id.matchingGridLayout);
        for (int i = 0; i < matchingGridLayout.getChildCount(); i++) {
            FrameLayout optionLayout = (FrameLayout) matchingGridLayout.getChildAt(i);
            ImageView optionImage = optionLayout.findViewById(R.id.matchingOptionButton);

            // Reset red buttons back to default
            if (optionImage.getBackground() != null &&
                    optionImage.getBackground().getConstantState().equals(
                            getResources().getDrawable(R.drawable.lesson_matching_red_button).getConstantState())) {
                optionImage.setBackgroundResource(R.drawable.lesson_matching_not_selected);
            }
        }
    }


    private void resetSelection(boolean resetWords) {
        View rootView = getView();
        if (rootView == null) return;

        GridLayout matchingGridLayout = rootView.findViewById(R.id.matchingGridLayout);
        if (matchingGridLayout == null) return;

        for (int i = 0; i < matchingGridLayout.getChildCount(); i++) {
            FrameLayout optionLayout = (FrameLayout) matchingGridLayout.getChildAt(i);
            ImageView optionImage = optionLayout.findViewById(R.id.matchingOptionButton);

            if (resetWords) {
                String optionText = ((TextView) optionLayout.findViewById(R.id.matchingOptionText)).getText().toString();
                if (!selectedMatches.containsKey(optionText)) {
                    optionImage.setBackgroundResource(R.drawable.lesson_matching_not_selected);
                }
            }
        }
    }


    private void resetEnglishSelection() {
        GridLayout matchingGridLayout = getView().findViewById(R.id.matchingGridLayout);
        for (int i = 0; i < matchingGridLayout.getChildCount(); i++) {
            FrameLayout optionLayout = (FrameLayout) matchingGridLayout.getChildAt(i);
            ImageView optionImage = optionLayout.findViewById(R.id.matchingOptionButton);
            TextView optionText = optionLayout.findViewById(R.id.matchingOptionText);

            String text = optionText.getText().toString();

            if (words.contains(text) && !selectedMatches.containsKey(text)) {
                // Reset the background of unmatched English words
                optionImage.setBackgroundResource(R.drawable.lesson_matching_not_selected);
            }
        }
    }



    private boolean validateMatches() {
        for (String word : words) {
            if (!selectedMatches.containsKey(word) || !correctMatches.get(word).equals(selectedMatches.get(word))) {
                return false;
            }
        }
        return true;
    }

    private void lockOption(FrameLayout optionLayout, int greenDrawable) {
        ImageView optionImage = optionLayout.findViewById(R.id.matchingOptionButton);
        optionImage.setOnClickListener(null); // Disable click
        optionImage.setBackgroundResource(greenDrawable); // Show green background
    }




    private FrameLayout findOptionLayoutByText(String text, boolean isWord) {
        GridLayout matchingGridLayout = getView().findViewById(R.id.matchingGridLayout);
        if (matchingGridLayout == null) return null;

        for (int i = 0; i < matchingGridLayout.getChildCount(); i++) {
            FrameLayout optionLayout = (FrameLayout) matchingGridLayout.getChildAt(i);
            TextView optionText = optionLayout.findViewById(R.id.matchingOptionText);

            if (optionText != null && optionText.getText().toString().equals(text)) {
                return optionLayout;
            }
        }
        return null;
    }


}
