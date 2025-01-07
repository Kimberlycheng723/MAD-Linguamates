package com.example.madasignment.home.lesson_unit.test;

import androidx.fragment.app.Fragment;

import com.example.madasignment.R;
import com.example.madasignment.home.lesson_unit.lesson_quiz.LessonFillInBlankQuestionFragment;
import com.example.madasignment.home.lesson_unit.lesson_quiz.LessonImageQuestionFragment;
import com.example.madasignment.home.lesson_unit.lesson_quiz.LessonListeningQuestionFragment;
import com.example.madasignment.home.lesson_unit.lesson_quiz.LessonMatchingQuestionFragment;
import com.example.madasignment.home.lesson_unit.lesson_quiz.LessonRearrangeWordsQuestionFragment;
import com.example.madasignment.home.lesson_unit.lesson_quiz.LessonSingleOptionQuestionFragment;
import com.example.madasignment.home.lesson_unit.lesson_quiz.LessonSpeakingQuestionFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LessonTestFragmentFactory {
    public static List<Fragment> createQuestionFragments() {
        List<Fragment> fragments = new ArrayList<>();


     fragments.add(LessonImageQuestionFragment.newInstance(
                "Which image matches '你好'?",
                new String[]{"Hello", "Good Morning", "Good Night", "Thank You"},
                new int[]{
                        R.drawable.image_lesson_hello,
                        R.drawable.image_lesson_good_morning,
                        R.drawable.image_lesson_good_night,
                        R.drawable.image_lesson_thank_you
                }
        ));

        fragments.add(LessonFillInBlankQuestionFragment.newInstance(
                "When entering a store in the morning, I greet the clerk with _____",
                R.drawable.lesson_fill_in_blank_question_image,
                "早上好"
        ));

        fragments.add(LessonListeningQuestionFragment.newInstance(
                R.raw.good_morning,
                Arrays.asList("晚上好", "早上好", "下午好"),
                "早上好"
        ));


       fragments.add(LessonRearrangeWordsQuestionFragment.newInstance(
                "Rearrange the words to form the correct translation for 'Good Morning':",
                "早上好"
        ));

        fragments.add(LessonSingleOptionQuestionFragment.newInstance(
                "Which translation is correct?",
                Arrays.asList("早上好", "晚上好", "下午好"),
                "早上好"
        ));


        fragments.add(LessonSpeakingQuestionFragment.newInstance(
                "晚上好",
                "wǎn shàng hǎo",
                R.drawable.image_lesson_good_night
        ));



        return fragments;
    }
}
